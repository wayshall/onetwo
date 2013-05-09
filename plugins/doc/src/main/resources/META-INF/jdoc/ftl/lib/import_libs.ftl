

<#macro bootstrap min=!siteConfig.dev>
	<#local basePath=pluginConfig.cssPath/>
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
