
<@extends>
	<@override name="title">
		用户页面
	</@override>
	
	<@override name="main-content">
		<@jentry name="ctemp" excludeFields="id,createTime,lastUpdateTime" type="create" formButtons=":submit|:save-and-new|:back">
			<@jfield name="content" formTag="textarea" cssStyle="width:600px;height:800px;"/>
		</@jentry>
	</@override> 
</@extends>