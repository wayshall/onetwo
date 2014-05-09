<%@tag import="java.util.Date"%>
<%@ attribute name="path" type="java.lang.String" required="true"%>
<%@ attribute name="min" type="java.lang.Boolean"%>
<%@ attribute name="dev" type="java.lang.Boolean"%>
<%
	String cssPath = path;
	if(min!=null && min){
		cssPath += ".min";
	}
	cssPath += ".css";
	if(dev!=null && dev){
		cssPath += "?t=" + new Date().getTime();
	}
%>
<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}<%= cssPath %>">