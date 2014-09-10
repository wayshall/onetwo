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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Date: 24 juil. 2005
 * Time: 14:32:12
 */
public class AAUtils {

    public static boolean isAjaxRequest(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        return "true".equals(request.getParameter(AAConstants.AJAX_URL_INDETIFIER));
    }

    public static boolean isAjaxRequest(Map requestParameterMap) {
        return "true".equals(requestParameterMap.get(AAConstants.AJAX_URL_INDETIFIER));
    }

    /**
     * @deprecated use addZonesToRefresh instead
     * @see AAUtils#addZonesToRefresh(javax.servlet.ServletRequest, String)
     * @param request
     * @param commaSeparatedZonesList
     */
    public static void addZonesToRefreh(ServletRequest request, String commaSeparatedZonesList) {
        addZonesToRefresh(request, commaSeparatedZonesList);
    }
    public static void addZonesToRefresh(ServletRequest request, String commaSeparatedZonesList) {
        Set res = getZonesToRefresh(request);
        if (res == null)
            return;
        StringTokenizer st = new StringTokenizer(commaSeparatedZonesList, ",;", false);
        while (st.hasMoreTokens()) res.add(st.nextToken());
    }

    /**
     * @deprecated use getZonesToRefresh instead
     * @see AAUtils#getZonesToRefresh(javax.servlet.ServletRequest)
     * @param request
     */
    public static Set getZonesToRefreh(ServletRequest request) {
        return getZonesToRefresh(request);
    }

    public static Set getZonesToRefresh(ServletRequest request) {
        return (Set) request.getAttribute(AAConstants.REFRESH_ZONES_KEY);
    }

    public static Set getZonesToRefresh(Map requestMap) {
        return (Set) requestMap.get(AAConstants.REFRESH_ZONES_KEY);
    }


    static void setZonesToRefresh(ServletRequest request, Set zones) {
        request.setAttribute(AAConstants.REFRESH_ZONES_KEY, zones);
    }

    public static void getRefreshZonesFromURL(ServletRequest request) {
        String[] zones = request.getParameterValues(AAConstants.ZONES_URL_KEY);
        if (zones != null) {
            for (int i1 = 0; zones != null && i1 < zones.length; i1++) {
                String zone1 = zones[i1];
                addZonesToRefresh(request, zone1);
            }
        }
    }

    static String getZoneContent(String zone, BufferResponseWrapper bufferResponseWrapper) {
        String res = bufferResponseWrapper.findSubstring(getZoneStartDelimiter(zone), getZoneEndDelimiter(zone));
        return res;
    }

    public static String getZoneStartDelimiter(String zone) {
        return "<div style=\"display:inline;\" id=\"" + AAConstants.ZONE_HTML_ID_PREFIX+
                zone.replaceAll("\"", "&quot;")  + "\">";
    }
    
    public static String getZoneEndDelimiter(String zone) {
        return AAConstants.END_OF_ZONE_PREFIX+zone+AAConstants.END_OF_ZONE_SUFFIX;
    }

}
