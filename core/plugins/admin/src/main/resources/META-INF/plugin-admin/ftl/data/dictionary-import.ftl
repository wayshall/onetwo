

<@extends>
	<@override name="title">
			字典数据管理
	</@override>
	
	
	<@override name="main-content">
	数据文件：${dictResource}
		<form name="import" method="post">
			<@widget.formToken/>
			<a submitting="请等待……" href="${pluginConfig.baseURL}/data/dictionary/import" data-method="post">导入字段数据</a>
		</form>
	</@override> 
</@extends>