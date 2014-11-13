<%@tag import="org.onetwo.common.web.config.BaseSiteConfig"%>
<%@ tag pageEncoding="UTF-8" import="org.onetwo.common.web.view.jsp.grid.*"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/grid" %>

<%@ attribute name="page" type="org.onetwo.common.utils.Page" required="true"%>
<%@ attribute name="action" type="java.lang.String" required="true"%>

<%!
public String pageLink(String action, int numb){
	String result = action;
	if(numb<=1)
		return result;
	if (action.indexOf("?")!=-1){
		result += "&pageNo="+numb;
	}else{
		result += "?pageNo="+numb;
	}
	return result;
}
%>
<div class="pages">
	<%
	if(page.isHasPre()){ %>
		<a href="<%= pageLink(action, 1)%>" style="background:none">首页</a>
		<a href="<%= pageLink(action, page.getPrePage())%>" style=" background:none">上一页</a>
	<%} 
	String pageClass = "";
	for(int pageNumber=page.getStartPage(); pageNumber<=page.getEndPage(); pageNumber++){
		if(page.getPageNo()==pageNumber){
			pageClass="class='tabOn' style='font-size:18px;color:red;'";
		}else{
			pageClass="style='font-size:12px;'";
		}
	%>
		<a href="<%=pageLink(action, pageNumber) %>" <%=pageClass%>><%=pageNumber %></a>&nbsp;
	<%
	}//end for
	
	if(page.isHasNext()){%>
		<a href="<%=pageLink(action, page.getNextPage()) %>" style=" background:none">下一页</a>
		<a href="<%=pageLink(action, page.getTotalPages()) %>" style=" background:none">尾页</a> 
	<%} //end if%>
当前第 ${page.pageNo} 页 ，一共 ${page.totalPages} 页，有 ${page.totalCount} 条数据
 </div>