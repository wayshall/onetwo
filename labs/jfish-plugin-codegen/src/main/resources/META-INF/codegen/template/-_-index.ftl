<#assign webPath="${moduleRequestPath}/${commonName?uncap_first}"/>
<${"@"}extends>
	<${"@"}override name="title">
		${commonName}列表
	</${"@"}override>
	
	<${"@"}override name="main-content">
		<${"@"}override name="grid_toolbar">
			<li>
			<a href="${'$'}{siteConfig.baseURL}${webPath}/new"> 新 建  </a>
			</li>
			<li>
			<a data-method="delete" class="dg-toolbar-button-delete" data-confirm="确定要批量删除这些数据？" href="${'$'}{siteConfig.baseURL}${webPath}"> 批量删除  </a>
			</li>
		</${"@"}override>
		
		<${"@"}grid name="page" 
				title="${commonName}列表"
				cssClass="tableStyle" 
				cssStyle="width:760px" 
				action=":qstr">
			<${"@"}field name="ids" label="全选" type="checkbox" value="id" cssStyle="width:60px;text-align:center;">
			</${"@"}field>
			
		<#list table.columnCollection as column>
			<${"@"}field name="${column.javaName}" label="${column.javaName}" cssStyle="width:60px;text-align:center;" orderBy="true"/>
		</#list>
			
			<${"@"}field name="operation" label="操作" cssStyle="text-align:center;" autoRender="false">
				<a href="${'$'}{siteConfig.baseURL}${webPath}/${'$'}{__entity__.id}/edit">编辑</a>
			</${"@"}field>
		</${"@"}grid>
		
	</${"@"}override> 
</${"@"}extends>