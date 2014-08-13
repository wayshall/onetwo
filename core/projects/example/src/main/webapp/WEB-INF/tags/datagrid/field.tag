<%@ tag pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ attribute name="field" type="org.onetwo.common.web.view.jsp.grid.FieldTagBean" required="true"%>
<%@ attribute name="entity" type="java.lang.Object" required="true"%>

<td ${field.gridAttributesHtml} class="wordbreak">
	<c:choose>
		<c:when test="${field.checkbox }">
			<c:set var="chbValue" value="${entity[field.value]}"></c:set>
			<input type="checkbox" name="${field.name}" value="${chbValue }" id="${field.name}${chbValue}" class="dg-checkbox-field" <c:if test="${field.reserved!=null}">reserved="${entity.originData[field.reserved]}"</c:if>/>
		</c:when>
		<c:when test="${field.radio }">
			<c:set var="radValue" value="${entity[field.value]}"></c:set>
			<input type="radio" name="${field.name}" value="${radValue }" id="${field.name}${radValue}" class="dg-checkbox-field" <c:if test="${field.reserved!=null}">reserved="${entity.originData[field.reserved]}"</c:if>/>
		</c:when>
		<c:otherwise>
			${entity[field.value]}
		</c:otherwise>
	</c:choose>

</td>
	