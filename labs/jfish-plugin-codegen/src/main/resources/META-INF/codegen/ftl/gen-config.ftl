
<@extends>

	
	<@override name="main-content">
		
		<@egrid name="tp" 
				action=":qstr"
				pagination="None"
				toolbar="${pluginConfig.templateBasePath}/btn/btn-codegen"
				theme=":fmtag:/lib/dg_bstrp"
				title="代码生成配置"
				excludeFields="content,id,createTime,lastUpdateTime"
				template=":fmtag:lib/dg_bstrp/datagrid.ftl">
			
			<@h.hidden name="dbid" value=dbid/>
			<#list tables as t>
				<@h.hidden name="tables" value=t/>
				${t}&nbsp;
			</#list>
			
			<@jentry name="codegen" label="" type="create" formButtons="" form=false>
				<@jfield label="包名" name="basePackage"/>
				<@jfield label="输出目录" name="generateOutDir"/>
				<@jfield label="要去掉的前缀" name="tablePrefix"/>
			</@jentry>
			
			<@field name="ids" label="全选" type="checkbox" showOrder="-2" cssStyle="width:50px;" value="id"/>
		</@egrid>
		
	</@override> 
</@extends>