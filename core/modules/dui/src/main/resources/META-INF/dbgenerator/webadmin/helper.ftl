
<#macro makeVueFormField field modelPrefix='dataModel' spaces="">
      ${spaces}<el-form-item
        ${spaces}label="${(field.label)!''}"
        ${spaces}prop="${field.column.javaName}">
      <#if field.select??>
        ${spaces}<dui-select
          ${spaces}v-model="${modelPrefix}.${field.column.javaName}"
          ${spaces}entity="${DUIEntityMeta.name}"
          ${spaces}field="${field.name}"/>
        <#assign hasSelectType=true/>
      <#elseif field.column.mapping.isNumberType()==true>
        ${spaces}<el-input-number
          ${spaces}v-model="${modelPrefix}.${field.column.javaName}"
          ${spaces}:min="1" :max="10"
          ${spaces}label="${(field.label)!''}"
          ${spaces}:disabled="${field.formDisabledValue}"
          <#if field.column.mapping.isSqlFloat()==true> :precision="2"</#if>/>
      <#elseif field.column.mapping.isSqlTimestamp()==true>
        ${spaces}<el-date-picker
          ${spaces}v-model="${modelPrefix}.${field.column.javaName}"
          ${spaces}type="datetime"
         ${spaces} placeholder="选择日期时间">
        ${spaces}</el-date-picker>
      <#elseif field.column.mapping.isSqlTime()==true>
        ${spaces}<el-time-picker
          ${spaces}v-model="${modelPrefix}.${field.column.javaName}"
          ${spaces}placeholder="选择时间>
       ${spaces}</el-time-picker>
      <#elseif field.column.mapping.isSqlDate()==true>
        ${spaces}<el-date-picker
          ${spaces}v-model="${modelPrefix}.${field.column.javaName}"
          ${spaces}type="date"
          ${spaces}placeholder="选择日期">
        ${spaces}</el-date-picker>
      <#elseif field.column.mapping.isBooleanType()==true>
        ${spaces}<el-switch
          ${spaces}v-model="${modelPrefix}.${field.column.javaName}"
          ${spaces}active-color="#13ce66"
          ${spaces}inactive-color="#ff4949">
        ${spaces}</el-switch>
      <#elseif field.input.isFileType()==true>
        ${spaces}<file-input v-model="${modelPrefix}.${field.name}File"/>
        <#assign hasFileType=true/>
      <#elseif field.column.isAssociationType()==true>
        ${spaces}<el-input v-model="${modelPrefix}.${field.column.javaName}" placeholder="请输入${(field.label)!''}"/>
      <#else>
        ${spaces}<el-input
          ${spaces}v-model="${modelPrefix}.${field.column.javaName}"
          ${spaces}type="${field.input.typeName}"
          ${spaces}placeholder="请输入${(field.label)!''}"/>
      </#if>
      ${spaces}</el-form-item>
</#macro>


<#macro generatedFormField table isSearchFormField=false fieldClass='formFieldClass'>
    <#list table.columns as column>
        <#assign requiredValue = 'true'/>
        <#if !column.primaryKey>
            <#if isSearchFormField==true>
                <#assign requiredValue = 'false'/>
            <#else>
                <#assign requiredValue = column.nullable?string('false', 'true')/>
            </#if><#t>
                <tr>
                    <td>${(column.commentName)!''}:</td>
                    <td>
                      <#if column.mapping.isNumberType()==true>
                        <input class="easyui-numberbox ${fieldClass}" type="text" name="${column.javaName}" 
                               data-options="required:${requiredValue}
                                             <#if column.mapping.isSqlFloat()==true>, precision:2</#if> "/>
                               
                      <#elseif column.mapping.isSqlTimestamp()==true>
                        <input class="easyui-datetimebox ${fieldClass}" type="text" name="${column.javaName}" 
                               data-options="required:${requiredValue},
                                             editable:false "/>
        
                      <#elseif column.mapping.isSqlTime()==true>
                        <input class="easyui-timespinner ${fieldClass}" type="text" name="${column.javaName}" 
                               data-options="required:${requiredValue},
                                             editable:false "/>
        
                      <#elseif column.mapping.isSqlDate()==true>
                        <input class="easyui-datebox ${fieldClass}" type="text" name="${column.javaName}" 
                               data-options="required:${requiredValue},
                                             editable:false "/>
                               
                      <#elseif column.mapping.isBooleanType()==true>
                        <input class="easyui-combobox ${fieldClass}" type="text" name="${column.javaName}" 
                               data-options="required:${requiredValue},
                                             editable:false,
                                             data: [{value:'true', text:'是', selected:'true'}, {value:'false', text:'否'}] "/>
                               
                      <#elseif column.isDictType()==true>
                        <input class="easyui-combobox ${fieldClass}" name="${column.javaName}" 
                               data-options="required:${requiredValue},
                                              method: 'get',
                                              editable: false,
                                            <#if isSearchFormField==true><#t>
                                              loadFilter: helper.addEmptyOptionForComboboxFilter,
                                            </#if><#t>
                                              url: '${'$'}{siteConfig.baseURL}/configmgr/dictionary/combobox/${column.commentsInfo['字典类型']}.json' "/>
                               
                      <#elseif column.isFileType()==true>
                        <input class="easyui-filebox ${fieldClass}" id="${column.javaName}" name="${column.javaName}" 
                               data-options="required:${requiredValue},
                                              editable: false,
                                              buttonText:'选择文件' "/>
                               
                      <#elseif column.isAssociationType()==true>
                        <input class="${fieldClass}" name="${column.javaName}" 
                               data-options="required:${requiredValue}"/>
                               
                      <#else>
                        <input class="easyui-textbox ${fieldClass}" type="text" name="${column.javaName}" 
                            <#if column.columnSize gte 500 ><#t/>
                               style="width:185px;height:200px"
                            </#if><#t/>
                               data-options="required:${requiredValue},
                                         <#if column.columnSize gte 500 ><#t/>
                                             multiline:true, 
                                         </#if><#t/>
                                             validType:'length[0,${column.columnSize}]' "/>
                      </#if>
                    </td>
                </tr>
       </#if>
    </#list>
</#macro>