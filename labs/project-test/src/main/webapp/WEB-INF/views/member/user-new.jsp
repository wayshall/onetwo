<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	

	
	<layout:override name="title">
		新建 User
	</layout:override>
	
 	<layout:override name="main-content">
 
	<form:form modelAttribute="user" action="${siteConfig.baseURL}/member/user" method="post">
		
<div class="page-header">
<h2>
新建 User
</h2>
</div>
		<table class="table table-bordered table-striped">
		
			<tr>
				<td>userName </td>
				<td>
				<form:input path="userName"/>
				<form:errors path="userName" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>email </td>
				<td>
				<form:input path="email"/>
				<form:errors path="email" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>status </td>
				<td>
				<form:input path="status"/>
				<form:errors path="status" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>age </td>
				<td>
				<form:input path="age"/>
				<form:errors path="age" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>birthDay </td>
				<td>
				<form:input path="birthDay"/>
				<form:errors path="birthDay" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>height </td>
				<td>
				<form:input path="height"/>
				<form:errors path="height" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>createTime </td>
				<td>
				<form:input path="createTime"/>
				<form:errors path="createTime" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>lastUpdateTime </td>
				<td>
				<form:input path="lastUpdateTime"/>
				<form:errors path="lastUpdateTime" cssClass="error"/>
				</td>
			</tr>
			<tr>
				<td>password </td>
				<td>
				<form:input path="password"/>
				<form:errors path="password" cssClass="error"/>
				</td>
			</tr>
		
		<tr>
			<td colspan="2">
			<input name="" type="submit" value="保存" class="btn btn-primary"/>
			<a href="${siteConfig.baseURL}/member/user" class="btn">返回</a>
			</td>
		</tr>
		</table>
	</form:form>
	
	
  </layout:override>
  
</layout:extends>