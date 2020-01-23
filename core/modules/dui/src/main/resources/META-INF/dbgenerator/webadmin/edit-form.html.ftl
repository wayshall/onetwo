<#import "helper.ftl" as helper>

<#assign dataFormName="dataForm"/>
<#assign dataGridName="dataGrid"/>
<#assign addDataDialogName="addDataDialog"/>

<#assign modulePath="${_globalConfig.requestModulePath}/${_tableContext.propertyName}"/>
<meta charset="UTF-8">
<div id="${addDataDialogName}" class="easyui-dialog" 
    style="width:50%;height:80%;padding:10px 20px"
    data-options="closed:true, modal:true, buttons:'#dlg-buttons' ">
           填写[${(table.comments[0])!''}]信息<hr/>
       <form id="${dataFormName}" class="easyui-form" action="${'$'}{siteConfig.baseURL}${modulePath}.json" method="post" >
            <input id="_method" name="_method" type="hidden" />
           <table cellpadding="5">
                <@helper.generatedFormField table=table/>
           </table>
           <${'@'}security.csrfInput/>
       </form>
</div>

<div id="dlg-buttons">
    <a href="#" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveData();">保存</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="cancel();">关闭</a>
</div>
<script type="text/javascript">
    $('#${addDataDialogName}').dialog({
        width:'50%',
        height:'80%',
        onClose: function(){
            $('#${dataFormName}').form('reset');
        }
    })
    
    
    function saveData(){
        helper.submitEasyForm({dataForm: '#${dataFormName}',dataDialog: '#${addDataDialogName}', datagrid: '#${dataGridName}'});
    }
    function cancel(){
        $('#${addDataDialogName}').dialog('close');
    }
    
    <#list table.getAssociationTypeColumns() as column>
    $('#${column.javaName}').combogrid({
        panelWidth:300,
        method: 'get',
        url: '${'$'}{siteConfig.baseURL}/${column.javaName}.json?pagination=false',
        idField:'${(column.commentsInfo['idField'])!'id'}',
        textField:'${(column.commentsInfo['textField'])!'name'}',
        mode:'remote',
        fitColumns:true,
        columns:[[
            {field:'${(column.commentsInfo['textField'])!'name'}',title:'${(column.commentName)!''}',align:'left',width:60}
        ]]
    });
    </#list>
</script>