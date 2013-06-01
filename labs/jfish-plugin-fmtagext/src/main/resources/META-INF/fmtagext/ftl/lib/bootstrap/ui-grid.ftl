<#import "[fmtagext]/lib/ui-helper.ftl" as helper>

<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign spring = JspTaglibs["http://www.springframework.org/tags"]/>

<#assign __dg__=__this__.component/>

<#attempt>

<#if __dg__.form??>
	<@form.form modelAttribute="${__dg__.form.name}" action="${siteConfig.baseURL+__dg__.form.actionValuer.getUIValue(__dg__.data)}" method="${__dg__.form.methodString}" cssClass="form-horizontal ${__dg__.form.cssClass}">
	
	<#include "[fmtagext]/lib/bootstrap/_ui-grid.ftl"/>
	</@form.form>
	<script>
		jQuery("#${__dg__.form.name}").initDatagrid();
	</script>
<#else>
	<#include "[fmtagext]/lib/bootstrap/_ui-grid.ftl"/>
</#if>
		
<#recover>
	<span style="color:red;"><b>有虫子爬进表格了，先抓一抓吧……</b><span>
</#attempt>

