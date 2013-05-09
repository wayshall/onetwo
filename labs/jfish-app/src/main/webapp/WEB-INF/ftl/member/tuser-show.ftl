
<@extends>
	<@override name="title">
		用户页面
	</@override>
	
	<@override name="main-content">
		<table>
		<tr>
			<td>
			用户名：${user.userName?default("")}
			</td>
		</tr>
		<tr>
			<td>
			密码：${user.password?default("")} 
			</td>
		</tr>
		<tr>
			<td>
			电子邮件：${user.email?default("")}
			</td>
		</tr>
		<tr>
			<td>
			生日：${user.birthDay?default("")}
			</td>
		</tr>
		</table>
	</@override> 
</@extends>