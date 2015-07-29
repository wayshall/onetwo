<#import "helper.ftl" as helper>

<#assign dataFormName="dataForm"/>
<#assign datagridName="dataGrid"/>

<#assign modulePath="/${_globalConfig.getModuleName()}/${_tableContext.propertyName}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix?replace('_', '-')}"/>

<${'@'}extends parent="application.html">
    
    <${'@'}override name="charset">
        <meta charset="UTF-8">
    </${'@'}override>
    
    <${'@'}override name="title">
       ${(table.comments[0])!''}
    </${'@'}override>
    <${'@'}override name="main-content">
       
  <div class="easyui-panel" style="padding: 5px;" data-options="fit:true">
    
     <div class="easyui-panel" style="height:15%">
        <form id="searchForm" class="easyui-form" >
           <table style="padding: 5px;" cellpadding="5px">
                <@helper.generatedFormField table=table isSearchFormField=true fieldClass="searchFieldClass"/>
                <tr>
                   <td>&nbsp;</td>
                    <td rowspan="5">
                        <a id="btnSearch" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索&nbsp;</a>
                    </td>
               </tr>
           </table>
       </form>
    </div>
     
    <div class="easyui-panel" style="height:85%" data-options="fit:true"> 
       <table id="${datagridName}"
              title="${(table.comments[0])!''}" >
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
    </div>
    
  </div>
        
	<${'#'}include "${pagePath}-edit-form.html">
       
                 
    <script type="text/javascript">
    
    $('#btnSearch').bind('click', function(){
        var param = $('#searchForm').serialize();
        var url = '${'$'}{siteConfig.baseURL}${modulePath}.json?'+param;
        $('#dataGrid').datagrid('options').url = url;
        $('#dataGrid').datagrid('reload');
    });
    
    var reqUrl = '${'$'}{siteConfig.baseURL}${modulePath}.json';
    var editingId;
    var toolbar = [
        {
            text:'新增',
            iconCls:'icon-add',
            handler:function(){
                $('#addDataDialog').dialog('open').dialog('setTitle', '新增');

                var url = '${'$'}{siteConfig.baseURL}${modulePath}.json';
                $('#${dataFormName}').attr('action', url);
                $('#${dataFormName}').find('input[name="_method"]').val('');
           }
        },

        {
            text:'编辑',
            iconCls:'icon-edit',
            handler:function(){
                if(!$('#${datagridName}').isSelectedOne()){
                    $.messager.alert('警告','请选择一条数据！','warning');
                   return ;
                }
                
                var dataForm = $('#${dataFormName}');
                var selected = $('#${datagridName}').datagrid('getSelected');
                
                $('#addDataDialog').dialog('open').dialog('setTitle', '编辑');
                
                var url = '${'$'}{siteConfig.baseURL}${modulePath}/'+selected.${table.primaryKey.javaName}+'.json';
                dataForm.attr('action', url);
                dataForm.find('input[name="_method"]').val('put');
                dataForm.form('load', url);
           }
        },

        {
            text:'删除',
            iconCls:'icon-remove',
            handler:helper.deleteHandler({
                        datagrid: '#${datagridName}',
                        url: '${'$'}{siteConfig.baseURL}${modulePath}.json',
                        idField: '${table.primaryKey.javaName}',
                        paramIdName: '${table.primaryKey.javaName}s'
                    })
        }
        
    ];
    
    var selectedRow = null;
    $("#${datagridName}").datagrid({
        iconCls: 'icon-ok',
        rownumbers: true,
        fitColumns: true,
        pagination: true,
        singleSelect: false,
        url: reqUrl,
        method: 'get',
        pageSize: 20,
        pageList: [20, 40, 60, 100],
        //idField: '${table.primaryKey.javaName}',
        toolbar: toolbar
    }); 
 </script>
    </${'@'}override>
</${'@'}extends>
