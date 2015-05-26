<%@page import="org.onetwo.common.web.view.jsp.TagUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/tags_new/datagrid" %>

<c:set var="_gridBean" value="${__tag__GridTagBean}"/>
<c:set var="_gridContainerId" value="${__tag__GridTagBean.id}Container"/>

<aa:zone name="${_gridBean.ajaxZoneNameFull}">
<c:if test="${_gridBean.searchForm}">
	<widget:form id="${_gridBean.name}Search" name="${_gridBean.name }" action="${_gridBean.action}" title="搜索" method="get">
		<widget:formField name="submitTag" type="hidden" value="1"/>
	<c:forEach items="${_gridBean.searchFormBean.fields }" var="sf">
		<widget:formField name="${sf.searchFieldName }" label="${sf.label }" type="${sf.searchFieldType }" emptyOptionLabel="请选择……" items="${sf.searchItems }" itemLabel="${sf.searchItemLabel }" itemValue="${sf.searchItemValue }" dataFormat="${sf.dataFormat}"/>
	</c:forEach>
		<c:choose>
			<c:when  test="${_gridBean.ajaxSupported}">
				<widget:formField name="" showLoadingText="false" type="submit" label="提交" cssClass="form-button" data-confirm="false" remote="true" ajaxName="${_gridBean.ajaxZoneName}" />
			</c:when>
			<c:otherwise>
				<widget:formField name="" showLoadingText="false" type="submit" label="提交" cssClass="form-button" data-confirm="false"/>
			</c:otherwise>
		</c:choose>		
		
	</widget:form>
	<script>
	<c:if test="${param.submitTag!='1'}">
		$('.jfish-toggle-control').html('显示');
		$('.jfish-toggle-body').hide();
	</c:if>
	</script>
</c:if>
<!-- 大表格那样的搜索控件插入点 -->
<layout:define name="grid_search"/>
<div class="datagrid jfish-container">
		<div class="title">
			<span class="mark"></span>
        	<span>${ not empty _gridBean.title?_gridBean.title:_gridBean.label }</span>
			<a href="#" class="jfish-toggle-control " control="1">隐藏</a>
			<a href="javascript:history.back();">返回</a>
			<c:if test="${helper.requestMethod=='get'}">
				<a href="${t:addParam( _gridBean.actionWithQueryString, 'theme.jsui', !_gridBean.jsgrid) }" class="" control="1">切换表格</a>
			</c:if>
		</div>

		
		<!-- table start -->
		<div class="fade in jfish-toggle-body">	
		
		<layout:define name="${_gridBean.customform}"/>
		<aa:zone name="${_gridBean.ajaxZoneName}">
		<c:set var="formId" value="${_gridBean.formId}"></c:set>
		<c:if test="${_gridBean.generatedForm }">
		<form id="${formId }" name="${formId}" action="${_gridBean.action}" method="post" class="${_gridBean.cssClass} form-horizontal datagrid" style="width: 100%; border: none; box-shadow: none;">
			<input name="_method" value="post" type="hidden"/>
			<widget:formToken/>
			<div class="toolbar">
			<layout:define name="grid_insert"/>
		</c:if>
			<layout:define name="${_gridBean.custombar }"/>
			<!-- aa -->
			<c:if test="${_gridBean.toolbar }">
				<layout:define name="grid_toolbar"/>
				<c:if test="${_gridBean.exportable }">
				<a href="${_gridBean.xlsFormatAction }" show-tips="此操作可能需要时间比较长，请耐心等候……">导出</a>
				</c:if>
			</c:if>
		</div>
		<div id="${_gridContainerId}">		
			<table id="${_gridBean.id}" class="datas" border="0" cellpadding="0" cellspacing="0">
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
		</div>
			
			<c:if test="${_gridBean.pagination}">
			<gridRender:pagination action="${_gridBean.actionWithQueryString }" page="${_gridBean.page }" formPagination="${_gridBean.formPagination}" remote="${_gridBean.ajaxSupported}" ajaxName="${_gridBean.ajaxZoneName}"/>
			</c:if>
	
		<c:if test="${_gridBean.generatedForm }">
		</form>
		</c:if>
		
	<c:if test="${_gridBean.jsgrid}">
		<script>
		Ext.require([
		    'Ext.data.*',
		    'Ext.grid.*',
		    'Ext.ux.grid.TransformGrid'
		]);

		var grid = Ext.create('Ext.ux.grid.TransformGrid', '${_gridBean.id}', {
            stripeRows: true
        });
        grid.render('${_gridContainerId}');
		</script>
     </c:if>
     
		<script>
		jQuery("#${formId}").initDatagrid();
		</script>
</aa:zone>
		</div>
		<!-- table end -->
</div>
</aa:zone>