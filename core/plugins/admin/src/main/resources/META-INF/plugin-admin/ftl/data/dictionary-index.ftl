

<@extends>
	<@override name="title">
			字典类型管理
	</@override>
	
	<@override name="main-content">
	
 <layout:override name="main-content">
	
	<layout:override name="grid_toolbar">
		<li>
			<a href="${pluginConfig.baseURL}/data/dictionary/new"> 新 建  </a>
			</li>
			<li>
			<a data-method="delete" class="dg-toolbar-button-delete" data-confirm="确定要批量删除这些数据？" href="${pluginConfig.baseURL}/data/dictionary"> 批量删除  </a>
		</li>
	</layout:override>
	
	<@widget.dataGrid name="diactionaryGrid" dataSource=page title="Dictionary列表" toolbar="true">
		<@widget.dataRow name="entity" type="iterator" renderHeader="true">
			<@widget.dataField name="ids" label="全选" render="checkbox" value="id"/>
			<@widget.dataField name="code" label="code"/>
			<@widget.dataField name="name" label="name"/>
			<@widget.dataField name="isValid" label="isValid"/>
			<@widget.dataField name="sort" label="sort"/>
			<@widget.dataField name="remark" label="remark"/>
			<@widget.dataField name="operation" label="操作" render="html">
				<a href="${pluginConfig.baseURL}/data/dictionary/${entity.id}/edit">编辑</a>
			</@widget.dataField>
		</@widget.dataRow>
	</@widget.dataGrid>
	

	</@override> 
</@extends>