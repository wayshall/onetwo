<%@ page contentType="text/html;charset=UTF-8" import="org.onetwo.common.web.view.jsp.grid.*" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/grid" %>

<c:set var="_gridBean" value="${__tag__GridTagBean}"/>
<div class="page-header">
<h2>
${ not empty _gridBean.title?_gridBean.title:_gridBean.label }
</h2>
</div>

<c:set var="formId" value="${_gridBean.id}_from"></c:set>
<form id="${formId }" name="${formId}" action="${_gridBean.action}" method="post" class="${_gridBean.cssClass}">
<input name="_method" value="post" type="hidden"/>

<table border="0" cellspacing="0"  class="table table-bordered table-striped">
	<c:forEach items="${_gridBean.rows}" var="row">
		<c:choose>
			<c:when test="${row.iterator}">
		<gridRender:iterator row="${row}" entity="${entity}"/>
			</c:when>
			<c:otherwise>
		<gridRender:row row="${row}" entity="${entity}"/>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</table>
<gridRender:pagination action="${_gridBean.actionWithQueryString }" page="${_gridBean.page }"/>

</form>
<script>
jQuery("#${formId}").initDatagrid();
</script>
