<#import "helper.ftl" as helper>

<#assign dataFormName="dataForm"/>
<#assign datagridName="dataGrid"/>

<#assign modulePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>
<meta charset="UTF-8">
<div id="addDataDialog" class="easyui-dialog" 
    style="width:50%;height:80%;padding:10px 20px"
    data-options="closed:true, modal:true, buttons:'#dlg-buttons' ">
           填写[${(table.comments[0])!''}]信息<hr/>
       <form id="${dataFormName}" class="easyui-form" action="${'$'}{siteConfig.baseURL}${modulePath}.json" method="post" >
            <input id="_method" name="_method" type="hidden" />
           <input name="${table.primaryKey.javaName}" type="hidden"/>
           <table cellpadding="5">
                <@helper.generatedFormField table=table isComboboxAddEmptyOption=true/>
           </table>
           <${'@'}security.csrfInput/>
       </form>
</div>

<div id="dlg-buttons">
    <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveData();">保存</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="cancel();">取消</a>
</div>
<script type="text/javascript">
    $('#addDataDialog').dialog({
        width:'50%',
        height:'80%',
        onClose: function(){
            $('#dataForm').form('reset');
        }
    })
    
    function saveData(){
        helper.submitEasyForm({dataForm: '#dataForm',dataDialog: '#addDataDialog', treegrid: '#dataGrid'});
    }
    
    function saveData(){
        helper.submitEasyForm({dataForm: '#${dataFormName}',dataDialog: '#addDataDialog', datagrid: '#${datagridName}'});
    }
    function cancel(){
        $('#addDataDialog').dialog('close');
    }
</script>