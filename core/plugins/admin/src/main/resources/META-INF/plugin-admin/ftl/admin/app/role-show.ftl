<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	
	
	<layout:override name="title">
		显示 Role ${role.id} 
	</layout:override>
	
 	<layout:override name="main-content">
		
<div class="page-header">
<h2>
Role ${role.id} 
</h2>
</div>

		<table class="table table-bordered table-striped">
			<tr>
				<td  style="width:160px">id </td>
				<td>
				<c:out value="${role.id}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">name </td>
				<td>
				<c:out value="${role.name}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">areaCode </td>
				<td>
				<c:out value="${role.areaCode}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">remark </td>
				<td>
				<c:out value="${role.remark}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">createTime </td>
				<td>
				<c:out value="${role.createTime}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">lastUpdateTime </td>
				<td>
				<c:out value="${role.lastUpdateTime}"/>
				</td>
			</tr>
		<tr>
			<td colspan="2">
			<a href="${siteConfig.baseURL}/admin/role/${role.id}/edit" class="btn btn-primary">编辑</a>
			<a href="${siteConfig.baseURL}/admin/role" class="btn">返回</a>
			</td>
		</tr>
		
		</table>

  </layout:override>
  
</layout:extends>