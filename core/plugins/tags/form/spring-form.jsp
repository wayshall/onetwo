<%@page import="org.onetwo.common.web.view.jsp.form.FormFieldTagBean"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="formRender" tagdir="/WEB-INF/tags/form" %>

<c:set var="_formBean" value="${__tag__FormTagBean}"/>
<div class="row-fluid">
	<div class="block">
	
	
<div class="navbar navbar-inner block-header">
	<div class="muted pull-left">${ not empty _formBean.title?_formBean.title:_formBean.label }</div>
	<div class="pull-right"><a href="#" id="searchControl" control="1">隐藏</a></div>
</div>
<%--
<form id="${_formBean.id}" name="${_formBean.name }" action="${_formBean.action }" method="${_formBean.method }" enctype="${_formBean.encType}" class="form-horizontal">
 --%>

<div class="block-content collapse in" id="searchBody">

<form:form id="${_formBean.id}" modelAttribute="${_formBean.name }" action="${_formBean.action }" method="${_formBean.method }" enctype="${_formBean.encType}">
<widget:formToken/>
	<table class="table table-bordered table-striped">
	
	<c:forEach items="${_formBean.fields}" var="ff">
	<c:choose>
		<c:when test="${ff.hidden}">
			<formRender:field field="${ff}"/>
		</c:when>
		<c:otherwise>
			<tr>
				<td><c:out value="${ff.label}"/> </td>
				<td>
					<formRender:field field="${ff}"/>
				</td>
			</tr>
		</c:otherwise>
	</c:choose>
	</c:forEach>
		
	<tr>
		<td colspan="2" style="text-align:center;">
		<c:if test="${_formBean.submit!=null }">
			<formRender:field field="${_formBean.submit}"/>
		</c:if>
		<c:forEach items="${_formBean.buttons}" var="btn">
			<formRender:field field="${btn}"/>
		</c:forEach>
		</td>
	</tr>
		
	</table>
</form:form>
 </div>
<script>
$('#${_formBean.id}').tooltip({
  selector: "[data-toggle=tooltip]"
})
$('#searchControl').click(function(){
	var $this = $(this);
	if($this.attr('control')=='0'){
		$('#searchBody').show();
		$this.html('隐藏');
		$this.attr('control', '1')
	}else{
		$('#searchBody').hide();
		$this.html('显示');
		$this.attr('control', '0')
	}
});
$('#showSearch').click(function(){
	$('#searchBody').show();
});
</script>

	</div>
</div>