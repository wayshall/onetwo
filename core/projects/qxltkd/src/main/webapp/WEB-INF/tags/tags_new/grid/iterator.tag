<%@ tag pageEncoding="UTF-8" import="org.onetwo.common.web.view.jsp.grid.*"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ taglib prefix="gridRender" tagdir="/WEB-INF/tags/tags_new/grid" %>

<%@ attribute name="row" type="org.onetwo.common.web.view.jsp.grid.RowTagBean" required="true"%>
<%@ attribute name="entity" type="java.lang.Object" required="true"%>

<c:if test="${row.renderHeader}">
	<thead>
		<tr id="${row.id}"
			name="${row.name}" style="${row.cssStyle}" 
			class="${row.cssClass}" onclick="${row.onclick}">
		<c:forEach items="${row.fields}" var="field">
			<th id="${field.id}" colspan="${field.colspan}"
				name="${field.name}" style="${field.cssStyle}" 
				class="${field.cssClass}" onclick="${field.onclick}">
			
			<%
				FieldTagBean fieldBean = (FieldTagBean) getJspContext().getAttribute("field");
				String linkText = "";
				if(fieldBean.isOrderable()){
					linkText = "<a href='";
					linkText += fieldBean.appendOrderBy(fieldBean.getRowTagBean().getGridTagBean().getAction());
							
					linkText += "'>";
					linkText +=fieldBean.getLabel();
					
					if (fieldBean.isOrdering() && fieldBean.getOrderType()==":desc"){
						linkText += "↑";
					}else if(fieldBean.isOrdering() && fieldBean.getOrderType()==":asc"){
						linkText += "↓";
					}
					
					linkText += "</a>";
					out.println(linkText);
				}else{
					out.println(fieldBean.getLabel());
				}
				
				if(fieldBean.isCheckbox()){
					%>
					<input type="checkbox" name="all_<%=fieldBean.getName() %>" value="" id="id_all_<%=fieldBean.getName() %>" class="dg-checkbox-all"/>
					<%
				}
			%>
			
			</th>
		</c:forEach>
		</tr>
	</thead>
</c:if>
	
<c:if test="${row.gridTagBean.page.result.size() <= 0}">
	<tr class="page-no-datas"><td colspan="${row.gridTagBean.colspan}" style="text-align:center">没有数据</td></tr>
</c:if>

<c:forEach items="${row.gridTagBean.page.result}" var="entity">
<%
request.setAttribute("entity", entity);
%>
<tr id="${row.id}"
	name="${row.name}" style="${row.cssStyle}" 
	class="${row.cssClass}" onclick="${row.onclick}">
	<c:forEach items="${row.fields}" var="field">
		<gridRender:field entity="${entity}" field="${field }"></gridRender:field>
	</c:forEach>
</tr>
</c:forEach>

