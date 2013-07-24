<#import "[fmtagext]/lib/ui-helper.ftl" as helper>

<#assign __component__=__this__.component/>

<#attempt>
	

<@form.form modelAttribute="${__component__.name}" action="${siteConfig.baseURL+__component__.actionValuer.getUIValue(__this__.data)}" method="${__component__.methodString}" cssClass="form-horizontal">

<#if __component__.isRenderFormBody()>
	<#if __component__.title?has_content>		
	<legend>
		<strong>${__component__.title}</strong>
	</legend>
	</#if>
	
	<#if !__component__.fields.empty>	
		<fieldset>
		<#list 	__component__.fields as field>
			<#if field.isUIShowable(__this__.data)>
				<#if field.hiddenField>
					<@jfishui component=field data=__this__.data/>
				<#else>
					<@jfishui component=field data=__this__.data/>
			    </#if>
			</#if>
		    
		</#list>
		</fieldset>
	</#if>
	
	<@jfishui component=__component__.children data=__this__.data/>
	
</#if>

<#if !__component__.buttons.empty>
	<div class="form-actions">
	  <#list __component__.buttons as btn>
	  	<@jfishui component=btn data=__this__.data/>&nbsp;
	  </#list>
	</div>
</#if>

	${__bodyContent__}
</@form.form>
<#recover>
	<span style="color:red;"><b>有虫子爬进表单了，先抓一抓吧……</b><span>
</#attempt>

