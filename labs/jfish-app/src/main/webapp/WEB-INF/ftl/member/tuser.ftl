
<@extends>
	<@override name="title">
		用户管理页面
	</@override>
	
	<@override name="main-content">
		<@grid name="page" 
				cssClass="tableStyle" 
				cssStyle="width:760px" 
				action=":qstr"
				toolbar="delete:new">
			<@field name="ids" label="全选" type="checkbox" value="id" cssStyle="width:60px;text-align:center;" render="html">
				<#if __entity__.id % 2==0>
					<input type='checkbox'/>
				<#else>
					aa
				</#if>
			</@field>
			<@field name="id" label="主键" cssStyle="width:60px;text-align:center;" orderBy="true"/>
			<@field name="userName" label="用户名" cssStyle="width:80px;text-align:center;" orderBy="true" search="true" autoRender="false">
				<@h.show_link entity=__entity__ showname="userName"/> 
			</@field>
			<@field name="email" label="邮箱地址" cssStyle="width:80px;text-align:center;" search="true"/>
			<@field name="birthDay" label="生日" cssStyle="width:120px;text-align:center;" search="true"/>
			<@field name="createTime" label="创建时间" cssStyle="width:120px;text-align:center;"/>
			<@field name="lastUpdateTime" label="最后更新时间" cssStyle="width:120px;text-align:center;"/>
			<@field name="operation" label="操作" cssStyle="text-align:center;" autoRender="false">
				<@h.edit_link entity=__entity__ />
				<@h.delete_link entity=__entity__ />
			</@field>
		</@grid>
		
	</@override> 
</@extends>