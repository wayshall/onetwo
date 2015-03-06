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
 * @jsp.tag name = "zone"
 * display-name = "zone"
 * description = ""
 */
public class ZoneTag extends TagSupport {
    private static final boolean DEFAULT_SKIP_INF_NOT_INCLUDED = false;

    private String name;
    private boolean skipIfNotIncluded = DEFAULT_SKIP_INF_NOT_INCLUDED;

    public String getName() {
        return name;
    }

    /**
     * @param name String
     * @jsp.attribute required="true"
     * rtexprvalue="true"
     * type="java.lang.String"
     * description="name description"
     */
    public void setName(String name) {
        this.name = name;
    }

    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().print(AAUtils.getZoneStartDelimiter(name));
        } catch (IOException e) {
            throw new JspException(e);
        }

        ServletRequest request = pageContext.getRequest();
        if (skipIfNotIncluded
                && AAUtils.isAjaxRequest(request)
                && !AAUtils.getZonesToRefresh(request).contains(name)
                )
            return SKIP_BODY;
        else
            return EVAL_BODY_INCLUDE;

    }

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().print(AAUtils.getZoneEndDelimiter(name));
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public int doAfterBody() throws JspException {
        return super.doAfterBody();
    }


    public void setPageContext(PageContext pageContext) {
        skipIfNotIncluded = DEFAULT_SKIP_INF_NOT_INCLUDED;
        super.setPageContext(pageContext);
    }

    public boolean isSkipIfNotIncluded() {
        return skipIfNotIncluded;
    }

    /**
     * @param skipIfNotIncluded boolean
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="boolean"
     * description="if the zone is not in the include list, tag content is not evaluated."
     */
    public void setSkipIfNotIncluded(boolean skipIfNotIncluded) {
        this.skipIfNotIncluded = skipIfNotIncluded;
    }
}
