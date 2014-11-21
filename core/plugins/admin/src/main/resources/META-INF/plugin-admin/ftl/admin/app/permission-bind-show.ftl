
<@extends>	
	<@override name="jsscript">
	<link rel="stylesheet" type="text/css" href="${siteConfig.jsPath}/ztree/css/zTreeStyle.css" />
<script type="text/javascript" src="${siteConfig.jsPath}/ztree/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${siteConfig.jsPath}/ztree/js/jquery.ztree.exhide-3.5.js"></script>

		<script>
			var jsonData = ${menuJsonData};
			var setting = {
				data: {
					simpleData: {
						enable: true,
						pIdKey: "pid"
					}
				},
				check: {
					enable: true
				}
			};
			$(document).ready(function(){
				$.fn.zTree.init($("#permissionTree"), setting, jsonData);
			});
			$(function(){
				$('#btnSavePermission').click(function(){
					$.showBlockMsg('请稍候……');
					var zTree = $.fn.zTree.getZTreeObj("permissionTree");
					var changeNodes = zTree.getCheckedNodes();
					var changedIds = $.map(changeNodes, function(node){
											return node.id;
										});
					
					var data = {
							appCode: '${appCode}',
							roleId: ${id},
							permissionId: changedIds
						};
					$.post('<@t.url href="${pluginConfig.baseURL}/app/permissionBind"/>', $.param(data, true), function(rs){
						$.jfish.showAjaxMsg(rs);
					}).error(function(rs){
						$.jfish.showAjaxMsg(rs);
					});;
				});
			});
		</script>
	</@override>
	<@override name="main-content">
		
		<div>
			<a id="btnSavePermission" href="javascript:void(0);" class="btn btn-primary">保存权限配置</a>
		</div>
		<div class="zTreeDemoBackground left">
			<ul id="permissionTree" class="ztree"></ul>
		</div>
	</@override>
</@extends>