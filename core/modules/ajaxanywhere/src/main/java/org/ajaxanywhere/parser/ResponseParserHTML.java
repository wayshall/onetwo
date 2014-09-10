/*
Copyright 2005  Vitaliy Shevchuk (shevit@users.sourceforge.net)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.ajaxanywhere.parser;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Pattern;

public class ResponseParserHTML implements ResponseParser {
    private static ResponseParser ourInstance = new ResponseParserHTML();
    public static final Pattern SCRIPT_START_PATTERN = Pattern.compile("<script",Pattern.CASE_INSENSITIVE);
    public static final Pattern SCRIPT_END_PATTERN = Pattern.compile("</script",Pattern.CASE_INSENSITIVE);

    public static ResponseParser getInstance() {
        return ourInstance;
    }

    private ResponseParserHTML() {
    }

    public ResponseBean parse(String html) {
        // javax.swing.text.html.parser bug workaround.
        // Swing HTML parser interpretes incorrectly <> and </> substrings
        // therefore we replace <> and </> with dummy strings and replace them back after parsing.

        String dummy = findDummy(html);
        html = html.replaceAll("<>", "<" + dummy + ">").replaceAll("</>", "</" + dummy + ">");

        // JSE 6 regression workarpond : http://forum.java.sun.com/thread.jspa?threadID=5118473
        // let's use a depricated DIR tag instead of SCRIPT
        html = SCRIPT_START_PATTERN.matcher(html).replaceAll("<DIR");
        html = SCRIPT_END_PATTERN.matcher(html).replaceAll("</DIR");

        ResponseBean responseBean = doParse(html);
        String htmlContent = responseBean.getHtmlContent();
        if (htmlContent != null)
            responseBean.setHtmlContent(htmlContent.replaceAll("<" + dummy + ">", "<>").replaceAll("</" + dummy + ">", "</>"));
        for (int i = 0; i < responseBean.getScriptContents().size(); i++) {
            String s = (String) responseBean.getScriptContents().get(i);
            responseBean.getScriptContents().set(i, s.replaceAll("<" + dummy + ">", "<>").replaceAll("</" + dummy + ">", "</>"));
        }
        return responseBean;
    }

    private String findDummy(String html) {
        String dummy;
        do {
            dummy = Double.toString(Math.random());
        } while (html.indexOf(dummy) != -1);

        return dummy;
    }

    public ResponseBean doParse(final String html) {

        ResponseBean res = new ResponseBean();
        try {
            final StringBuffer contentHTML = new StringBuffer();
            final List scripts = res.getScriptContents();
            final Set images = res.getImages();

            HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
                private boolean insideScript;
                private StringBuffer scriptContent = new StringBuffer();
                private int lastStop;

                private void append(String str) {
                    if (insideScript) {
                        scriptContent.append(str);
                    } else {
                        contentHTML.append(str);
                    }
                }

                public void appendSinceLastStop(int newPos) {
                    if (lastStop > newPos)
                        return;
                    append(html.substring(lastStop, newPos));
                    lastStop = newPos;
                }

                private void flushScript() {
                    int posScriptEnd = scriptContent.indexOf(">");
                    if (posScriptEnd == -1)
                        posScriptEnd = 0;
                    int posC1 = scriptContent.indexOf("<!--", posScriptEnd);
                    int posC11 = scriptContent.indexOf("<![CDATA[", posScriptEnd);
                    int posQ1 = scriptContent.indexOf("'", posScriptEnd);
                    int posQ2 = scriptContent.indexOf("\"", posScriptEnd);

                    if ((posC1 != -1) && (posQ2 == -1 || posC1 < posQ2) && (posQ1 == -1 || posC1 < posQ1))
                        scriptContent.delete(posC1, posC1 + 4);

                    if ((posC11 != -1) && (posQ2 == -1 || posC11 < posQ2) && (posQ1 == -1 || posC11 < posQ1))
                        scriptContent.delete(posC11, posC11 + 9);


                    posQ1 = scriptContent.lastIndexOf("'", posScriptEnd);
                    posQ2 = scriptContent.lastIndexOf("\"", posScriptEnd);
                    int posC2 = scriptContent.indexOf("-->", posScriptEnd);
                    int posC22 = scriptContent.indexOf("//]]>", posScriptEnd);

                    if ((posC2 != -1) && (posQ2 == -1 || posC2 > posQ2) && (posQ1 == -1 || posC2 > posQ1))
                        scriptContent.delete(posC2, posC2 + 3);
                    if ((posC22 != -1) && (posQ2 == -1 || posC22 > posQ2) && (posQ1 == -1 || posC22 > posQ1))
                        scriptContent.delete(posC22, posC22 + 5);

                    int len = scriptContent.length();
                    if (len >0 && scriptContent.charAt(len -1) =='>'){
                        int lastEndTagPos = scriptContent.lastIndexOf("</");
                        if (lastEndTagPos!=-1)
                            scriptContent.setLength(lastEndTagPos);
                    }
                    scripts.add(scriptContent.toString());
                    scriptContent.setLength(0);
                }

                public void handleText(char[] data, int pos) {
                    appendSinceLastStop(pos);
                }

                public void handleComment(char[] data, int pos) {
                    appendSinceLastStop(pos);
                    if (data == null)
                        flushScript();
                }

                public void handleError(String errorMsg, int pos) {

                }

                public void handleEndTag(HTML.Tag tag, int pos) {
                    if (pos == -1)
                        return;
                    if (lastStop > pos)
                        return;
                    appendSinceLastStop(pos);

                    if (tag == HTML.Tag.DIR && insideScript) { /** DIR is prevously replaced SCRIPT tag to **/
                        int posScriptEnd = html.indexOf('>', pos);

                        if (posScriptEnd != -1)
                            lastStop = posScriptEnd + 1;

                        insideScript = false;
                        flushScript();
                    }
                }

                public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes, int pos) {
                    handleStartTag(tag, attributes, pos);
                }

                public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int pos) {
                    appendSinceLastStop(pos);
                    if (tag == HTML.Tag.DIR) { /** DIR is prevously replaced SCRIPT tag to **/
                        insideScript = true;
                    } else if (tag == HTML.Tag.IMG) {
                        images.add(attributes.getAttribute(HTML.Attribute.SRC));
                    }
                }

            };

            Reader reader = new StringReader(html);
            new ParserDelegator().parse(reader, callback, false);
            callback.handleComment(null, html.length());

            res.setHtmlContent(contentHTML.toString());
            return res;

        } catch (IOException e) {
            throw new RuntimeException(e.toString()); // this should never heppen
        }
    }

}
