<#import "/lib/spring.ftl" as s/>
<#import ":fmtag:lib/html-helper.ftl" as h/>
<#import "/lib/import_libs.ftl" as import/>

<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign spring = JspTaglibs["http://www.springframework.org/tags"]/>
<#assign widget = JspTaglibs["/WEB-INF/widget.tld"]/>
<#assign security = JspTaglibs["/WEB-INF/security.tld"]/>


<!DOCTYPE html>
<html lang="zh">
	<head>
	    <meta charset="utf-8">
	    <title>
			<@define name="title">测试</@define>
	    </title>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="description" content="">
	    <meta name="author" content="">
					
		<@import.jquery version="1.7.2" min=true/>
		<@import.bootstrap/>
		
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
    </style>
    
		<@h.include_js src="table" plugin="fmtag"/>
		
		<@define name="jsscript"></@define>
		
	</head>
	<body>	
		<div class="navbar navbar-inverse navbar-fixed-top">
	      <div class="navbar-inner">
	        <div class="container">
	          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </a>
	          <a class="brand" href="${siteConfig.baseURL}/index">测试项目</a>
	          <div class="nav-collapse collapse">
	            <ul class="nav">
	              <li class="${activeLink!''}"><a href="${siteConfig.baseURL}/index">首页</a></li>
	              
	            </ul>
	            
			<#if helper.login>
				<p class="navbar-text pull-right">
				欢迎你，${helper.currentUserLogin.userName} <@h.link_to text="退出" message="确定要退出？" class="navbar-link" method="post" href="${siteConfig.baseURL}/cm/user/logout"/>
	            </p>
			<#else>
	           <@form.form modelAttribute="user" action="${siteConfig.baseURL}/cm/user/login" method="post" cssClass="navbar-form pull-right">
	              <input name="userName" class="span2" type="text" placeholder="用户名">
	              <input name="userPassword" class="span2" type="password" placeholder="密码">
	              <button type="submit" class="btn">登录</button>
	            </@form.form>
	        </#if>
	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
	    </div>
	    
	    
	    <div class="container">
	    	<#if message>
	    	<div class="alert alert-block">
			  <a class="close" data-dismiss="alert">×</a>
			  <h4 class="alert-heading">提示!</h4>
			  ${message}
			</div>
			</#if>
			
			<@define name="main-content">页面详细内容</@define>
			
		</div>
		
		
      <hr>

      <footer class="footer">
        <p>&copy; 基于jfish构建  2012.10</p>
      </footer>
      
	</body>
</html>
