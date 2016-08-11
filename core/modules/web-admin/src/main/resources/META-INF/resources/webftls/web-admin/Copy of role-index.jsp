<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/pages/common/taglibs.jsp" %>


<layout:extends parentPage="/WEB-INF/layout/easyui.jsp">
    <layout:override name="body">
		  <div class="easyui-panel" style="padding: 5px;" data-options="fit:true">
		    
		     <div class="easyui-panel" style="height:15%">
		        <form id="searchForm" class="easyui-form" >
		           <table style="padding: 5px;" cellpadding="5px">
		                <tr>
		                    <td>角色名:</td>
		                    <td>
		                        <input class="easyui-textbox searchFieldClass" type="text" name="name" 
		                               data-options="required:false,
		                                             validType:'length[0,50]' "/>
		                    </td>
		                </tr>
		                <tr>
		                   <td>&nbsp;</td>
		                    <td rowspan="1">
		                        <a id="btnSearch" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索&nbsp;</a>
		                    </td>
		               </tr>
		           </table>
		       </form>
		    </div>
		     
		    <div class="easyui-panel" style="height:85%" data-options="fit:true"> 
		       <table id="dataGrid"
		              title="角色表" >
		        <thead>
		            <tr>
		                <th field="id" checkbox="true"></th>
		                <th field="name" >角色名</th>
		                <th field="status" >状态</th>
		                <th field="remark" >角色描述</th>
		                <th field="createAt" >创建时间</th>
		                <th field="updateAt" >更新时间</th>
		            </tr>
		        </thead>
		      </table>
		    </div>
		    
		  </div>
		        
            <%@include file="role-edit-form.jsp" %>
		    
		    <div id="assignPermission" class="easyui-dialog" title="分配角色" 
		            style="width:50%;height:80%;padding:10px 20px"
		            data-options="closed:true, modal:true, buttons:'#assigRoleButtons'">
		        <form id="assignForm" class="easyui-form" method="post">
		            <div class="easyui-panel" style="padding:5px">
		                <ul id="permissionTree">
		                </ul>
		            </div>
		        </form>
		    </div>
		    
		    
		    <div id="selectApp" class="easyui-dialog" title="选择子系统" 
		            style="width:50%;height:80%;padding:10px 20px"
		            data-options="closed:true, modal:true">
		        <div class="easyui-panel" style="height:85%" data-options="fit:true"> 
		           <table id="appDataGrid"
		                  title="系统列表" >
		            <thead>
		                <tr>
		                    <th field="name" >角色名</th>
		                    <th field="_operation" formatter="permissionField.assignPermissionLink">操作</th>
		                </tr>
		            </thead>
		          </table>
		        </div>
		    </div>
		    
		    
		    <div id="assigRoleButtons" >
		        <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="assignPermissions();">保存</a>
		        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="closeAssignRole();">取消</a>
		    </div>   
		                 
		    <script type="text/javascript">
		    
		    $('#btnSearch').bind('click', function(){
		        var param = $('#searchForm').serialize();
		        var url = '${webConfig.adminBaseURL}/role.json?'+param;
		        $('#dataGrid').datagrid('options').url = url;
		        $('#dataGrid').datagrid('reload');
		    });
		
		    var onTreeLoading = true;
		    $('#permissionTree').tree({
		        method:'get',
		        animate:true,
		        checkbox:true,
		        onLoadSuccess: helper.loadTreeErrorHandler()
		    });
		
		    function closeAssignRole(){
		        $('#assignPermission').dialog('close');
		    }
		    
		    function assignPermissions(){
		        var role = $('#dataGrid').datagrid('getSelected');
		        var app = $('#appDataGrid').getSelectedRow();
		        var url = '${webConfig.adminBaseURL}/rolePermission/'+role.id+'.json?appCode='+app.code;
		        //$.post(url, param, helper.remoteMessageHandler());
		        var param = helper.getCsrfParams();
		        var permissionCodes = $('#permissionTree').tree('getAllCheckedIds') || [];
		        param.permissionCodes = permissionCodes;
		        param = $.param(param, true);
		        $.post(url, param, helper.remoteMessageHandler());
		    };
		    
		    var permissionField = {
		        assignPermissionLink:  function(val,row,index){
		            return '<a href="javascript:void(0)" onclick="permissionField.showPermissions('+index+');">分配权限</a>';
		        },
		        
		        showPermissions: function(index){
		            var role = $('#dataGrid').getSelectedRow();
		            var app = $('#appDataGrid').selectRow(index);
		            
		            $('#assignPermission').dialog('open');
		            var url = '${webConfig.adminBaseURL}/rolePermission/'+role.id+'.json?appCode='+app.code;
		           // $('#permissionTree').tree('options').url = url;
		            $('#permissionTree').tree({
		            	url: url,
		            	cascadeCheck: false
		            });
		            $('#permissionTree').tree('reload');
		        }
		        
		    }
		    var reqUrl = '${webConfig.adminBaseURL}/role.json';
		    var editingId;
		    var toolbar = [
		        {
		            text:'分配权限',
		            handler:helper.selectOneHandler('#dataGrid', function(row){
		               /*  $('#assignPermission').dialog('open');
		                
		                var url = '${webConfig.adminBaseURL}/rolePermission/'+row.id+'.json';
		                $('#permissionTree').tree('options').url = url;
		                $('#permissionTree').tree('reload'); */
		                $('#selectApp').dialog('open');
		                var url = '${webConfig.adminBaseURL}/application.json';
		                $("#appDataGrid").datagrid({
		                    iconCls: 'icon-ok',
		                    rownumbers: true,
		                    fitColumns: true,
		                    pagination: false,
		                    singleSelect: true,
		                    url: url,
		                    method: 'get',
		                }); 
		           })
		        },
		        
		        {
		            text:'新增',
		            iconCls:'icon-add',
		            handler:function(){
		                $('#addDataDialog').dialog('open').dialog('setTitle', '新增');
		
		                var url = '${webConfig.adminBaseURL}/role.json';
		                $('#dataForm').attr('action', url);
		                $('#dataForm').find('input[name="_method"]').val('');
		           }
		        },
		
		        {
		            text:'编辑',
		            iconCls:'icon-edit',
		            handler:function(){
		                if(!$('#dataGrid').isSelectedOne()){
		                    $.messager.alert('警告','请选择一条数据！','warning');
		                   return ;
		                }
		                
		                var dataForm = $('#dataForm');
		                var selected = $('#dataGrid').datagrid('getSelected');
		                
		                $('#addDataDialog').dialog('open').dialog('setTitle', '编辑');
		                
		                var url = '${webConfig.adminBaseURL}/role/'+selected.id+'.json';
		                dataForm.attr('action', url);
		                dataForm.find('input[name="_method"]').val('put');
		                dataForm.form('load', url);
		           }
		        },
		
		        {
		            text:'删除',
		            iconCls:'icon-remove',
		            handler:helper.deleteHandler({
		                        datagrid: '#dataGrid',
		                        url: '${webConfig.adminBaseURL}/role.json',
		                        idField: 'id',
		                        paramIdName: 'ids'
		                    })
		        }
		        
		    ];
		    
		    var selectedRow = null;
		    $("#dataGrid").datagrid({
		        iconCls: 'icon-ok',
		        rownumbers: true,
		        fitColumns: true,
		        pagination: true,
		        singleSelect: false,
		        url: reqUrl,
		        method: 'get',
		        pageSize: 20,
		        pageList: [20, 40, 60, 100],
		        //idField: 'id',
		        toolbar: toolbar
		    }); 
		 </script>
    </layout:override>
</layout:extends>
