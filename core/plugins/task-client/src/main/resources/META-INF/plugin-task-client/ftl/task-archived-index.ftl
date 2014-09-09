<@extends>
	<@override name="title">
			任务队列
	</@override>
	
	<@override name="main-content">
	
	
	<@widget.dataGrid name="taskArchivedGrid" dataSource=page title="任务队列" toolbar=true>
	    <@widget.dataRow name="entity" type="iterator" renderHeader=true>
			<@widget.dataField  name="id" label="序号" />
			<@widget.dataField  name="name" label="名称" />
			<@widget.dataField  name="currentTimes" label="已执行次数" />
			<@widget.dataField  name="tryTimes" label="最大尝试执行次数" />
			<@widget.dataField  name="result.name" label="结果" />
			<@widget.dataField  name="task.taskType" label="任务类型" />
			<@widget.dataField  name="operation" render="html" label="操作" permission="class:org.onetwo.plugins.task.client.TaskModule$Queue$ExeLog">
				<a href="${pluginConfig.baseURL}/taskqueue/${entity.id}/log" class="btn" data-toggle="modal" >执行明细</a>
			</@widget.dataField>
		</@widget.dataRow>	
	</@widget.dataGrid >
		
	</@override> 
</@extends>