
<#macro makeVueFormField field modelPrefix='dataModel' spaces="" isEditFormField=true>
      ${spaces}<el-form-item
    <#if isEditFormField && field.insertable==false>
        ${spaces}v-if="statusMode!=='Add'"
    </#if>
        ${spaces}label="${(field.label)!''}"
        ${spaces}prop="${field.name}">
    <#if field.select??>
     <#if field.select.treeSelect==true>
        ${spaces}<dui-tree-select
          ${spaces}v-model="${modelPrefix}.${field.name}"
          ${spaces}entity="${DUIEntityMeta.name}"
        <#if isEditFormField>
          ${spaces}:disabled="${field.formDisabledValue}"
        </#if>
          ${spaces}field="${field.name}"/>
     <#else>
        ${spaces}<dui-select
          ${spaces}v-model="${modelPrefix}.${field.name}"
    <#if isEditFormField>
          :data="${field.name}Datas"
    </#if>
          ${spaces}entity="${DUIEntityMeta.name}"
        <#if isEditFormField>
          ${spaces}:disabled="${field.formDisabledValue}"
        </#if>
          ${spaces}field="${field.name}"/>
     </#if>
        <#assign hasSelectType=true/>
      <#elseif field.input.typeName=='NUMBER'>
        ${spaces}<el-input-number
          ${spaces}v-model="${modelPrefix}.${field.name}"
          ${spaces}:min="1"
          ${spaces}:max="10"
        <#if field.column.mapping.isSqlFloat()==true>
          ${spaces}:precision="2"
        </#if>
        <#if isEditFormField>
          ${spaces}:disabled="${field.formDisabledValue}"
        </#if>
          ${spaces}label="${(field.label)!''}"/>
      <#elseif field.input.typeName=='DATE_TIME'>
        ${spaces}<el-date-picker
          ${spaces}v-model="${modelPrefix}.${field.name}"
          ${spaces}type="datetime"
        <#if isEditFormField>
          ${spaces}:disabled="${field.formDisabledValue}"
        </#if>
         ${spaces} placeholder="选择日期时间">
        ${spaces}</el-date-picker>
      <#elseif field.input.typeName=='TIME'>
        ${spaces}<el-time-picker
          ${spaces}v-model="${modelPrefix}.${field.name}"
        <#if isEditFormField>
          ${spaces}:disabled="${field.formDisabledValue}"
        </#if>
          ${spaces}placeholder="选择时间>
       ${spaces}</el-time-picker>
      <#elseif field.input.typeName=='DATE'>
        ${spaces}<el-date-picker
          ${spaces}v-model="${modelPrefix}.${field.name}"
          ${spaces}type="date"
        <#if isEditFormField>
          ${spaces}:disabled="${field.formDisabledValue}"
        </#if>
          ${spaces}placeholder="选择日期">
        ${spaces}</el-date-picker>
      <#elseif field.input.typeName=='SWITCH'>
        ${spaces}<el-switch
          ${spaces}v-model="${modelPrefix}.${field.name}"
          ${spaces}active-color="#13ce66"
        <#if isEditFormField>
          ${spaces}:disabled="${field.formDisabledValue}"
        </#if>
          ${spaces}inactive-color="#ff4949">
        ${spaces}</el-switch>
      <#elseif field.input.isFileType()==true>
        ${spaces}<file-input
          ${spaces}v-model="${modelPrefix}.${field.name}File"
          ${spaces}:exist-file-path="${modelPrefix}.${field.name}"/>
        <#assign hasFileType=true/>
      <#else>
        ${spaces}<el-input
          ${spaces}v-model="${modelPrefix}.${field.name}"
          ${spaces}type="${field.input.typeName?lower_case}"
        <#if isEditFormField>
          ${spaces}:disabled="${field.formDisabledValue}"
        </#if>
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