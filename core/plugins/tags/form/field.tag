<%@tag import="org.onetwo.common.web.view.jsp.form.FormFieldTagBean"%>
<%@ tag pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%@ attribute name="field" type="org.onetwo.common.web.view.jsp.form.FormFieldTagBean" required="true"%>

<%
	FormFieldTagBean ff = field;
	switch(ff.getType()){
	case input:
		//<form:input path="${field.name}"/>
		%>
		<input value="${field.fieldValue}" type="text" ${field.attributesHtml}/>
		<%
		break;
	case file:
	case password:
		%>
		<input value="${field.fieldValue}" type="<%= ff.getType() %>" ${field.attributesHtml}/>
		<%
		break;
	case date:
		%>
		<input class="Wdate" readonly="readonly" onclick="WdatePicker({dateFmt: '${field.dataFormat}'})" value="${field.fieldValue}" ${field.attributesHtml}>
		<%
		break;
	case textarea:
		//<form:textarea path="${field.name}"/>
		%>
		<textarea id="${field.id}" name="${field.name}">
		<c:out value="${field.fieldValue}"/>
		</textarea>
		<%
		break;
	case hidden:
		%>
		<input value="${field.fieldValue}" type="hidden" ${field.attributesHtml}/>
		<%
		break;
	case select:
		%>
		<form:select path="${field.name}" disabled="${field.disabled }" data-toggle="${field.title==null?'':'tooltip'}" title="${field.title}">
			<c:if test="${field.emptyOption}"><option value="">${t:escapeHtml(field.emptyOptionLabel)}</option></c:if>
			<form:options items="${field.items}" itemLabel="${field.itemLabel}" itemValue="${field.itemValue }" />
		</form:select>
		<%
		break;
	case button:
		%>
		<a href="${field.value }" class="btn">${field.label }</a>
		<%
		break;
	case checkbox:
		//<form:checkbox path="${field.name}" />
		%>
		<input value="${field.fieldValue}" type="checkbox" ${field.attributesHtml}/>
		<%
		break;
	case checkboxGroup:
		%>
		<form:checkboxes path="${field.name}" items="${field.items}" itemLabel="${field.itemLabel}" itemValue="${field.itemValue }" readOnly="${field.readOnly}" disabled="${field.disabled}"/>
		<%
		break;
	case radio:
		//<form:radiobutton path="${field.name}"/>
		%>
		<input value="${field.fieldValue}" type="radio" ${field.attributesHtml}/>
		<%
		break;
	case radioGroup:
		%>
		<form:radiobuttons path="${field.name}" items="${field.items}" itemLabel="${field.itemLabel}" itemValue="${field.itemValue }" readOnly="${field.readOnly}" disabled="${field.disabled}"/>
		<%
		break;
	case submit:
		%>
		${ff.label }<input name="" type="submit" value="${field.label }" class="btn btn-primary"/>
		<%
		break;
	default:
		out.println(ff.getBodyContent());
		break;
	}
%>
<c:if test="${field.errorTag}">
<form:errors path="${field.errorPath}" cssClass="label label-important"/>
</c:if>
