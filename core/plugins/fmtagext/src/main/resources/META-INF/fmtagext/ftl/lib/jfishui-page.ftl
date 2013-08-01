
<@extends>
	<@override name="title">
		${__page__.title}
	</@override>
	
	
	<@override name="main-content">
		
		<#list __page__.dataComponents as dc>
			<@jfishui component=dc.component data=dc.data />
		</#list>
		
	</@override>
	
</@extends>