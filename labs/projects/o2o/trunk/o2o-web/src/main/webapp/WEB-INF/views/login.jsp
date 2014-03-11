<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	

 <layout:override name="jsscript">
 	<script>
	 	$(function() {
			var version = $.browser.version;
			$('.right a').popover().on('show.bs.popover', function () { $(this).siblings().popover('hide'); });
			if(parseInt(version)>8) {
				$('.right a').fadeTo(0,0.6).hover(
					function() { $(this).stop().fadeTo(400,1) },
					function() { $(this).stop().fadeTo(400,0.6) }
				)
			}
		})
 		if(parent && parent.window.mainPage){
 			parent.location.href = location.href;
 		} 
 	</script>
 </layout:override>
 <layout:override name="main-content">
 <import:css path="/admin/login" min="false" dev="${siteConfig.dev}" />
 	<div class="data-board" data-name="test" data-url="${siteConfig.baseURL}/login/test">
 	</div>
 	
 	<c:choose>
		<c:when test="${helper.login}">
				已经登录：${helper.currentUserLogin.userName} <widget:link dataMethod="post" label="注销" href="${siteConfig.baseURL}/logout" dataConfirm="确定要注销？"/>
		</c:when>
		<c:otherwise>
		
	<div class="logo"></div>
    <div class="login container">
    	<center>
    	<div class="title">
        	<div></div>
            <h4>&nbsp;&nbsp;欢迎您使用本系统.</h4>
        </div>
        	<form class="form-horizontal" role="form" action="${siteConfig.baseURL}/login" method="post">
            <widget:formToken/>
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
    </div>
    <div class="right container">
       <a id="phone" data-container="body" data-toggle="popover" data-placement="top" data-title="固定电话" data-content="0763-3508222"></a>
       <a id="email" data-container="body" data-toggle="popover" data-placement="top" data-title="电子邮箱" data-content="admin@qyscard.com"></a>
       <a id="weibo" data-container="body" data-toggle="popover" data-placement="top" data-title="新浪微博" data-content="weibo.com/u/2094727705"></a>
    </div>
			</c:otherwise>
	</c:choose>
</layout:override>
</layout:extends>
