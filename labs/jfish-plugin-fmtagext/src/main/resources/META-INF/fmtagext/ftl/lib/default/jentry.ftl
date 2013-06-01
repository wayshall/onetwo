<#import "${selfConfig.templateBasePath}lib/jui-hepler.ftl" as jhelper>
<#import "${selfConfig.templateBasePath}lib/default/jfish-ui.ftl" as jui>

<#attempt>

	<#assign __entity__=__entryData__.entity>
	<#if __jentryDO__.show>
		<@jui.showGrid jentryDO=__jentryDO__ entryData=__entryData__/>
	<#else>
		<#if __jentryDO__.form>
			<@form.form modelAttribute="${__jentryDO__.name}" action="${__jentryDO__.formAction}" method="${__jentryDO__.formMethod}" cssClass="form-horizontal">
				<#include "${selfConfig.templateBasePath}/lib/default/jentry-fields.ftl">		
			</@form.form>
		<#else>
			<#include "${selfConfig.templateBasePath}/lib/default/jentry-fields.ftl">
		</#if>
	</#if>
<#recover>
	<span style="color:red;"><b>有虫子爬进来了，先抓一抓吧……</b><span>
</#attempt>

