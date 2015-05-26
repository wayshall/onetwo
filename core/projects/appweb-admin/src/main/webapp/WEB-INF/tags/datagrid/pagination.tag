<%@tag import="org.onetwo.common.web.view.jsp.TagUtils"%>
<%@ tag pageEncoding="UTF-8" import="org.onetwo.common.web.view.jsp.grid.*"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/datagrid" %>

<%@ attribute name="page" type="org.onetwo.common.utils.Page" required="true"%>
<%@ attribute name="action" type="java.lang.String" required="true"%>
<%@ attribute name="formPagination" type="java.lang.Boolean" required="true"%>
<%@ attribute name="remote" type="java.lang.Boolean" required="true" %>
<%@ attribute name="ajaxName" type="java.lang.String"%>

<%
	String linkText = "";
	if(remote!=null && remote){
		linkText = " remote='true' ajaxName='"+ajaxName+"'";
	}
	if(formPagination){
		linkText += " data-method='post'";
	}else if(remote!=null && remote){
		linkText += " data-method='get'";
	}
%>
<div class="dataTables_paginate paging_bootstrap pagination">
<ul class="clearfix">
	<%
	if(page.isHasPre()){ %>
		<li><a href="<%= TagUtils.pageLink(action, 1)%>" style="background:none" <%=linkText %>>首页</a></li>
		<li><a href="<%= TagUtils.pageLink(action, page.getPrePage())%>" style=" background:none" <%=linkText %>>上一页</a></li>
	<%} 
	if(page.getEndPage()!=0){
		String pageClass = "";
		for(int pageNumber=page.getStartPage(); pageNumber<=page.getEndPage(); pageNumber++){
			if(page.getPageNo()==pageNumber){
				pageClass="class='tabOn' style='font-size:18px;color:red;'";
			}else{
				pageClass="style='font-size:12px;'";
			}
		%>
			<li><a href="<%=TagUtils.pageLink(action, pageNumber) %>" <%=pageClass%> <%=linkText %> ><%=pageNumber %></a></li>
		<%
		}//end for
	}
	
	if(page.isHasNext()){%>
		<li><a href="<%=TagUtils.pageLink(action, page.getNextPage()) %>" style=" background:none" <%=linkText %>>下一页</a></li>
		<li><a href="<%=TagUtils.pageLink(action, page.getTotalPages()) %>" style=" background:none" <%=linkText %>>尾页</a></li> 
	<%} //end if%>
	<li style="margin-left: 12px; float: left"> 每页显示 <input id="btnPageSize" name="pageSize" style="width:30px;height: 30px;text-align: center; font-size:14px;font-weight: 700" value="${page.pageSize}" action="${TagUtils.pageLink(action, 1)}" remote="${remote}" ajaxName="${ajaxName }"/> 条记录</li>
</ul>
 </div>
 <div>
 当前第 ${page.pageNo} 页 ，一共 ${page.totalPages} 页，有 ${page.totalCount} 条数据
 </div>
 <layout:override name="cssscript">
 	<style>
 		.pagination ul li { height: 30px; line-height: 30px; float: left; }
 		.pagination ul li a { height: 30px; line-height: 30px; }
 	</style>
 </layout:override>