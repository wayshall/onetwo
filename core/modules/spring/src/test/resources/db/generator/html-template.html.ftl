<meta charset="UTF-8">
<#list table.columns as column>
<tr>
    <td>${(column.comments[0])!''}:</td>
    <td>
        <input class="easyui-${(column.mapping.attrs.cssClass)!'textbox'}" type="text" name="${column.javaName}" data-options=""/>
    </td>
</tr>
</#list>