

<@extends>
	<@override name="title">
			字典数据管理
	</@override>
	
	
 <@override name="main-content">
	
	<@layout.override name="grid_toolbar">
		<li>
			<a href="${pluginConfig.baseURL}/data/dictionary/new?typeId=${type.id}"> 新 建  </a>
			</li>
			<li>
			<a data-method="delete" class="dg-toolbar-button-delete" data-confirm="确定要批量删除这些数据？" href="${pluginConfig.baseURL}/data/dictionary"> 批量删除  </a>
		</li>
	</@layout.override>
	
	<@widget.dataGrid name="diactionaryGrid" dataSource=page title="${type.name}数据列表" toolbar=true>
		<@widget.dataRow name="entity" type="iterator" renderHeader=true>
			<@widget.dataField name="ids" label="全选" render="checkbox" value="id"/>
			<@widget.dataField name="code" label="代码"/>
			<@widget.dataField name="name" label="名称"/>
			<@widget.dataField name="value" label="值"/>
			<@widget.dataField name="valid" label="是否有效"/>
			<@widget.dataField name="sort" label="排序"/>
			<@widget.dataField name="remark" label="备注"/>
			<@widget.dataField name="operation" label="操作" render="html">
				<a href="${pluginConfig.baseURL}/data/dictionary/${entity.id}/edit">编辑</a>
			</@widget.dataField>
		</@widget.dataRow>
	</@widget.dataGrid>
	

	</@override> 
</@extends>