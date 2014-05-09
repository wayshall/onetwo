<%@tag import="org.onetwo.common.web.view.jsp.TagUtils"%>
<%@ tag pageEncoding="UTF-8" import="org.onetwo.common.web.view.jsp.grid.*"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/datagrid" %>

<%@ attribute name="page" type="org.onetwo.common.utils.Page" required="true"%>
<%@ attribute name="action" type="java.lang.String" required="true"%>
<%@ attribute name="formPagination" type="java.lang.Boolean" required="true"%>

<%
	String linkText = "";
	if(formPagination){
		linkText = " data-method='post'";
	}
%>
<div class="dataTables_paginate paging_bootstrap pagination">
<ul>
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
</ul>
 </div>
 <div>
 当前第 ${page.pageNo} 页 ，一共 ${page.totalPages} 页，有 ${page.totalCount} 条数据
 </div>