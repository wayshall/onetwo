<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<#assign webPath="${moduleRequestPath}/${commonName?lower_case}"/>

<layout:extends>	
	
	<layout:override name="title">
		编辑 ${commonName} ${"$"}{${commonName?uncap_first}.${table.primaryKey.javaName}} 
	</layout:override>
	
 	<layout:override name="main-content">

 	<widget:form name="${commonName_uncapitalize}" action="${"$"}{siteConfig.baseURL}${webPath}/${"$"}{${commonName?uncap_first}.${table.primaryKey.javaName}}" method="put" label="编辑${commonName}">
 		<%@ include file="${commonName_partingLine}-form.jsp" %>
 	</widget:form>
	
  </layout:override>
  
</layout:extends>