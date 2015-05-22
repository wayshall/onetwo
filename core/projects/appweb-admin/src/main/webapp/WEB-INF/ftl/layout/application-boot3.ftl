<#import "/lib/spring.ftl" as s/>
<#import ":fmtag:lib/html-helper.ftl" as h/>
<#import "/lib/import_libs.ftl" as import/>

<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign spring = JspTaglibs["http://www.springframework.org/tags"]/>
<#assign widget = JspTaglibs["/WEB-INF/widget.tld"]/>
<#assign security = JspTaglibs["/WEB-INF/security.tld"]/>
<#assign layout = JspTaglibs["/WEB-INF/layout.tld"]/>
<#assign t = JspTaglibs["/WEB-INF/tools.tld"] /> 
<#assign aa = JspTaglibs["/WEB-INF/ajaxanywhere.tld"] /> 


<!DOCTYPE html>
<html lang="zh">
	<head>
	    <meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=no">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta http-equiv="pragma" content="no-cache">
	    <title>
			<@define name="title">系统</@define>
	    </title>
					
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="${siteConfig.cssPath}/bootstrap/3.3.4/css/bootstrap.min.css">
	<!-- Optional theme -->
	<link rel="stylesheet" href="${siteConfig.cssPath}/app.css">
	
	
	<script type="text/javascript" src="${siteConfig.jsPath}/jquery/1.11.2/jquery.min.js"></script>
	<script type="text/javascript" src="${siteConfig.cssPath}/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${siteConfig.baseURL}/fmtag/static/js/aa.js?t=${now.millis}"></script>
	<script type="text/javascript" src="${siteConfig.baseURL}/fmtag/static/js/table-jq-1.11.2.js?t=${now.millis}"></script>

		<@define name="jsscript"></@define>
		<@define name="cssscript"></@define>
	<style type="text/css">
		.wordbreak {
			word-wrap:break-word;
			word-break:break-all
		}
		.center {
		  width: auto;
		  display: table;
		  margin-left: auto;
		  margin-right: auto;
		}
		.text-center {
		  text-align: center;
		}
		.container small {
		  color: #F76200;
		  margin-right: 5px;
		  font-size: 12px;
		  line-height: 20px;
		  height: 20px;
		  overflow: hidden;
		}
	</style>
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
	    
	    <@aa.zone name="message">
	    	<#if helper.web('message')>
				<div class="alert alert-${helper.firstNotblank(messageType, helper.web('messageType'), 'block')}  alert-dismissable">
	  				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
	  				<strong>提示!</strong>
					${helper.web('message')}
				</div>
				<#if helper.web('noPermissionPath')>
					<div style="margin: 8px auto auto; text-align: center;">受限地址：${helper.web("noPermissionPath")}</div>
				</#if>
			</#if>
		</@aa.zone>
			
			<@define name="main-content">
			页面详细内容
			</@define>
			<br style="clear:both; border:0px; padding:0px; margin:0px;" />
		</div>
      <script type="text/javascript">
      $.jfish.initAfterPage();
      </script>
      
      <div>
      	lucky number：${__requestinfo__.executedTimeInMillis }
      </div>
	</body>
</html>
