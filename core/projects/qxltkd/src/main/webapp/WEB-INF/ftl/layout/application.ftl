<#import "/lib/spring.ftl" as s/>
<#import ":fmtag:lib/html-helper.ftl" as h/>
<#import "/lib/import_libs.ftl" as import/>

<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign spring = JspTaglibs["http://www.springframework.org/tags"]/>
<#assign widget = JspTaglibs["/WEB-INF/widget.tld"]/>
<#assign security = JspTaglibs["/WEB-INF/security.tld"]/>
<#assign layout = JspTaglibs["/WEB-INF/layout.tld"]/>
<#assign t=JspTaglibs["/WEB-INF/tools.tld"] /> 


<!DOCTYPE html>
<html lang="zh">
	<head>
	    <meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=no">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta http-equiv="pragma" content="no-cache">
	    <title>
			<@define name="title">市民卡运营支撑系统</@define>
	    </title>
					
		<script type="text/javascript" src="${siteConfig.jsPath}/jquery/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${siteConfig.jsPath}/jquery/jquery.placeholder.js"></script>
		<script>
		/*数据表格--输入框placeholder*/
		$(function() {
			$('input, textarea').placeholder();
			$(".datagrid .handle a").addClass("btn btn-small");
		})
		</script>
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/bootstrap/css/bootstrap.min.css?t=${now.millis}">
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/bootstrap/css/bootstrap-responsive.min.css">
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/admin/assets/styles.css">
		
		<link rel="stylesheet" href="${siteConfig.cssPath}/style.css" />
		<link rel="stylesheet" href="${siteConfig.cssPath}/font-awesome/4.1.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="${siteConfig.cssPath}/font-awesome/4.1.0/css/font-awesome-ie7.min.css">
		<script type="text/javascript" src="${siteConfig.cssPath}/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${siteConfig.baseURL}/fmtag/static/js/aa.js"></script>
		<script type="text/javascript" src="${siteConfig.baseURL}/fmtag/static/js/table.js"></script>
		<script type="text/javascript" src="${siteConfig.jsPath}/My97DatePicker/WdatePicker.js"></script>
        <@define name="jsscript"></@define>		
        
     <#if themeSetting.jsui>
		<script type="text/javascript" src="${siteConfig.jsPath}/extjs/ext-all.js"></script>
		<link rel="stylesheet" type="text/css" href="${siteConfig.jsPath}/extjs/resources/ext-theme-${themeSetting.extTheme }/ext-theme-${themeSetting.extTheme }-all.css" />
		<script>
		Ext.Loader.setConfig({
		    enabled: true
		});
		Ext.Loader.setPath('Ext.ux', '${siteConfig.jsPath}/extjs/ux');
		</script>
	</#if>
		<@define name="cssscript"></@define>
	</head>
	<body>	
		<div id="showTipsModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		    <h3 id="tipsTitle" class="modal-title">提示</h3>
		  </div>
		  <div class="modal-body">
		    <p>loading……</p>
		  </div>
		  <div class="modal-footer">
		    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
		  </div>
		</div>


	    <div class="container-fluid">
	    	<#if message>
	    	<div class="alert alert-block">
			  <a class="close" data-dismiss="alert">×</a>
			  <h4 class="alert-heading">提示!</h4>
			  ${message}
			</div>
			</#if>
			
			<@define name="main-content">
			页面详细内容
			</@define>
		</div>
		
			<@define name="jsscript"/>
			<script>
				$(function() {
					$(".toolbar .btn-primary").removeClass("btn-primary").addClass("btn").addClass("btn-small")
					$(".handle a").addClass("btn").addClass("btn-small");
					$(".toolbar button").addClass("btn-small");
					
				})
			</script>
			<style>
				#d4311 { margin-right: 8px; }
				#d4312 { margin-left: 8px; }
			</style>
		
	</body>
</html>
