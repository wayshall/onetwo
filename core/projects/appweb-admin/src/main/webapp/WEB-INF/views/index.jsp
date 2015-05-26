
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	

	
	<layout:override name="title">
		列表
	</layout:override>
	
 	<layout:override name="main-content">
 		欢迎使用jfish
 		<ul>
 			<li><a href="${siteConfig.baseURL}/plugin-admin/data/init" target="_blank">1、初始化基础数据</a></li>
 			<li><a href="${siteConfig.baseURL}/plugin-permission/index" target="_blank">2、导入基础菜单数据</a></li>
 			<li>3、<a href="${siteConfig.baseURL}/plugin-security/common/login" target="_blank">登录</a>后，点击<a href="${siteConfig.baseURL}/plugin-admin/index" target="_blank">这里</a>进入管理后台</a></li>
 		</ul>
  	</layout:override>
  
</layout:extends>
