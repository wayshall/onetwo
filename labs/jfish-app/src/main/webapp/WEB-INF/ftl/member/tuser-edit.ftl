
<@extends>
	<@override name="title">
		用户页面
	</@override>
	
	<@override name="main-content">
	<@form.form modelAttribute="user" action="${siteConfig.baseURL}/member/tuser/${user.id}" method="put">
		<table>
		<tr>
			<td>
			<@form.input path="userName"/>
			<@form.errors path="userName" cssClass="error"/>
			</td>
		</tr>
		<tr>
			<td>
			<@form.input path="password"/>
			<@form.errors path="password" cssClass="error"/>
			</td>
		</tr>
		<tr>
			<td>
			<@form.input path="email"/>
			<@form.errors path="email" cssClass="error"/>
			</td>
		</tr>
		<tr>
			<td>
			<input name="submit" type="submit" value="submit"/>
			</td>
		</tr>
		</table>
	</@form.form>
	</@override> 
</@extends>