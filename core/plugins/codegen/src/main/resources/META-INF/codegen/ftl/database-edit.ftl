
<@extends>
	<@override name="title">
		用户页面
	</@override>
	
	<@override name="main-content">
	
		<@jentry name="db" excludeFields="id,userId,createTime,lastUpdateTime"  type="update">
			<@jfield name="username" showOrder="4"/> 
		</@jentry>
		
	</@override> 
</@extends>