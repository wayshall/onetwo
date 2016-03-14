<@extends parent="application.html">
    
    <@override name="charset">
        <meta charset="UTF-8">
    </@override>
    
    <@override name="title">
               字典数据管理
    </@override>
    <@override name="main-content">
       
       <table id="dataGrid"
               title="数据字典表"
               class="easyui-treegrid"
               style="height:100%;width:100%;">
            <thead>
                <tr>
                    <th field="id" checkbox="true">选择</th>
                    <th field="code">字典编码</th>
                    <th field="name">显示名称</th>
                    <th field="value">字典值</th>
                    <th field="dictType">字典类型</th>
                    <th field="parentCode">所属类别</th>
                    <th field="isValid">是否有效</th>
                    <th field="isEnumValue">是否枚举常量</th>
                    <th field="sort">排序</th>
                    <th field="remark">描述</th>
                    <th field="updateAt">最后更新日期</th>
                </tr>
            </thead>
        </table>
        
    <#include "/configmgr/dictionary-edit-form.html">
       
                 
    <script type="text/javascript">
    
    var editingId;
    var toolbar = [
        {
            text:'新增',
            iconCls:'icon-add',
            handler:function(){
                $('#addDataDialog').dialog('open').dialog('setTitle', '新增');


                var selected = $('#dataGrid').treegrid('getSelected');
                if(selected){
                    $('#parentCode').textbox('setText', selected.code);
                    $('#parentCode').textbox('setValue', selected.code);
                }
                
                var url = '${siteConfig.baseURL}/configmgr/dictionary.json';
                $('#dataForm').attr('action', url);
                $('#dataForm').find('input[name="_method"]').val('');
           }
        },

        {
            text:'编辑',
            iconCls:'icon-edit',
            handler:function(){
                var selectedNodes = $('#dataGrid').treegrid('getSelections');
                if(!selectedNodes || selectedNodes.length>1){
                    $.messager.alert('警告','请选择一条数据！','warning');
                   return ;
                }
                
                var dataForm = $('#dataForm');
                var selected = $('#dataGrid').treegrid('getSelected');
                
                $('#addDataDialog').dialog('open').dialog('setTitle', '编辑');
                $('#code').textbox('disable');
                var url = '${siteConfig.baseURL}/configmgr/dictionary/'+selected.code+'.json';
                dataForm.attr('action', url);
                dataForm.find('input[name="_method"]').val('put');
                dataForm.form('load', url);
           }
        },

        {
            text:'删除',
            iconCls:'icon-remove',
            handler:function(){
                var selectedNodes = $('#dataGrid').treegrid('getSelections');
                if(!selectedNodes){
                    $.messager.alert('警告','请先选择数据！','warning');
                   return ;
                }
                var codes = $.map(selectedNodes, function(e){
                    return e.code;
                });
                helper.deleteHandler({
                    treegrid: '#dataGrid',
                    url: '${siteConfig.baseURL}/configmgr/dictionary.json',
                    params: {'code': codes}
                });
           }
        }
        
    ];
    
    var selectedRow = null;
    var loadUrl = '${siteConfig.baseURL}/configmgr/dictionary.json';
    $("#dataGrid").treegrid({
        iconCls: 'icon-ok',
        rownumbers: true,
        fitColumns: true,
        pagination: true,
        singleSelect: false,
        url: loadUrl,
        method: 'get',
        idField: 'code',
        toolbar: toolbar,
        treeField: 'name',
        onClickRow: function(row){
            if(selectedRow && selectedRow==row){
                $('#dataGrid').treegrid('unselect', selectedRow.id);
                selectedRow = null;
            }else{
                selectedRow = row;
            }
        },
        onBeforeExpand:function(row){  
            //动态设置展开查询的url  
            var url = '${siteConfig.baseURL}/configmgr/dictionary/children.json?parentCode='+row.code;
            $("#dataGrid").treegrid("options").url = url;  
            return true;      
        },
        onLoadSuccess: function(){
            $("#dataGrid").treegrid("options").url = loadUrl;  
            return true;
        }
    }); 
 </script>
    </@override>
</@extends>