<meta charset="UTF-8">
<div id="addDataDialog" class="easyui-dialog" 
    style="width:50%;height:80%;padding:10px 20px"
    closed="true" buttons="#dlg-buttons">
           填写[${(table.comments[0])!''}]信息<hr/>
       <form id="loginForm" class="easyui-form" action="${'$'}{siteConfig.baseURL}/dologin?ajaxRequest=true" method="post" >
           <table cellpadding="5">
            <#list table.columns as column>
                <tr>
                   <td>${(column.comments[0])!''}:</td>
                    <td>
                        <input class="easyui-${column.mapping.attrs.cssClass}" type="text" name="${column.javaName}" data-options=""/>
                    </td>
               </tr>
            </#list>
           </table>
           <${'@'}security.csrfInput/>
       </form>
</div>
<div id="dlg-buttons">
    <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()">保存</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#addDataDialog').dialog('close')">取消</a>
</div>