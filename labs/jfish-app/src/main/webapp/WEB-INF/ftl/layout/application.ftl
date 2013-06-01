<#import "/lib/spring.ftl" as s/>
<#import ":fmtag:/lib/html-helper.ftl" as h/>
<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign spring = JspTaglibs["http://www.springframework.org/tags"]/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title> 
			<@define name="title">后台管理页面 - </@define> 
		</title>
		
		<link rel="stylesheet" href="${siteConfig.jqueryuiPath}/themes/base/jquery.ui.all.css">
		<script type="text/javascript" src="${siteConfig.jqueryPath}/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${siteConfig.jqueryuiPath}/jquery.ui.core.js"></script>
		<script type="text/javascript" src="${siteConfig.jqueryuiPath}/jquery.ui.widget.js"></script>
		<script type="text/javascript" src="${siteConfig.jsPath}/My97DatePicker/WdatePicker.js"></script>
		
		<@h.include_css src=["style", "yooyAdmin"] />
		<@h.include_js src="table" plugin="fmtag"/>
		
	</head>
	<body id="yyZjH">
				
		<!--headder begin-->
		<!-- #BeginLibraryItem "/Library/yyAdminHeader.lbi" -->
		<#include "/common/header.ftl" />
		<!-- #EndLibraryItem -->
		<!--headder end-->
		<div class="blank10"></div>
		<!--content begin-->
		<div class="shop-supplier-content w960">
		<!-- 
		<div class="historyUrl">
			<a href="#" title="首页">首页</a> &gt;
			<a href="#" title="优游后台">优游后台</a> &gt;
			<a href="#" title="自驾">自驾</a> &gt;
			<a href="#" title="交易和售后">交易和售后</a> &gt; <template:define name="page_location" />
		</div>
		
		
		 -->
		
		<div class="w960 clear">
			<!-- #BeginLibraryItem "/Library/zjMenu.lbi" -->
			<@dataComponent var="menuData" datasource="tuserServiceImpl" template="common/menu.ftl" method="fetchData">
			</@dataComponent>
			
			<@dataComponent var="menuData" datasource="tuserServiceImpl" method="fetchDataByMap" params="{name:'test'}">
				<#list menuData?keys as key>
					<dd class="menu_on"><a href="${siteConfig.baseURL}${menuData[key]}">- ${key}</a></dd>
				</#list>
			</@dataComponent>
			
			<@dataComponent var="menuData" datasource="tuserServiceImpl" method="fetchDataByParams" params="test, 2">
				<#list menuData?keys as key>
					<dd class="menu_on"><a href="${siteConfig.baseURL}${menuData[key]}">- ${key}</a></dd>
				</#list>
			</@dataComponent>
			<!-- #EndLibraryItem -->
			<div class="shop-supp-main right order-zjk">
				
				
				<div class="shop-ui-tab02">
					<div id="tab-Menu" class="tab-Menu">
					
						<!-- 
							tab_menu_bar
						 -->
						
					</div>
					<div id="tab-cont-warp" class="tab-cont-warp">
					
						<div class="shop-content tab01">
							<h3 class="shop-bTit01">
							
									<!--
									table title
									-->
							</h3>
							<div class="shop-ui-tab-warp">
								<div id="shop-ui-tab-content" class="shop-ui-tab-content">
								
										<#if message??>
											<div id="message" class="success"><span style="color:red"><b>${message}</b></span></div>	
										</#if>
									
										<@define name="search-bar">
										</@define>
									
									<div class="blank10"></div>
										<@define name="main-content">
											define data-list
										</@define>
									<!--
										content 
									 -->
								</div>
							</div>
						</div>
					
					</div>
				</div>
			
			</div>
		</div>
		</div>
		<!--content end-->
		<!--footer begin-->
		<#include "/common/footer.ftl" />
		<!--footer end-->
	</body>
</html>
