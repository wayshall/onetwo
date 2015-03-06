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

/**
 * Date: 23 juil. 2005
 * Time: 21:36:48
 */
public interface AAConstants {

    String REFRESH_ZONES_KEY = "AjaxAnywhere.refreshZones";
    String REFRESH_ALL_KEY = "AjaxAnywhere.refreshAll";
    String ZONE_CONTENT_KEY_PREFIX = "AjaxAnywhere.zoneContent.";

    String AJAX_URL_INDETIFIER = "aaxmlrequest";
    String ZONES_URL_KEY = "aazones";
    String ZONE_HTML_ID_PREFIX = "aazone.";

    String END_OF_ZONE_PREFIX = "<!-- @end of zone [";
    String END_OF_ZONE_SUFFIX = "]@ --></div>";

    //constants used in the XML content returned to the client
    String AA_XML_ZONES = "zones";
    String AA_XML_ZONE = "zone";
    String AA_XML_NAME = "name";
    String AA_XML_ID= "id";
    String AA_XML_SCRIPT = "script";
    String AA_XML_IMAGE = "image";
    String AA_XML_EXCEPTION = "exception";
    String AA_XML_TYPE = "type";
    String AA_XML_REDIRECT = "redirect";
	
}
