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
				<td  style="width:160px">email </td>
				<td>
				<c:out value="${user.email}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">status </td>
				<td>
				<c:out value="${user.status}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">age </td>
				<td>
				<c:out value="${user.age}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">birthDay </td>
				<td>
				<c:out value="${user.birthDay}"/>
				</td>
			</tr>
			<tr>
				<td  style="width:160px">height </td>
				<td>
				<c:out value="${user.height}"/>
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
				<td  style="width:160px">password </td>
				<td>
				<c:out value="${user.password}"/>
				</td>
			</tr>
		<tr>
			<td colspan="2">
			<a href="${siteConfig.baseURL}/member/user/${user.id}/edit" class="btn btn-primary">编辑</a>
			<a href="${siteConfig.baseURL}/member/user" class="btn">返回</a>
			</td>
		</tr>
		
		</table>

  </layout:override>
  
</layout:extends>