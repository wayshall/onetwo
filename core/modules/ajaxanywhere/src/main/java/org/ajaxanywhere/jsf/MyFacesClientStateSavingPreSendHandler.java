package org.ajaxanywhere.jsf;

import org.ajaxanywhere.PreSendHandler;
import org.ajaxanywhere.BufferResponseWrapper;
import org.ajaxanywhere.AAUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyFacesClientStateSavingPreSendHandler implements PreSendHandler {
    private static final String TREE_KEY = "<input type=\"hidden\" name=\"jsf_tree_64\" id=\"jsf_tree_64\" value=\"";
    private static final String STATE_KEY = "<input type=\"hidden\" name=\"jsf_state_64\" id=\"jsf_state_64\" value=\"";
    private static final String SEQUENCE_KEY = "<input type=\"hidden\" name=\"jsf_sequence\" value=\"";
    private static final int TREE_KEY_LEN = TREE_KEY.length();
    private static final int STATE_KEY_LEN = STATE_KEY.length();
    private static final int SEQUENCE_KEY_LEN = SEQUENCE_KEY.length();
    private static final String ZONE_NAME = "stateSavingScript";

    public BufferResponseWrapper handle(HttpServletRequest request, BufferResponseWrapper responseWrapper) {

        String content = responseWrapper.getBuffer();
        String tree = null;
        String state = null;
        String jsf_sequence = null;
        int pos1 = content.indexOf(TREE_KEY);
        if (pos1 != -1) {
            int pos2 = content.indexOf('"', pos1 + TREE_KEY_LEN);
            if (pos2 != -1)
                tree = content.substring(pos1 + TREE_KEY_LEN, pos2);
        }
        pos1 = content.indexOf(STATE_KEY);
        if (pos1 != -1) {
            int pos2 = content.indexOf('"', pos1 + STATE_KEY_LEN);
            if (pos2 != -1)
                state = content.substring(pos1 + STATE_KEY_LEN, pos2);
        }

        pos1 = content.indexOf(SEQUENCE_KEY);
        if (pos1 != -1) {
            int pos2 = content.indexOf('"', pos1 + SEQUENCE_KEY_LEN);
            if (pos2 != -1)
                jsf_sequence = content.substring(pos1 + SEQUENCE_KEY_LEN, pos2);
        }

        try {
            if (tree != null || state != null || jsf_sequence != null) {
                responseWrapper.output(AAUtils.getZoneStartDelimiter(ZONE_NAME) + "<script type=\"text/javascript\">\n");
                if (state != null) responseWrapper.output("    var state = AjaxAnywhere.findInstance(aaInstanceId).findForm().elements[\"jsf_state_64\"]\n" +
                        "    if (state!=null && state.tagName.toLowerCase() == \"input\")\n" +
                        "                state.value=\"" + state + "\";"
                );
                if (tree != null) responseWrapper.output("    var tree = AjaxAnywhere.findInstance(aaInstanceId).findForm().elements[\"jsf_tree_64\"]\n" +
                        "    if (tree!=null && tree.tagName.toLowerCase() == \"input\")\n" +
                        "               tree.value=\"" + tree + "\";"
                );
                if (jsf_sequence != null) responseWrapper.output(
                        "   var jsf_sequence = AjaxAnywhere.findInstance(aaInstanceId).findForm().elements[\"jsf_sequence\"]\n" +
                        "    if (jsf_sequence!=null && jsf_sequence.tagName.toLowerCase() == \"input\")\n" +
                        "               jsf_sequence.value=\"" + jsf_sequence + "\";");
                responseWrapper.output("</script>" + AAUtils.getZoneEndDelimiter(ZONE_NAME));
                AAUtils.addZonesToRefresh(request, ZONE_NAME);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseWrapper;
    }
}
