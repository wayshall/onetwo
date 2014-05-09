<%@page import="org.onetwo.common.web.view.jsp.TagUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/datagrid" %>

<c:set var="_gridBean" value="${__tag__GridTagBean}"/>

<c:if test="${_gridBean.searchForm}">
	<widget:form id="${_gridBean.name}Search" name="${_gridBean.name }" action="${_gridBean.action}" title="搜索" method="get">
		<widget:formField name="submitTag" type="hidden" value="1"/>
	<c:forEach items="${_gridBean.searchFormBean.fields }" var="sf">
		<widget:formField name="${sf.searchFieldName }" label="${sf.label }" type="${sf.searchFieldType }" emptyOptionLabel="请选择……" items="${sf.searchItems }" itemLabel="${sf.searchItemLabel }" itemValue="${sf.searchItemValue }"/>
	</c:forEach>
		<widget:formField name="" type="submit" label="提交"  />
		
	</widget:form>
	<script>
	<c:if test="${param.submitTag!='1'}">
		$('.jfish-toggle-control').html('显示');
		$('.jfish-toggle-body').hide();
	</c:if>
	</script>
</c:if>

<aa:zone name="${_gridBean.ajaxZoneName}">
<div class="row-fluid jfish-container">
	<div class="block">
		<div class="navbar navbar-inner block-header">
			<div class="muted pull-left">
			${ not empty _gridBean.title?_gridBean.title:_gridBean.label }
			</div>
			<div class="pull-right"><a href="#" class="jfish-toggle-control" control="1">隐藏</a></div>
		</div>

		<c:set var="formId" value="${_gridBean.formId}"></c:set>
		<c:if test="${_gridBean.generatedForm }">
		<form id="${formId }" name="${formId}" action="${_gridBean.action}" method="post" class="${_gridBean.cssClass} form-horizontal">
			<input name="_method" value="post" type="hidden"/>
			<widget:formToken/>
		</c:if>
		
		<!-- table start -->
		<div class="block-content collapse in jfish-toggle-body">	
		<div class="span12">
		<div class="dataTables_wrapper form-inline" role="grid">
		
			<div class="row-fluid">
			<layout:define name="grid_custombar"/>
			</div>
			
			<c:if test="${_gridBean.toolbar }">
			<div class="well">
				<div class="btn-group pull-right">
					<a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#">
						&nbsp;&nbsp;&nbsp;操作&nbsp;&nbsp;&nbsp;
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<layout:define name="grid_toolbar"/>
						<c:if test="${_gridBean.exportable }">
						<li>
						<a href="${_gridBean.xlsFormatAction }">导出</a>
						</li>
						</c:if>
					</ul>
				</div>
			</div>
			</c:if>
				
			<table border="0" cellspacing="0"  class="table table-bordered table-striped">
				<c:if test="${not empty _gridBean.bodyContent }">
				<c:out value="${_gridBean.bodyContent}" escapeXml="false"/>
				</c:if>
				<c:forEach items="${_gridBean.rows}" var="row">
					<c:choose>
						<c:when test="${row.iterator}">
					<gridRender:iterator row="${row}"/>
						</c:when>
						<c:otherwise>
					<gridRender:row row="${row}"/>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</table>
			
			<c:if test="${_gridBean.pagination}">
			<gridRender:pagination action="${_gridBean.actionWithQueryString }" page="${_gridBean.page }" formPagination="${_gridBean.formPagination}" remote="${_gridBean.ajaxSupported}" ajaxName="${_gridBean.ajaxZoneName}"/>
			</c:if>
		
		</div>
		</div>
		</div>
		<!-- table end -->
		
		<c:if test="${_gridBean.generatedForm }">
		</form>
	</c:if>

		<script>
		jQuery("#${formId}").initDatagrid();
		</script>
	</div>

</div>
</aa:zone>
