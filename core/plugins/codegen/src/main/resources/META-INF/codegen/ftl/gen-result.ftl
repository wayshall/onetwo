
<@extends>

	
	<@override name="main-content">

		<h3>生成文件如下：</h3>
		<div class="alert alert-success">
			<#list genMessages as msg>
				${msg}<br/>
			</#list>
		</div>
		
	</@override> 
</@extends>