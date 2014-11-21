<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	
	
	<layout:override name="title">
		显示 User ${user.id} 
	</layout:override>
	
 	<layout:override name="main-content">
		
<div class="page-header">
<h2>
User ${user.id} 
</h2>
</div>

		<table class="table table-bordered table-striped">
			<tr>
				<td  style="width:160px">id </td>
				<td>
				<c:out value="${user.id}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">userName </td>
				<td>
				<c:out value="${user.userName}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">nickName </td>
				<td>
				<c:out value="${user.nickName}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">email </td>
				<td>
				<c:out value="${user.email}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">mobile </td>
				<td>
				<c:out value="${user.mobile}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">gender </td>
				<td>
				<c:out value="${user.gender}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">status </td>
				<td>
				<c:out value="${user.status}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">birthday </td>
				<td>
				<c:out value="${user.birthday}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">createTime </td>
				<td>
				<c:out value="${user.createTime}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">lastUpdateTime </td>
				<td>
				<c:out value="${user.lastUpdateTime}"/>
				</td>
			</tr>
		<tr>
			<td colspan="2">
			<a href="${siteConfig.baseURL}/admin/user/${user.id}/edit" class="btn btn-primary">编辑</a>
			<a href="${siteConfig.baseURL}/admin/user" class="btn">返回</a>
			</td>
		</tr>
		
		</table>

  </layout:override>
  
</layout:extends>