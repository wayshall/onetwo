<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<!DOCTYPE html>
<html lang="zh">
	<head>
	    <meta charset="utf-8">
	    <title>
			<layout:define name="title">标题</layout:define>
	    </title>
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="description" content="">
	    <meta name="author" content="">
					
		<script type="text/javascript" src="${siteConfig.jsPath}/jquery/jquery-1.7.2.min.js"></script>
		
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/bootstrap/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath}/bootstrap/css/bootstrap-responsive.css" />
		<script type="text/javascript" src="${siteConfig.cssPath}/bootstrap/js/bootstrap.js"></script>
		
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
    </style>
    
		<script type="text/javascript" src="${siteConfig.baseURL}/fmtag/static/js/table.js"></script>
		<layout:define name="jsscript"></layout:define>		
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
	              <li class=""><a href="${siteConfig.baseURL}/index">首页</a></li>
	              
	            </ul>
	            
	      <c:choose>
			<c:when test="${helper.login}">
				<p class="navbar-text pull-right">
				欢迎你，${helper.currentUserLogin.userName} 
				<a class="navbar-link" data-method="post" data-confirm="确定要退出？" href="${siteConfig.baseURL}/cm/user/logout">退出</a>
	            </p>
	        </c:when>
			<c:otherwise>
	           <form:form modelAttribute="user" action="${siteConfig.baseURL}/login" method="post" cssClass="navbar-form pull-right">
	              <input name="userName" class="span2" type="text" placeholder="用户名">
	              <input name="userPassword" class="span2" type="password" placeholder="密码">
	              <button type="submit" class="btn">登录</button>
	            </form:form>
	        </c:otherwise>
	        </c:choose>
	        
	          </div><!--/.nav-collapse -->
	        </div>
	      </div>
	    </div>
	    
	    
	    <div class="container">
	    	<c:if test="${not empty message}">
	    	<div class="alert alert-block">
			  <a class="close" data-dismiss="alert">×</a>
			  <h4 class="alert-heading">提示!</h4>
			  ${message}
			</div>
			</c:if>
			
			<layout:define name="main-content">
			页面详细内容
			</layout:define>
			
		</div>
		
		
      <hr>

      <footer class="footer">
        <p>&copy; 基于jfish构建  2012.10</p>
      </footer>
      
	</body>
	
</html>
