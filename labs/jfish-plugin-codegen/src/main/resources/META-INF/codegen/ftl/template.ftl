<@extends>
	<@override name="main-content">
		
			<@egrid name="page" 
					action=":qstr"
					toolbar="new:delete"
					theme=":fmtag:/lib/dg_bstrp"
					excludeFields="userId"
					excludeFields="content,id,createTime,lastUpdateTime"
					template=":fmtag:lib/dg_bstrp/datagrid.ftl">
				<@field name="ids" label="全选" type="checkbox" value="id" cssStyle="width:50px;text-align:center;" showOrder="-2"/>
				<@field name="operation" label="操作" cssStyle="width:100px;" autoRender="false" showOrder="-1">
					<@h.edit_link entity=__entity__ />
					<@h.show_link entity=__entity__ />
				</@field>
			</@egrid>
		</div>
	</@override>
</@extends>