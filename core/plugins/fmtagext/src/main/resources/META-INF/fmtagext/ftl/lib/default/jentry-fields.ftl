<fieldset>

<#if label?has_content>			
<legend>
	<strong>${__jentryDO__.label}</strong> ${__jentryDO__.isCreate()?string("新增", "修改")}
</legend>
</#if>

<#if __jentryDO__.isCreate()==false>
	<input name="${__entryData__.idName}" value="${__entryData__.idValue}" type="hidden"/>
</#if>

${__jentryDO__.bodyContent}
<#list __jentryDO__.fields as jfield>
	<#if jfield.autoRender==false>
		${jfield.render()}
	<#elseif jfield.templateField>
		<#include "${jfield.template}.ftl"/>
	<#else>
		<@jui.formField entry=__jentryDO__ field=jfield entryData=__entryData__/>
	</#if>
</#list>

<#if __jentryDO__.formButtons?has_content>
  <div class="form-actions">
  <#list __jentryDO__.formButtonList as formBtn>
  	&nbsp;
  	<#if formBtn?starts_with(":")>
		<#include "${selfConfig.templateBasePath+'/lib/default/jentry-formbuttons-'+formBtn?substring(1)}.ftl"/>
  	<#else>
		<#include "${formBtn}.ftl"/>
  	</#if>
  </#list>
  </div>
</#if>
  
</fieldset>