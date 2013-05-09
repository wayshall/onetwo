<#macro text name id="" attrs="">
	<#if id?has_content == false><#local id=name/></#if>
	<input name="${name}" id="${id}" type="text" ${attrs}/>
</#macro> 

<#macro hidden name value>
	<input name="${name}" type="hidden" value="${value}"/>
</#macro> 

<#macro form id="" name="submitForm" action="" method="get" attrs="" enctype="">
    <#local atualMethod=method />
    <#local _enctype=enctype />
    <#if atualMethod?lower_case == "get">
    	<#local atualMethod="get" />
    	<#local _enctype="application/x-www-form-urlencoded" />
    <#else>
    	<#local atualMethod="post" />
    </#if>
    <form id="${id}" name="${name}" action="${action}" method="${atualMethod}" enctype="${_enctype}" ${attrs}>
    <#if atualMethod?lower_case != "get">
    	<input name="_method" value="${method}" type="hidden"/>
    </#if>
    	<#nested>
    </form>
</#macro>

<#macro submit value id="" attrs="">
	<input id="${id}" value="${value}" type="submit" ${attrs}/>
</#macro>

<#macro button name submit="" id="" onclick="" attrs="">
	<#if id?has_content == false><#local id=name/></#if>
	<button id="${id}" name="${name}" onclick="<#if submit?has_content>document.[${submit}].submit();</#if>${onclick}">提交</button>
</#macro>

<#macro link name action target="" class="btn">
	<a class="${class}" target="${target}" href="${action}">${name}</a>
</#macro>

<#macro new_link name="新建" target="" class="btn">
	<a class="${class}" target="${target}" href="${urlHelper.newAction}">${name}</a>
</#macro>

<#macro show_link entity name="查看" showname="" target="" class="btn">
	<a class="${class}" target="${target}" href="${siteConfig.baseURL+urlHelper.showAction(entity)}"><#if showname?has_content>${entity[showname]?default(name)}<#else>${name}</#if></a>
</#macro>

<#macro edit_link entity name="修改" showname="" target="" class="btn">
	<a class="${class}" target="${target}" href="${siteConfig.baseURL+urlHelper.editAction(entity)}"><#if showname?has_content>${entity[showname]?default(name)}<#else>${name}</#if></a>
</#macro>

<#macro delete_link entity message="确定要删除？" name="删除" showname="" target="" class="btn">
	<a class="${class}" target="${target}" data-method="delete" data-confirm="${message}" href="${siteConfig.baseURL+urlHelper.showAction(entity)}"><#if showname?has_content>${entity[showname]?default(name)}<#else>${name}</#if></a>
</#macro>

<#macro delete_batch_link entity message="确定要删除？" name="删除" showname="" target="" class="">
	<a class="${class}" target="${target}" data-method="delete" data-confirm="${message}" href="${siteConfig.baseURL+urlHelper.listAction}"><#if showname?has_content>${entity[showname]?default(name)}<#else>${name}</#if></a>
</#macro>

<#macro link_to href method="get" message="确定要执行此操作？" text="提交" target="" class="" params="">
	<a class="${class}" target="${target}" data-method="${method}" data-confirm="${message}" href="${href}" data-params="${params}">
	${text}
	<span style="display:none;" class="link_params"><#nested></span>
	</a>
</#macro>

<#macro include_js src module="" plugin="" ts=siteConfig.dev>
	<#if module?has_content && module?starts_with("/")==false ><#local module="/${module}"/></#if>
	<#local jspath=siteConfig.jsPath/>
	<#if plugin?has_content><#local jspath=helper.getPluginConfig(plugin).jsPath/></#if>
	<#if src?is_sequence>
		<#list src as file>
		<script type="text/javascript" src="${jspath + module}/${file}.js<#if ts==true>?t=${now.millis}</#if>"></script>
		</#list>
	<#else>
		<script type="text/javascript" src="${jspath + module}/${src}.js<#if ts==true>?t=${now.millis}</#if>"></script>
	</#if>
</#macro>

<#macro include_css src module="">
	<#if module?has_content && module?starts_with("/")==false ><#local module="/${module}"/></#if>
	<#if src?is_sequence>
		<#list src as file>
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath + module}/${file}.css" />
		</#list>
	<#else>
		<link rel="stylesheet" type="text/css" href="${siteConfig.cssPath + module}/${src}.css" />
	</#if>
</#macro>

<#macro image src width height class="" style="" alt="">
	<img class="${class}" style="${style}" alt="${alt}" src="${siteConfig.rsPath}<#if src!=''><#if siteConfig.dev>${src}<#else>${src?replace('\\.\\w+$', '_'+width+'_'+height+'$0', 'r')}</#if><#else>${siteConfig.getProperty('site.default.image')}</#if>" width="${width}" height="${height}" onerror="this.src='${siteConfig.rsPath+siteConfig.getProperty('site.default.image')}';"/>
</#macro>

