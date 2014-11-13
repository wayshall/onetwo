<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ page trimDirectiveWhitespaces="true"%>
<%    
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1    
response.setHeader("Pragma","no-cache"); //HTTP 1.0    
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server    
%>
<!DOCTYPE html>
<html lang="zh">
	<head>
	    <meta charset="utf-8">
	    <title>
			<layout:define name="title">欢迎使用市民卡运营支撑系统</layout:define>
	    </title>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="description" content="">
	    <meta name="author" content="">
		<script type="text/javascript" src="${siteConfig.jsPath}/jquery/jquery-1.7.2.min.js"></script>
		
	  <import:css path="/bootstrap/css/bootstrap" min="true" dev="${siteConfig.dev}" />
	  <import:css path="/bootstrap/css/bootstrap-responsive" min="true" dev="${siteConfig.dev}" />
	  
	  <import:css path="/admin/assets/styles" min="false" dev="${siteConfig.dev}" />
	  
	    <!--[if lte IE 6]>
  <link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/bootstrap/css/bootstrap-ie6.css">
  <![endif]-->
  <!--[if lte IE 7]>
  <link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/bootstrap/css/ie.css">
  <![endif]-->
	  
	<script type="text/javascript" src="${siteConfig.cssPath}/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${siteConfig.baseURL}/fmtag/static/js/aa.js?t=${now.millis}"></script>
	<script type="text/javascript" src="${siteConfig.baseURL}/fmtag/static/js/table.js?t=${now.millis}"></script>
	<script type="text/javascript" src="${siteConfig.jsPath}/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
	.wordbreak {
		word-wrap:break-word;
		word-break:break-all
	}
	</style>
    <layout:define name="jsscript"></layout:define>
    
<c:if test="${themeSetting.jsui}">
	<import:js name="ext-all" module="extjs"/>
	<link rel="stylesheet" type="text/css" href="${siteConfig.jsPath}/extjs/resources/ext-theme-${themeSetting.extTheme }/ext-theme-${themeSetting.extTheme }-all.css" />
	<script>
	Ext.Loader.setConfig({
	    enabled: true
	});
	Ext.Loader.setPath('Ext.ux', '${siteConfig.jsPath}/extjs/ux');
	</script>
</c:if>

	<layout:define name="cssscript"></layout:define>
	
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
	    	<c:if test="${not empty t:web('message')}">
			<div class="alert alert-${t:firstNotblank(messageType, t:web('messageType'), 'block')}  alert-dismissable">
  				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
  				<strong>提示!</strong>
				${t:web('message')}
			</div>
			<c:if test="${not empty t:web('noPermissionPath')}">
				<div style="margin: 8px auto auto; text-align: center;">受限地址：${t:web("noPermissionPath")}</div>
			</c:if>
			
			</c:if>
			
			<layout:define name="main-content">
			页面详细内容
			</layout:define>
			<br style="clear:both; border:0px; padding:0px; margin:0px;" />
		</div>
      <script type="text/javascript">
      $.jfish.initAfterPage();
      </script>
	</body>
</html>

  <script type="text/javascript" src="${siteConfig.cssPath}/bootstrap/js/bootstrap-ie.js?t=${now.millis}"></script>
  <script>

  if ($.eb.ie6()) {
	$('.jfish-toggle-control').trigger('click');
	$('.jfish-toggle-control').trigger('click');
  }
  </script>
