<@extends>
	<@override name="title">
			任务队列
	</@override>
	
	<@override name="main-content">
	
	
	<@layout.override name="grid_toolbar">
	    <@security.hasPermission  code="plugin:TaskModule$Queue$New">
			<li>
			<a href="${pluginConfig.baseURL}/taskqueue/email/new"> 新 建  </a>
			</li>
		</@security.hasPermission >
	</@layout.override>
			
	<@widget.dataGrid name="taskququeGrid" dataSource=page title="任务队列" toolbar=true>
	    <@widget.dataRow name="entity" type="iterator" renderHeader=true>
			<@widget.dataField  name="id" label="序号" />
			<@widget.dataField  name="name" label="名称" />
			<@widget.dataField  name="currentTimes" label="已执行次数" />
			<@widget.dataField  name="tryTimes" label="最大尝试执行次数" />
			<@widget.dataField  name="status.name" label="状态" />
			<@widget.dataField  name="taskType" label="任务类型" />
			<@widget.dataField  name="operation" render="html" label="操作">
				<@security.hasPermission code="plugin:TaskModule$Queue$ExeLog">
				<a href="${pluginConfig.baseURL}/taskqueue/${(entity.id)!''}/log" >执行明细</a>
				</@security.hasPermission>
				
				<@security.hasPermission code="plugin:TaskModule$Queue$Cancel">
				<#if entity.cacelable>
				<a href="${pluginConfig.baseURL}/taskqueue/${(entity.id)!''}/cancel" data-confirm="确定要取消任务？" data-method="put" >取消任务</a>
				</#if>
				</@security.hasPermission>
			</@widget.dataField>
		</@widget.dataRow>	
	</@widget.dataGrid >
		
	</@override> 
</@extends>