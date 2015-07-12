<#assign dataFormName="dataForm"/>
<#assign datagridName="dataGrid"/>

<#assign modulePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>
<meta charset="UTF-8">
<div id="addDataDialog" class="easyui-dialog" 
    style="width:50%;height:80%;padding:10px 20px"
    closed="true" buttons="#dlg-buttons">
           填写[${(table.comments[0])!''}]信息<hr/>
       <form id="${dataFormName}" class="easyui-form" action="${'$'}{siteConfig.baseURL}${modulePath}.json" method="post" >
           <input name="${table.primaryKey.javaName}" type="hidden"/>
           <table cellpadding="5">
            <#list table.columns as column>
                <#if !column.primaryKey>
                <tr>
                   <td>${(column.comments[0])!''}:</td>
                    <td>
                      <#if column.mapping.isNumberType()==true>
                        <input class="easyui-numberbox formFieldClass" type="text" name="${column.javaName}" 
                               data-options="required:${column.nullable?string('false', 'true')}"/>
                               
                      <#elseif column.mapping.isTimestampType()==true>
                        <input class="easyui-datetimebox formFieldClass" type="text" name="${column.javaName}" 
                               data-options="required:${column.nullable?string('false', 'true')}"/>

                      <#elseif column.mapping.isDateType()==true>
                        <input class="easyui-datebox" type="text" name="${column.javaName}" 
                               data-options="required:${column.nullable?string('false', 'true')}"/>
                               
                      <#elseif column.mapping.isBooleanType()==true>
                        <input class="easyui-combobox formFieldClass" type="text" name="${column.javaName}" 
                               data-options="required:${column.nullable?string('false', 'true')},
                                             editable:false,
                                             data: [{value:'true', text:'是', selected:'true'}, {value:'false', text:'否'}]
                                            "/>
                               
                      <#elseif column.commentsInfo['字典类型']??>
                        <input class="easyui-combobox formFieldClass" name="${column.javaName}" 
                               data-options="required:${column.nullable?string('false', 'true')},
                                              method: 'get',
                                              url: '${'$'}{siteConfig.baseURL}/configmgr/dictionary/combobox.json?parentCode=${column.commentsInfo['字典类型']}'
                                            "/>
                               
                      <#else>
                        <input class="easyui-textbox formFieldClass" type="text" name="${column.javaName}" 
                               data-options="required:${column.nullable?string('false', 'true')},
                                             validType:'length[0,${column.columnSize}]'
                                            "/>
                      </#if>
                    </td>
               </tr>
               </#if>
            </#list>
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