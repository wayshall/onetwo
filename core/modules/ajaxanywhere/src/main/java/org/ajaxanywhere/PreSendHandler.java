package org.ajaxanywhere;

import javax.servlet.http.HttpServletRequest;

public interface PreSendHandler {

    public BufferResponseWrapper handle(HttpServletRequest request, BufferResponseWrapper responseWrapper);

}
