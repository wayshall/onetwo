
<@extends>	

	
	<@override name="title">
		登录
	</@override>
	<@override name="jsscript">
		<script>
 		if(parent && parent.window.mainPage){
 			parent.location.href = location.href;
 		} 
 		</script>
	</@override>
	
	
 	<@override name="main-content">
			<#if helper.login>
					已经登录：${helper.currentUserLogin.userName} <widget:link dataMethod="post" label="注销" href="${pluginConfig.baseURL}/common/logout" dataConfirm="确定要注销？"/>
			</#if>
			
	    <div class="login container">
	    	<center>
	    	<div class="title">
	            <h4>&nbsp;&nbsp;${siteConfig.appName }</h4>
	        </div>
	        	<form class="form-horizontal" role="form" action="${pluginConfig.baseURL}/common/login" method="post">
	            <@widget.formToken/>
	              <div class="form-group">
	                <input type="text" class="form-control" name="userName" id="userName" placeholder="请输入用户名">
	                <p>Tips：<span>用户名可能是字母，数字或汉字的组合.</span></p>
	              </div>
	              <div class="form-group">
	                <input type="password" class="form-control" name="userPassword" id="userPassword" placeholder="请输入密码">
	              </div>
	              <button type="submit" class="btn btn-default btn-lg" data-loading-text="正在登录……">&nbsp;&nbsp;登&nbsp;&nbsp;录&nbsp;&nbsp;</button>
	            </form>
	        </center>
	
  	</@override>
  
</@extends>
