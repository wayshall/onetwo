<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	

 <layout:override name="main-content">
 	<div>系统程序发生内部错误.</div>
	<div>${__exceptionStack__}</div>
	<div><a href="javascript:void(0);" onclick="history.back(-1);">返回</a></div>
</layout:override>
</layout:extends>
