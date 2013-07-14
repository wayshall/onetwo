<#assign webPath="${moduleRequestPath}/${commonName?uncap_first}"/>
<${"@"}extends>
	<${"@"}override name="title">
		新建 ${commonName}
	</${"@"}override>
	
	<${"@"}override name="main-content">
	<${"@"}form.form modelAttribute="user" action="${"$"}{siteConfig.baseURL}${webPath}" method="post">
		
<div class="page-header">
<h2>
新建 ${commonName}
</h2>
</div>
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
			<input name="" type="submit" value="保存" class="btn btn-primary"/>
			<a href="${"$"}{siteConfig.baseURL}${webPath}" class="btn">返回</a>
			</td>
		</tr>
		</table>
	</${"@"}form.form>
	</${"@"}override> 
</${"@"}extends>