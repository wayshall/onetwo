<%@ tag pageEncoding="UTF-8" import="org.onetwo.common.web.view.jsp.grid.*"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/grid" %>

<%@ attribute name="row" type="org.onetwo.common.web.view.jsp.grid.RowTagBean" required="true"%>
<%@ attribute name="entity" type="java.lang.Object" required="true"%>

<c:if test="${row.header }">
<thead>
</c:if>

<tr id="${row.id}"
	name="${row.name}" style="${row.cssStyle}" 
	class="${row.cssClass}" onclick="${row.onclick}">
	<c:forEach items="${row.fields}" var="field">
		<gridRender:field entity="${entity}" field="${field}"></gridRender:field>
	</c:forEach>
</tr>

<c:if test="${row.header }">
</thead>
</c:if>