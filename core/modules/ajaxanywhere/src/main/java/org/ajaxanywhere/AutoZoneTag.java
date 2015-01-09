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

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 * @jsp.tag name = "autoZone"
 * display-name = "autoZone"
 * description = ""
 */
public class AutoZoneTag extends TagSupport {


    private String forId;

    public String getForId() {
        return forId;
    }

    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     * description="indicates that the content goes into the innerHTLM of another DOM object"
     */
    public void setForId(String forId) {
        this.forId = forId;
    }

    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     * description="unique ID"
     */
    public void setId(String id) {
        super.setId(id);
    }


    public int doStartTag() throws JspException {
        try {
            if (id!=null){
                pageContext.getOut().print("<span id=\""+id.replaceAll("\"", "&quot;")+"\">");
            }
            pageContext.getOut().print(AutoZoneUtils.getStartMarker(getZoneId()));
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_BODY_INCLUDE;
    }

    private String getZoneId() {
        if (id!=null && forId == null)
            return id;
        else if (id==null && forId != null)
            return forId;
        else
            throw new IllegalArgumentException("a single attribute, either id or forId must be specified for <aa:autoZone> tag");
    }

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print(AutoZoneUtils.getEndMarker(getZoneId()));
            if (id!=null){
                pageContext.getOut().print("</span>");
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public int doAfterBody() throws JspException {
        return super.doAfterBody();
    }


    public void setPageContext(PageContext pageContext) {
        forId=null;
        setId(null);
        super.setPageContext(pageContext);
    }
}
