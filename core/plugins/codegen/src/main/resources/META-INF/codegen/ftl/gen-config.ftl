
<@extends>

	
	<@override name="main-content">
		
		<@form.form modelAttribute="" action="${pluginConfig.baseURL}/gencode" method="post">
			<input name="dbid" value="${dbid}" type="hidden" />
			
			<table class="table table-bordered table-striped">
				<thead>
					<tr><td colspan="2">配置</td></tr>
				</thead>
				<tbody>
				<tr>
					<td>
						模块名称：
					</td>
					<td>
						<input name="modelName" type="text" value=""/>
					</td>
				</tr>
				
				<tr>
					<td>
						要去掉的前缀：
					</td>
					<td>
						<input name="tablePrefix" type="text" value=""/>
					</td>
				</tr>
				
				</tbody>
			</table>
			
			
			<table class="table table-bordered table-striped">
				<thead>
					<tr><td>要生成代码的表</td></tr>
				</thead>
				<tbody>
			<#list tables as t>
				<tr>
					<td>
				<@h.hidden name="tables" value=t/>
				${t}&nbsp;
					</td>
				</tr>
			</#list>
				</tbody>
			</table>
			
			
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<td colspan="5">选择代码模板：</td>
					</tr>
				</thead>
				<thead>
					<tr>
						<td>选择</td>
						<td>名称</td>
						<td>包名</td>
						<td>文件名称后缀</td>
						<td>文件后缀</td>
					</tr>
				</thead>
				<tbody>
				<#list page.result as tp>
					<tr>
						<td><input name="ids" type="checkbox" value="${tp.id}"/></td>
						<td>${tp.name}</td>
						<td>${tp.packageName}</td>
						<td>${tp.fileNamePostfix}</td>
						<td>${tp.filePostfix}</td>
					</tr>
				</#list>
				<tr>
					<td colspan="5">
						<input type="submit" class="btn btn-primary" value="生成代码" />
					</td>
				</tr>
				</tbody>
			</table>
		</@form.form>
		
	</@override> 
</@extends>