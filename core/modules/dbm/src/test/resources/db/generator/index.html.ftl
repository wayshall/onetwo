<meta charset="UTF-8">
<table id="dataGrid"
      title="${(table.comments[0])!''}" 
      style="height:100%;width:100%;">
<thead>
    <tr>
    <#list table.columns as column>
        <th field="${column.javaName}">${(column.comments[0])!''}</th>
    </#list>
    </tr>
</thead>
</table>