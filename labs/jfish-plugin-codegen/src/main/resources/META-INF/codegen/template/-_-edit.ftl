<#assign webPath="${moduleRequestPath}/${commonName?uncap_first}"/>
<${"@"}extends>
	<${"@"}override name="title">
		${commonName} ${"$"}{${commonName?uncap_first}.id} 编辑
	</${"@"}override>
	
	<${"@"}override name="main-content">
		
<div class="page-header">
<h2>
${commonName} ${"$"}{${commonName?uncap_first}.id} 编辑
</h2>
</div>

	<${"@"}form.form modelAttribute="user" action="${"$"}{siteConfig.baseURL}${moduleRequestPath}/${commonName?uncap_first}/${"$"}{${commonName?uncap_first}.id}" method="put">
		<table class="table table-bordered table-striped">
		
		<#list table.columnCollection as column>
			<tr>
				<td>${column.javaName} </td>
				<td>
				<${"@"}form.input path="${column.javaName}"/>
				<${"@"}form.errors path="${column.javaName}" cssClass="error"/>
				</td>
			</tr>
		</#list>
		
		<tr>
			<td colspan="2">
			<input name="" type="submit" value="更新" class="btn btn-primary"/>
			<a href="${"$"}{siteConfig.baseURL}${webPath}" class="btn">返回</a>
			</td>
		</tr>
		</table>
	</${"@"}form.form>
	</${"@"}override> 
</${"@"}extends>