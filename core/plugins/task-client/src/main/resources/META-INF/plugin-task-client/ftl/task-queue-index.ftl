<@extends>
	<@override name="title">
			任务队列
	</@override>
	
	<@override name="main-content">
		
		 <@widget.dataGrid name="usergrid" dataSource=page title="套餐列表" toolbar=true>
	        <@widget.dataRow name="entity" type="iterator" renderHeader=true>
			<@widget.dataField name="ids" label="全选"  render="checkbox" value="id" />
			 <@widget.dataField  name="name" label="名称" />
            <@widget.dataField  name="price" label="优惠价格(元)" />
		    <@widget.dataField  name="prePrice" label="原价格(元)" />
		   	<@widget.dataField name="startDate" label="开始日期" dataFormat="yyyy-MM-dd" />
		    <@widget.dataField name="endDate" label="结束日期" dataFormat="yyyy-MM-dd" />
			<@widget.dataField  name="remark" label="备注"/>
			<@widget.dataField  name="operation" label="操作" render="html">
			<@security.hasPermission code="Menu_Travel_Pack_Edit">
			 <#if entity!=null>
		    <a href="${siteConfig.baseURL}/travel/pack/${entity.id}/edit">编辑</a>
		    </#if>
		     </@security.hasPermission >
			</@widget.dataField>
		</@widget.dataRow>	
	</@widget.dataGrid >
		
	</@override> 
</@extends>