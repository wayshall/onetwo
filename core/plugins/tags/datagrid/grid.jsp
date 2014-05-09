<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/datagrid" %>

<c:set var="_gridBean" value="${__tag__GridTagBean}"/>

<c:if test="${_gridBean.searchForm}">
	<widget:form id="${_gridBean.name}Search" name="${_gridBean.name }" title="搜索" method="get">
	<c:forEach items="${_gridBean.searchFormBean.fields }" var="sf">
		<widget:formField name="submitTag" type="hidden" value="1"/>
		<widget:formField name="${sf.searchFieldName }" label="${sf.label }" type="${sf.searchFieldType }" emptyOptionLabel="请选择……" items="${sf.searchItems }" itemLabel="${sf.searchItemLabel }" itemValue="${sf.searchItemValue }"/>
		<widget:formField name="" type="submit" label="提交" />
	</c:forEach>
	</widget:form>
</c:if>

<div class="row-fluid">
	<div class="block">
		<div class="navbar navbar-inner block-header">
			<div class="muted pull-left">
			${ not empty _gridBean.title?_gridBean.title:_gridBean.label }
			</div>
		</div>

		<c:set var="formId" value="${_gridBean.formId}"></c:set>
		<c:if test="${_gridBean.generatedForm }">
		<form id="${formId }" name="${formId}" action="${_gridBean.action}" method="post" class="${_gridBean.cssClass}">
			<input name="_method" value="post" type="hidden"/>
		</c:if>
		
		<!-- table start -->
		<div class="block-content collapse in">	
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
					</ul>
				</div>
			</div>
			</c:if>
				
			<table border="0" cellspacing="0"  class="table table-bordered table-striped">
				<c:if test="${not empty _gridBean.bodyContent }">
				<c:out value="${_gridBean.bodyContent}"/>
				</c:if>
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
			
			<c:if test="${_gridBean.pagination}">
			<gridRender:pagination action="${_gridBean.actionWithQueryString }" page="${_gridBean.page }" formPagination="${_gridBean.formPagination}"/>
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
		<c:if test="${param.submitTag!='1'}">
			$('#searchControl').click();
		</c:if>
		</script>
	</div>
</div>
