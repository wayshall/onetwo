
<@extends>	
	
	<@override name="title">
		分配角色
	</@override>
	
 	<@override name="main-content">
<div class="page-header">
<h2>
分配角色
</h2>
</div>
     
     <@widget.form name="user" method="put">
     	<@widget.formField name="userName" disabled="true" label="用户名" />
     	<@widget.formField name="roleIds" label="角色" cssStyle="width:400px" type="checkboxGroup" items=roles itemLabel="name" itemValue="id" value="id" errorTag="roles"/>
		<@widget.formField name="" type="submit" label="提交" cssClass="btn-primary"/>
		<@widget.formField name="" type="button" value="${pluginConfig.baseURL}/app/user?appCode=${user.appCode }" label="返回"/>
     </@widget.form>
  </@override>
  
</@extends>