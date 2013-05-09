
<@extends>
	<@override name="title">
		用户页面
	</@override>
	
	<@override name="main-content">
	
		<@jentry name="ctemp" excludeFields="id,userId,createTime,lastUpdateTime"  type="update">
			<@jfield name="content" formTag="textarea" cssStyle="width:800px;height:800px;"/>
		</@jentry>
		
	</@override> 
</@extends>