<#assign dataFormName="dataForm"/>
<#assign datagridName="dataGrid"/>

<#assign modulePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<${'@'}extends parent="application.html">
    
    <${'@'}override name="charset">
        <meta charset="UTF-8">
    </${'@'}override>
    
    <${'@'}override name="title">
       ${(table.comments[0])!''}
    </${'@'}override>
    <${'@'}override name="main-content">
       
       <table id="${datagridName}"
              title="${(table.comments[0])!''}" 
              style="height:100%;width:100%;">
        <thead>
            <tr>
                <th field="${table.primaryKey.javaName}" checkbox="true">${(table.primaryKey.comments[0])!''}</th>
            <#list table.columns as column>
                <#if !column.primaryKey>
                <th field="${column.javaName}" >${(column.comments[0])!''}</th>
                </#if>
            </#list>
            </tr>
        </thead>
      </table>
        
	<${'#'}include "${modulePath}-edit-form.html">
       
                 
    <script type="text/javascript">
    
    var editingId;
    var toolbar = [
        {
            text:'新增',
            iconCls:'icon-add',
            handler:function(){
                $('#addDataDialog').dialog('open').dialog('setTitle', '新增');


                var selected = $('#${datagridName}').datagrid('getSelected');
                if(selected){
                    $('#parentCode').textbox('setText', selected.code);
                    $('#parentCode').textbox('setValue', selected.code);
                }
                
                var url = '${'$'}{siteConfig.baseURL}${modulePath}.json';
                $('#${dataFormName}').attr('action', url);
                $('#${dataFormName}').find('input[name="_method"]').val('');
           }
        },

        {
            text:'编辑',
            iconCls:'icon-edit',
            handler:function(){
                var selectedNodes = $('#${datagridName}').datagrid('getSelections');
                if(!selectedNodes || selectedNodes.length>1){
                    $.messager.alert('警告','请选择一条数据！','warning');
                   return ;
                }
                
                var dataForm = $('#${dataFormName}');
                var selected = $('#${datagridName}').datagrid('getSelected');
                
                $('#addDataDialog').dialog('open').dialog('setTitle', '编辑');
                $('#code').textbox('disable');
                var url = '${'$'}{siteConfig.baseURL}${modulePath}/'+selected.code+'.json';
                dataForm.attr('action', url);
                dataForm.find('input[name="_method"]').val('put');
                dataForm.form('load', url);
           }
        },

        {
            text:'删除',
            iconCls:'icon-remove',
            handler:function(){
                var selectedNodes = $('#${datagridName}').datagrid('getSelections');
                if(!selectedNodes){
                    $.messager.alert('警告','请先选择数据！','warning');
                   return ;
                }
                var ${table.primaryKey.javaName} = $.map(selectedNodes, function(e){
                    return e.${table.primaryKey.javaName};
                });
                helper.deleteHandler({
                    datagrid: '#${datagridName}',
                    url: '${'$'}{siteConfig.baseURL}${modulePath}.json',
                    params: {'${table.primaryKey.javaName}s': ${table.primaryKey.javaName}}
                });
           }
        }
        
    ];
    
    var selectedRow = null;
    var loadUrl = '${'$'}{siteConfig.baseURL}${modulePath}.json';
    $("#${datagridName}").datagrid({
        iconCls: 'icon-ok',
        rownumbers: true,
        fitColumns: true,
        pagination: true,
        singleSelect: false,
        url: loadUrl,
        method: 'get',
        idField: 'code',
        toolbar: toolbar,
        treeField: 'name',
        onClickRow: function(row){
            if(selectedRow && selectedRow==row){
                $('#${datagridName}').datagrid('unselect', selectedRow.id);
                selectedRow = null;
            }else{
                selectedRow = row;
            }
        }
    }); 
 </script>
    </${'@'}override>
</${'@'}extends>