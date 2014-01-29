<%@ attribute name="name" type="java.lang.String" required="true"%>
<%@ attribute name="module" type="java.lang.String"%>
<%@ attribute name="min" type="java.lang.Boolean"%>
<%
	String jspath = name;
	if(module!=null){
		jspath = "/"+module + "/" + name;
	}
	if(min!=null && min){
		jspath += ".min";
	}
%>
<script type="text/javascript" src="${siteConfig.jsPath}<%=jspath %>.js"></script>