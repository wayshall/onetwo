<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%    
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1    
response.setHeader("Pragma","no-cache"); //HTTP 1.0    
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server    
%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
	    <meta charset="utf-8">
	    <title>
			<layout:define name="title">欢迎使用市民卡运营支撑系统</layout:define>
	    </title>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
    	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	    <meta name="description" content="">
	    <meta name="author" content="">
		<script type="text/javascript" src="${siteConfig.jsPath}/jquery/jquery-1.7.2.min.js"></script>
		
	  <import:css path="/bootstrap/css/bootstrap" min="true" dev="${siteConfig.dev}" />
	  <import:css path="/bootstrap/css/bootstrap-responsive" min="true" dev="${siteConfig.dev}" />
	  
	  
	  
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
		<!-- customer setting -->
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/font-awesome/4.1.0/css/font-awesome.min.css"/>
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/font-awesome/4.1.0/css/font-awesome-ie7.min.css"/>
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/style.css"/>
		<script type="text/javascript" src="${siteConfig.jsPath}/major.js"></script>
    	
		<layout:define name="cssscript"></layout:define>
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
	</head>
	<body>
		<!-- <div id="showTipsModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
		</div> -->
		
	  
	    	<c:if test="${not empty message || not empty param.message}">
			<div class="alert alert-${t:firstNotblank(messageType, param.messageType, 'block')}  alert-dismissable">
  				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
  				<strong>提示!</strong>
				${t:escapeHtml(message)}
			  	${t:escapeHtml(param.message)}
			</div>
			
			</c:if>
			
			<layout:define name="main-content">
			页面详细内容
			</layout:define>
		
		<!--[if lte IE 6]>
	    <script type="text/javascript" src="${siteConfig.cssPath}/dev/js/bootstrap-ie.js"></script>
	    <script type="text/javascript">$(function() {$.bootstrapIE6(el);})</script>
	    <![endif]-->
	    <layout:define name="jsscript"></layout:define>
      	<script type="text/javascript">
      	$.jfish.initAfterPage();
      	</script>
	</body>
</html>
