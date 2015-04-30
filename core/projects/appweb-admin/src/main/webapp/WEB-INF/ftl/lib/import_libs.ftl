
<#macro jquery min=!siteConfig.dev version="1.8.2">
	<#local basePath=siteConfig.jsPath/>
	<#local fname="jquery-${version}"/>
	<#local postfix=""/>
	<#if siteConfig.dev>
		<#local postfix="?t=${now.millis}"/>
	</#if>
	<#if min>
		<#local fname=fname+".min"/>
	</#if>
	<script type="text/javascript" src="${basePath}/jquery/${fname}.js${postfix}"></script>
</#macro>


<#macro jqueryui min=!siteConfig.dev version="1.9.0">
	<#local basePath=siteConfig.jsPath/>
	<#local fname="jquery-ui-${version}"/>
	<#local postfix=""/>
	<#if siteConfig.dev>
		<#local postfix="?t=${now.millis}"/>
	</#if>
	<#if min>
		<#local fname=fname+".min"/>
	</#if>
	<script type="text/javascript" src="${basePath}/jquery/ui/${fname}.js${postfix}"></script>
	<link rel="stylesheet" type="text/css" href="${basePath}/jquery/ui/ui-lightness/${fname}.css${postfix}" />
</#macro>

<#macro calendar min=!siteConfig.dev>
	<#local basePath=siteConfig.jsPath/>
	<#local fname="fullcalendar"/>
	<link rel="stylesheet" type="text/css" href="${basePath}/calendar/${fname}.css" />
	<link rel="stylesheet" type="text/css" href="${basePath}/calendar/${fname}.print.css" />
	<#local postfix=""/>
	<#if siteConfig.dev>
		<#local postfix="?t=${now.millis}"/>
	</#if>
	<#if min>
		<#local fname=fname+".min"/>
	</#if>
	<script type="text/javascript" src="${basePath}/calendar/${fname}.js${postfix}"></script>
</#macro>

<#macro datepicker>
	<#local basePath=siteConfig.jsPath/>
	<script type="text/javascript" src="${basePath}/datepicker/WdatePicker.js"></script>
</#macro>

<#macro bootstrap min=!siteConfig.dev>
	<#local basePath=siteConfig.cssPath/>
	<#local fname="bootstrap"/>
	<#local responsive_fname="bootstrap-responsive"/>
	<#local postfix=""/>
	<#if siteConfig.dev>
		<#local postfix="?t=${now.millis}"/>
	</#if>
	<#if min>
		<#local fname=fname+".min"/>
		<#local responsive_fname=responsive_fname+".min"/>
	</#if>
	<link rel="stylesheet" type="text/css" href="${basePath}/bootstrap/css/${fname}.css${postfix}" />
	<link rel="stylesheet" type="text/css" href="${basePath}/bootstrap/css/${responsive_fname}.css${postfix}" />
	<script type="text/javascript" src="${basePath}/bootstrap/js/${fname}.js${postfix}"></script>
</#macro>
