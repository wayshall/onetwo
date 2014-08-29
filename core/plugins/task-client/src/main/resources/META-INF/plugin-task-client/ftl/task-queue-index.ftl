<@extends>
	<@override name="title">
			任务队列
	</@override>
	
	<@override name="main-content">
	
	
	<@layout.override name="grid_toolbar">
	    <@security.hasPermission  code="class:org.onetwo.plugins.task.client.TaskModule$Queue$New">
			<li>
			<a href="${pluginConfig.baseURL}/taskqueue/email/new"> 新 建  </a>
			</li>
		</@security.hasPermission >
	</@layout.override>
			
	<@widget.dataGrid name="taskququeGrid" dataSource=page title="任务队列" toolbar=true>
	    <@widget.dataRow name="entity" type="iterator" renderHeader=true>
			<@widget.dataField  name="id" label="序号" />
			<@widget.dataField  name="name" label="名称" />
			<@widget.dataField  name="status.name" label="状态" />
			<@widget.dataField  name="taskType" label="任务类型" />
		</@widget.dataRow>	
	</@widget.dataGrid >
		
	</@override> 
</@extends>