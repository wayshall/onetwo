<%@ tag pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ attribute name="field" type="org.onetwo.common.web.view.jsp.grid.FieldTagBean" required="true"%>
<%@ attribute name="entity" type="java.lang.Object" required="true"%>

<c:choose>
	<c:when test="${field.checkbox }">
		<td>
			<c:set var="chbValue" value="${entity[field.value]}"></c:set>
			<input type="checkbox" name="${field.name}" value="${chbValue }" id="${field.name}${chbValue}" class="dg-checkbox-field" ${field.gridAttributesHtml} <c:if test="${field.reserved!=null}">reserved="${entity.originData[field.reserved]}"</c:if>/>
		</td>
	</c:when>
	<c:when test="${field.radio }">
		<td>
			<c:set var="radValue" value="${entity[field.value]}"></c:set>
			<input type="radio" name="${field.name}" value="${radValue }" id="${field.name}${radValue}" class="dg-checkbox-field" ${field.gridAttributesHtml}  <c:if test="${field.reserved!=null}">reserved="${entity.originData[field.reserved]}"</c:if>/>
		</td>
	</c:when>
	<c:otherwise>
		<td ${field.gridAttributesHtml} ${field.dynamicAttributesHtml}>
			${entity[field.value]}
		</td>
	</c:otherwise>
</c:choose>
	