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

package org.ajaxanywhere;

import org.ajaxanywhere.parser.ResponseBean;
import org.ajaxanywhere.parser.ResponseParser;
import org.ajaxanywhere.parser.ResponseParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Date: 23 juil. 2005
 * Time: 21:50:21
 */
public class XMLHandler {

    public static void sendZones(BufferResponseWrapper bufferResponseWrapper, Set refreshZones) {
        Document doc = newDocument();
        Element root = addRootElement(doc, AAConstants.AA_XML_ZONES);

        List scripts = new ArrayList();
        Set images = new HashSet();

        for (Iterator iterator = refreshZones.iterator(); iterator.hasNext();) {
            String zone = (String) iterator.next();
            String content = AAUtils.getZoneContent(zone, bufferResponseWrapper);

            //if zone added to refresh list but not present in content, then exclude zone info in response
            if(content == null) {
                continue;
            }
            Element zoneNode = doc.createElement(AAConstants.AA_XML_ZONE);
            zoneNode.setAttribute(AAConstants.AA_XML_NAME, zone);

            handleZoneContent(content, zoneNode, doc, scripts, images, root);
        }

        for (int i = 0; i < scripts.size(); i++) {
            String script = (String) scripts.get(i);
            int posScriptEnd = script.indexOf('>');
            if (posScriptEnd != -1)
                script = script.substring(posScriptEnd + 1);

            Element scriptNode = doc.createElement(AAConstants.AA_XML_SCRIPT);
            appendText(scriptNode, doc, script);
            root.appendChild(scriptNode);
        }

        for (Iterator it = images.iterator(); it.hasNext();) {
            String image = (String) it.next();
            Element imageNode = doc.createElement(AAConstants.AA_XML_IMAGE);
            appendText(imageNode, doc, image);
            root.appendChild(imageNode);
        }

        sendDOMDocument(bufferResponseWrapper.getOriginalResponse(), doc);
    }

    private static void handleZoneContent(String content, Element zoneNode, Document doc, List scripts, Set images, Element root) {
        ResponseParser parser = ResponseParserFactory.getInstance().getParser();
        ResponseBean result = parser.parse(content);

        appendText(zoneNode, doc, result.getHtmlContent());
        scripts.addAll(result.getScriptContents());
        images.addAll(result.getImages());
        root.appendChild(zoneNode);
    }


    private static Element addRootElement(Document doc, String rootTagName) {
        Element root = doc.createElement(rootTagName);
        doc.appendChild(root);
        return root;
    }

    private static void sendDOMDocument(HttpServletResponse originalResponse, Document doc) {
        try {

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.transform(new DOMSource(doc), new StreamResult(originalResponse.getOutputStream()));

            originalResponse.flushBuffer();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static Document newDocument() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }


    private static void appendText(Element zoneNode, Document doc, String content) {
        zoneNode.appendChild(doc.createCDATASection(content.replaceAll("\r", "")));
    }

    public static void handleError(HttpServletResponse response, Throwable exception) {
        Document doc = newDocument();

        Element root = addRootElement(doc, AAConstants.AA_XML_EXCEPTION);
        root.setAttribute(AAConstants.AA_XML_TYPE, exception.getClass().getName());
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        appendText(root, doc, sw.toString());

        sendDOMDocument(response, doc);

    }

    public static void sendRedirect(BufferResponseWrapper bufferResponseWrapper) {
        Document doc = newDocument();
        Element root = addRootElement(doc, AAConstants.AA_XML_REDIRECT);
        String redirect = bufferResponseWrapper.getRedirect();
        appendText(root, doc, redirect);

        sendDOMDocument(bufferResponseWrapper.getOriginalResponse(), doc);
    }
}
