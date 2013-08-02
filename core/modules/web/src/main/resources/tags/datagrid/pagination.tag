<%@tag import="org.onetwo.common.web.view.jsp.TagUtils"%>
<%@ tag pageEncoding="UTF-8" import="org.onetwo.common.web.view.jsp.grid.*"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/datagrid" %>

<%@ attribute name="page" type="org.onetwo.common.utils.Page" required="true"%>
<%@ attribute name="action" type="java.lang.String" required="true"%>

<div class="pagination">
<ul>
	<%
	if(page.isHasPre()){ %>
		<li><a href="<%= TagUtils.pageLink(action, 1)%>" style="background:none">首页</a></li>
		<li><a href="<%= TagUtils.pageLink(action, page.getPrePage())%>" style=" background:none">上一页</a></li>
	<%} 
	String pageClass = "";
	for(int pageNumber=page.getStartPage(); pageNumber<=page.getEndPage(); pageNumber++){
		if(page.getPageNo()==pageNumber){
			pageClass="class='tabOn' style='font-size:18px;color:red;'";
		}else{
			pageClass="style='font-size:12px;'";
		}
	%>
		<li><a href="<%=TagUtils.pageLink(action, pageNumber) %>" <%=pageClass%>><%=pageNumber %></a></li>
	<%
	}//end for
	
	if(page.isHasNext()){%>
		<li><a href="<%=TagUtils.pageLink(action, page.getNextPage()) %>" style=" background:none">下一页</a></li>
		<li><a href="<%=TagUtils.pageLink(action, page.getTotalPages()) %>" style=" background:none">尾页</a></li> 
	<%} //end if%>
</ul>
 </div>
 <div>
 当前第 ${page.pageNo} 页 ，一共 ${page.totalPages} 页，有 ${page.totalCount} 条数据
 </div>