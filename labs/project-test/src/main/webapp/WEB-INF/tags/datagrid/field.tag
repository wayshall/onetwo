<%@ tag pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ attribute name="field" type="org.onetwo.common.web.view.jsp.grid.FieldTagBean" required="true"%>
<%@ attribute name="entity" type="java.lang.Object" required="true"%>

<td id="${field.id}" colspan="${field.colspan}"
	name="${field.name}" style="${field.cssStyle}" 
	class="${field.cssClass}" onclick="${field.onclick}" >
	<c:choose>
		<c:when test="${field.checkbox }">
		<c:set var="chbValue" value="${entity[field.value]}"></c:set>
		<input type="checkbox" name="${field.name}" value="${chbValue }" id="${field.name}${chbValue}" class="dg-checkbox-field"/>
		</c:when>
		<c:otherwise>
			${entity[field.value]}
		</c:otherwise>
	</c:choose>

</td>
	