
<@extends>

	<@var name="activeLink" value="详细账本管理"/>
	
	
	<@override name="main-content">
		
		<@grid name="tables" 
				action=":qstr"
				pagination="None"
				toolbar="${pluginConfig.templateBasePath}/btn/btn-codegen-config"
				theme=":fmtag:/lib/dg_bstrp"
				title="程序数据库表"
				excludeFields="id,createTime,lastUpdateTime"
				template=":fmtag:lib/dg_bstrp/datagrid.ftl">
			
			<@h.hidden name="dbid" value=id/>
			<#--	
			<@jentry name="codegen" label="" type="create" formButtons="" form=false>
				<@jfield name="包名："/>
				<@jfield name="要去掉的前缀："/>
			</@jentry>
			-->
			
			<@field name="tables" label="全选" type="checkbox" showOrder="-2" cssStyle="width:50px;" autoRender="false"/>
			<@field name="表名" autoRender="false"> 
				${__entity__}
			</@field>
		</@grid>
		
	</@override> 
</@extends>