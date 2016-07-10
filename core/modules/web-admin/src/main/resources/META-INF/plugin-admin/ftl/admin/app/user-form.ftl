
<@widget.formField name="appCode" type="hidden"/>	
<@widget.formField name="userName" label="用户名(登录帐号名)"/>
<@widget.formField name="nickName" label="用户昵称"/>
<@widget.formField name="confirmPassword" label="用户密码" type="html">
	<input name="confirmPassword" type="password"/><#if user.id??>（如果不修改密码，请留空）</#if>
</@widget.formField>
<@widget.formField name="email" label="email" />
<@widget.formField name="mobile" label="手机" />
<@widget.formField name="gender" type="select" label="性别" items=genders itemLabel="label" itemValue="value"/>
<@widget.formField name="status" type="select" label="状态" items=userStatus itemLabel="label" itemValue="value" showable=user.id??/>

<@widget.formField name="birthday" label="生日" type="date" dataFormat="yyyy-MM-dd" />
<@widget.formField name="" type="submit" label="提交"/>
<@widget.formField name="" type="button" value="${pluginConfig.baseURL}/app/user?appCode${user.appCode }" label="返回"/>
		
