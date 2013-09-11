<@extends>
	<@override name="main-content">
		
		<#--
			
			<@grid name="page" 
				title="database列表"
				cssClass="tableStyle" 
				cssStyle="width:760px" 
				action=":qstr">
			<@field name="ids" label="全选" type="checkbox" value="id" cssStyle="width:60px;text-align:center;">
			</@field>
			
			<@field name="label" label="数据库名称" cssStyle="width:60px;text-align:center;" />
			<@field name="username" label="数据库用户" cssStyle="width:60px;text-align:center;" />
			<@field name="jdbcUrl" label="链接串" cssStyle="text-align:center;" />
			<@field name="operation" label="操作" cssStyle="width:160px;text-align:center;" autoRender="false">
				<a href="${pluginConfig.baseURL}/database/${__entity__.id}/tables">查看数据库表</a>
			</@field>
		</@grid>
		
		-->
		
	
		
	<@widget.dataGrid name="database" dataSource=page title="database列表" >
		<@widget.dataRow name="entity" type="iterator" renderHeader=true>
			<@widget.dataField name="ids" label="全选" render="checkbox" value="id"/>
			<@widget.dataField name="label" label="数据库名称"/>
			<@widget.dataField name="username" label="数据库用户"  />
			<@widget.dataField name="jdbcUrl" label="链接串"  />
			<@widget.dataField name="operation" label="操作" render="html">
				<a href="${pluginConfig.baseURL}/database/${entity.id}/tables">查看数据库表</a>
			</@widget.dataField>
		</@widget.dataRow>
	</@widget.dataGrid>
	
	</@override>
</@extends>