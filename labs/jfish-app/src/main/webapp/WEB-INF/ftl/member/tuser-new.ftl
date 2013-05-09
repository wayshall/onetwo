
<@extends>
	<@override name="title">
		用户页面
	</@override>
	
	<@override name="main-content">
	<@s.form path="user" action="${siteConfig.baseURL}/member/tuser" method="post">
		<table>
		<tr>
			<td> 
			用户名：<@s.formInput path="userName"/> 
			</td>
		</tr>
		<tr>
			<td> 
			密码：<@s.formInput path="password"/> 
			</td>
		</tr>
		<tr>
			<td>
			email：<@s.formInput path="email"/> 
			</td>
		</tr>
		<tr>
			<td>
			<input name="submit" type="submit" value="submit"/>
			</td>
		</tr>
		</table>
	</@s.form>
	</@override> 
</@extends>