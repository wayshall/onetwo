<@extends>
	<@override name="main-content">
		
		
			
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
		
	</@override>
</@extends>