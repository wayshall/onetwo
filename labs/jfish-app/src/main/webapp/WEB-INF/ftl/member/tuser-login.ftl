
<@extends>
	<@override name="title">
		用户登录页面
	</@override>
	
	<@override name="main-content">
	
	<#if Session.loginUserInfo??>
		欢迎你，${Session.loginUserInfo.userName}
	<#else>
		<@h.form action="${siteConfig.baseURL}/member/tuser/login" method="post">
		<table>
		<tr>
			<td>
			用户名：<input name="userName" value="" type="text" />
			</td>
		</tr>
		<tr>
			<td>
			密码：<input name="password" value="" type="password" />
			</td>
		</tr>
		<tr>
			<td>
			<input name="submit" type="submit" value="submit"/>
			</td>
		</tr>
		</table>
		</@h.form>
	</#if>
	
	</@override> 
</@extends>