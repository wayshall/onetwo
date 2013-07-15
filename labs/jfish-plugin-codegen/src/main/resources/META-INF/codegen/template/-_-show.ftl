<#assign webPath="${moduleRequestPath}/${commonName?uncap_first}"/>
<${"@"}extends>
	<${"@"}override name="title">
${commonName} ${"$"}{${commonName?uncap_first}.id}
	</${"@"}override>
	
	<${"@"}override name="main-content">
		
<div class="page-header">
<h2>
${commonName} ${"$"}{${commonName?uncap_first}.id} 
</h2>
</div>

		<table class="table table-bordered table-striped">
		<#list table.columnCollection as column>
			<tr>
				<td  style="width:160px">${column.javaName} </td>
				<td>
				${"$"}{${commonName?uncap_first}.userName?html}
				</td>
			</tr>
		</#list>
		<tr>
			<td colspan="2">
			<a href="${"$"}{siteConfig.baseURL}${webPath}/${"$"}{${commonName?uncap_first}.id}/edit" class="btn btn-primary">编辑</a>
			<a href="${"$"}{siteConfig.baseURL}${webPath}" class="btn">返回</a>
			</td>
		</tr>
		
		</table>
	</${"@"}override> 
</${"@"}extends>