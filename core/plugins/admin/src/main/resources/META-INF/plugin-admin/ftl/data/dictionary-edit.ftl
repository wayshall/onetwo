<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	
	
	<layout:override name="title">
		编辑 Dictionary ${dictionary.id} 
	</layout:override>
	
 	<layout:override name="main-content">

 	<widget:form name="dictionary" action="${siteConfig.baseURL}/data/dictionary/${dictionary.id}" method="put" label="编辑Dictionary">
 		<%@ include file="dictionary-form.jsp" %>
 	</widget:form>
	
  </layout:override>
  
</layout:extends>