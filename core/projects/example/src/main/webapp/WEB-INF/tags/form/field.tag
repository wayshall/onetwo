<%@tag import="org.onetwo.common.utils.StringUtils"%>
<%@tag import="org.onetwo.common.web.view.jsp.form.FormFieldTagBean"%>
<%@ tag pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%@ attribute name="field" type="org.onetwo.common.web.view.jsp.form.FormFieldTagBean" required="true"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false"%>
<%
	FormFieldTagBean ff = field;
	String _cssClass = StringUtils.isBlank(cssClass)?"form-control "+ff.getCssClass():cssClass+" "+ff.getCssClass();
	String _cssStyle = StringUtils.isBlank(cssStyle)?ff.getCssStyle():cssStyle+" "+ff.getCssStyle();
	switch(ff.getType()){
	case input:
		//<form:input path="${field.name}"/>
		%>
		<input value="${field.fieldValue}" type="text" class="<%= _cssClass %>" style="<%= _cssStyle %>" ${field.attributesHtml}/>
		<%
		break;
	case file:
	case password:
		%>
		<input value="${field.fieldValue}" type="<%= ff.getType() %>" class="<%= _cssClass %>" style="<%= _cssStyle %>" ${field.attributesHtml}/>
		<%
		break;
	case date:
		%>
		<input class="Wdate <%= _cssClass %>" style="<%= _cssStyle %>" readonly="readonly" onclick="WdatePicker({dateFmt: '${field.dataFormat}'})" value="${field.fieldValue}" ${field.attributesHtml}>
		<%
		break;
	case textarea:
		//<form:textarea path="${field.name}"/>
		%>
		<textarea class="<%= _cssClass %>" style="<%= _cssStyle %>" id="${field.id}" name="${field.name}">
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
		<form:select path="${field.name}" disabled="${field.disabled }" data-toggle="${field.title==null?'':'tooltip'}" title="${field.title}" cssClass="<%= _cssClass %>" cssStyle="<%= _cssStyle %>" onclick="${field.onclick}">
			<c:if test="${field.emptyOption}"><option value="">${t:escapeHtml(field.emptyOptionLabel)}</option></c:if>
			<form:options items="${field.items}" itemLabel="${field.itemLabel}" itemValue="${field.itemValue }" />
		</form:select>
		<%
		break;
	case button:
		%>
		<a href="${field.value }" class="btn btn-default" ${field.attributesHtml}>${field.label }</a>
		<%
		break;
	case checkbox:
		//<form:checkbox path="${field.name}" />
		%>
		<input value="${field.fieldValue}" type="checkbox" ${field.attributesHtml} class="<%= _cssClass %>" style="<%= _cssStyle %>"/>
		<%
		break;
	case checkboxGroup:
		%>
		<form:checkboxes path="${field.name}" items="${field.items}" itemLabel="${field.itemLabel}" itemValue="${field.itemValue }" readOnly="${field.readOnly}" cssClass="<%= _cssClass %>" cssStyle="<%= _cssStyle %>" disabled="${field.disabled}"/>
		<%
		break;
	case radio:
		//<form:radiobutton path="${field.name}"/>
		%>
		<input value="${field.fieldValue}" type="radio" ${field.attributesHtml} class="<%= _cssClass %>" style="<%= _cssStyle %>"/>
		<%
		break;
	case radioGroup:
		%>
		<form:radiobuttons path="${field.name}" items="${field.items}" itemLabel="${field.itemLabel}" itemValue="${field.itemValue }" cssClass="<%= _cssClass %>" cssStyle="<%= _cssStyle %>" readOnly="${field.readOnly}" disabled="${field.disabled}"/>
		<%
		break;
	case submit:
		%>
		<button name="" type="submit" class="btn btn-default" <c:if test="${field.showLoadingText}">data-loading-text="正在提交……"</c:if> ${field.attributesHtml}>${field.label }</button>
		<%
		break;
	default:
		out.println(ff.getBodyContent());
		break;
	}
%>
<c:if test="${field.errorTag}">
<p class="help-block">
	<form:errors path="${field.errorPath}" cssClass="label label-important"/>
</p>
</c:if>
