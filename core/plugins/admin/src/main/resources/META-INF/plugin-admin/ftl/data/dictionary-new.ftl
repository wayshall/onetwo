<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	

	
	<layout:override name="title">
		新建 Dictionary
	</layout:override>
	
 	<layout:override name="main-content">
 
 	<widget:form name="dictionary" action="${siteConfig.baseURL}/data/dictionary" method="post" label="新增Dictionary">
 		<%@ include file="dictionary-form.jsp" %>
 	</widget:form>
	
  </layout:override>
  
</layout:extends>