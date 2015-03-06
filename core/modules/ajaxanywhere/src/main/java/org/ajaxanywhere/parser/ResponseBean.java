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

import java.util.*;

public class ResponseBean {

    private String htmlContent;
    private List scriptContents = new ArrayList();
    private Set images = new HashSet();

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public List getScriptContents() {
        return scriptContents;
    }

    /**
     * @return unique list of image source paths
     */
    public Set getImages() {
        return images;
    }
}
