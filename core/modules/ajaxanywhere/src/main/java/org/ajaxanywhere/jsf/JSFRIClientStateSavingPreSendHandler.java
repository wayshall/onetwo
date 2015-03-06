package org.ajaxanywhere.jsf;

import org.ajaxanywhere.PreSendHandler;
import org.ajaxanywhere.BufferResponseWrapper;
import org.ajaxanywhere.AAUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JSFRIClientStateSavingPreSendHandler implements
        PreSendHandler {
    private static final String VIEW_KEY = "<input type=\"hidden\" name=\"com.sun.faces.VIEW\" id=\"com.sun.faces.VIEW\" value=\"";
    private static final int VIEW_KEY_LEN = VIEW_KEY.length();
    private static final String ZONE_NAME = "stateSavingScript";

    public BufferResponseWrapper handle(HttpServletRequest
            request, BufferResponseWrapper responseWrapper) {

        String content = responseWrapper.getBuffer();
        String view = null;
        int pos1 = content.indexOf(VIEW_KEY);
        if (pos1 != -1) {
            int pos2 = content.indexOf('"', pos1 + VIEW_KEY_LEN);
            if (pos2 != -1)
                view = content.substring(pos1 + VIEW_KEY_LEN, pos2);
        }

        try {

            responseWrapper.output(AAUtils.getZoneStartDelimiter(ZONE_NAME) + "<script type=\"text/javascript\">\n" +
                    "    var views = document.getElementsByName(\"com.sun.faces.VIEW\")\n" +
                    "    if (views!=null)\n" +
                    "        for (var i=0;i<views.length;i++)\n" +
                    "            if (views[i].tagName.toLowerCase() == \"input\")\n" +
                    " views[i].value=\"" + view + "\";" +

                    "</script>" + AAUtils.getZoneEndDelimiter(ZONE_NAME));
            AAUtils.addZonesToRefresh(request, ZONE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseWrapper;
    }
}

