<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<#assign webPath="${moduleRequestPath}/${commonName?lower_case}"/>

<layout:extends>	

	
	<layout:override name="title">
		新建 ${commonName}
	</layout:override>
	
 	<layout:override name="main-content">
 
 	<widget:form name="${commonName_uncapitalize}" action="${"$"}{siteConfig.baseURL}${webPath}" method="post" label="新增${commonName}">
 		<%@ include file="${commonName_partingLine}-form.jsp" %>
 	</widget:form>
	
  </layout:override>
  
</layout:extends>