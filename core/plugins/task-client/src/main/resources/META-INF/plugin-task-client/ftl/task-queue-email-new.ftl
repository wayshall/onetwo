<@extends>
	<@override name="title">
			新建任务队列
	</@override>
	
	<@override name="main-content">
	
	 	<@widget.form name="taskQueue" action="${pluginConfig.baseURL}/taskqueue/email" method="post" label="新建邮件任务">
	 		<@widget.formField name="planTime" label="任务执行时间" type="date" dataFormat="yyyy-MM-dd HH:mm:ss"/>
	 		<@widget.formField name="name" label="任务名称"/>
	 		<@widget.formField name="bizTag" label="业务标签"/>
	 		<@widget.formField name="subject" label="邮件标题"/>
	 		<@widget.formField name="html" label="是否html邮件" value="htmlChecked" type="radioGroup" items=htmlSelector/>
	 		<@widget.formField name="content" label="邮件内容" type="textarea"/>
			<@widget.formField name="" type="submit" label="提交"/>
			<@widget.formField name="" type="button" value="${pluginConfig.baseURL}/taskqueue" label="返回"/>
	 	</@widget.form>
		
	</@override> 
</@extends>