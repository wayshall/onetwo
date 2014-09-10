<@extends>
	<@override name="title">
			任务队列
	</@override>
	
	<@override name="main-content">
	
	
	<@widget.dataGrid name="taskququeGrid" dataSource=page title="执行明细" >
	    <@widget.dataRow name="entity" type="iterator" renderHeader=true>
			<@widget.dataField  name="execIndex" label="执行序号" />
			<@widget.dataField  name="startTime" label="开始执行时间" />
			<@widget.dataField  name="endTime" label="执行结束时间" />
			<@widget.dataField  name="taskInput" label="输入" cssStyle="width:300px;" render="html">
				<div style="heigh:10px;word-break: break-all;overflow:auto;">${(entity.taskInput?html)!''}</div>
			</@widget.dataField>	
			<@widget.dataField  name="taskOutput" label="输出" cssStyle="width:300px;" render="html">
				<div style="word-break: break-all;overflow:auto;">${(entity.taskOutput?html)!''}</div>
			</@widget.dataField>	
		</@widget.dataRow>	
	</@widget.dataGrid >
		
	</@override> 
</@extends>