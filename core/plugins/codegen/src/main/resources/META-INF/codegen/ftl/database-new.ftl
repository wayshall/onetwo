
<@extends>
	<@override name="title">
		用户页面
	</@override>
	
	<@override name="main-content">
		<@jentry name="db" excludeFields="id,createTime,lastUpdateTime" type="create" formButtons=":submit|:save-and-new|:back">
			<@jfield name="username" showOrder="2"/>
		</@jentry>
	</@override> 
</@extends>