
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<layout:extends>	

	
	<layout:override name="title">
		User列表
	</layout:override>
	
	
    <layout:override name="jsscript">
    <script>
    	$(function(){
    		$('input[name="appCode"]').click(function(){
    			var checked = $(this).attr('checked');
    			$(this).attr("disabled", true);
    			var _this = $(this);
    			var method = checked?'post':'delete';
    			$.post('<t:url href="/admin/app/permissionBind" />', {_method: method, roleId:$(this).attr("role"), appCode: $(this).val()}, function(data){
    				$.jfish.showAjaxMsg(data);
    				_this.removeAttr("disabled");
    			});
    		});
    	});
    </script>
    </layout:override>
	
 <layout:override name="main-content">
	

	<widget:dataGrid name="role" dataSource="${page}" title="用户列表" ajaxSupported="true" >
		<widget:dataRow name="entity" type="iterator" renderHeader="true">
			<widget:dataField name="name" label="角色名称" cssStyle="width:50px"/>
			<widget:dataField name="operation" label="分配权限" render="html" exportable="false">
				<c:forEach items="${apps}" var="app" varStatus="s">
					<widget:link href="/admin/app/permissionBind/${app.code}/${entity.id}" label="${app.name }" cssClass="btn btn-primary" dataConfirm="false" />
				</c:forEach>
			</widget:dataField>
		</widget:dataRow>
	</widget:dataGrid>
	
  </layout:override>
  
</layout:extends>
