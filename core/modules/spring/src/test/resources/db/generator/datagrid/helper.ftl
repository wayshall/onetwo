
<#macro generatedFormField table>
    <#list table.columns as column>
        <#if !column.primaryKey>
        <tr>
           <td>${(column.comments[0])!''}:</td>
            <td>
              <#if column.mapping.isNumberType()==true>
                <input class="easyui-numberbox formFieldClass" type="text" name="${column.javaName}" 
                       data-options="required:${column.nullable?string('false', 'true')}
                                     <#if column.mapping.isSqlFloat()==true>, precision:2</#if> "/>
                       
              <#elseif column.mapping.isSqlTimestamp()==true>
                <input class="easyui-datetimebox formFieldClass" type="text" name="${column.javaName}" 
                       data-options="required:${column.nullable?string('false', 'true')},
                                     editable:false "/>

              <#elseif column.mapping.isSqlTime()==true>
                <input class="easyui-timespinner formFieldClass" type="text" name="${column.javaName}" 
                       data-options="required:${column.nullable?string('false', 'true')},
                                     editable:false "/>

              <#elseif column.mapping.isSqlDate()==true>
                <input class="easyui-datebox formFieldClass" type="text" name="${column.javaName}" 
                       data-options="required:${column.nullable?string('false', 'true')},
                                     editable:false "/>
                       
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
                                      url: '${'$'}{siteConfig.baseURL}/configmgr/dictionary/combobox/${column.commentsInfo['字典类型']}.json'
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
</#macro>