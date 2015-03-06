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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;

/**
 * Date: 23 juil. 2005
 * Time: 21:02:45
 */
public class AAFilter implements Filter {

    public String preSendHandlerClassName;

    public void init(FilterConfig filterConfig) throws ServletException {
        preSendHandlerClassName = filterConfig.getInitParameter("preSendHandlerClass");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!AAUtils.isAjaxRequest(servletRequest)) {
            filterChain.doFilter(servletRequest, response);
            return;
        }


        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml;charset=utf-8");

        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");

        BufferResponseWrapper bufferResponseWrapper = new BufferResponseWrapper(response);

        Set refreshZones = new HashSet();
        AAUtils.setZonesToRefresh(request, refreshZones);
        AAUtils.getRefreshZonesFromURL(request);

        try {
            filterChain.doFilter(request, bufferResponseWrapper);

            bufferResponseWrapper = execHandler(request,bufferResponseWrapper);

            if (bufferResponseWrapper.getRedirect() != null) {
                XMLHandler.sendRedirect(bufferResponseWrapper);
            } else {
                XMLHandler.sendZones(bufferResponseWrapper, refreshZones);
            }
        }

        catch (Throwable e) {
            XMLHandler.handleError(response, e);
        }

    }

    private BufferResponseWrapper execHandler(HttpServletRequest request, BufferResponseWrapper bufferResponseWrapper) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (preSendHandlerClassName!=null){
            Class hClass;
            try {
                hClass = Thread.currentThread().getContextClassLoader().loadClass(preSendHandlerClassName);
            } catch (ClassNotFoundException e) {
                hClass = Class.forName(preSendHandlerClassName);
                e.printStackTrace();
            }
            PreSendHandler handler = (PreSendHandler) hClass.newInstance();
            return handler.handle(request, bufferResponseWrapper);
        }
        return bufferResponseWrapper;
    }


    public void destroy() {
    }
}
