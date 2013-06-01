<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>

<#macro td_attributes field>
	<#if field.colspan gt 1> colspan="${field.colspan}"</#if>
	<#if field.id??> id="${field.id?html}"</#if>
	<#if field.name??> name="${field.name?html}"</#if>
	<#if field.cssStyle??> style="${field.cssStyle?html}"</#if>
	<#if field.cssClass??> class="${field.cssClass?html}"</#if>
	<#if field.onclick??> onclick="${field.onclick?html}"</#if>
	${field.attributesString?html}<#t>
</#macro>

<#macro html_attributes field>
	<#if field.id??> id="${field.id?html}"</#if>
	<#if field.name??> name="${field.name?html}"</#if>
	<#if field.cssStyle??> style="${field.cssStyle?html}"</#if>
	<#if field.cssClass??> class="${field.cssClass?html}"</#if>
	<#if field.onclick??> onclick="${field.onclick?html}"</#if>
	${field.attributesString?html}<#t>
</#macro>

<#macro form_field_attributes field>
	<#if field.id??> id="${field.id?html}"</#if>
	<#if field.name??> name="${field.name?html}"</#if>
	<#if field.cssStyle??> style="${field.cssStyle?html}"</#if>
	<#if field.cssClass??> class="${field.cssClass?html}"</#if>
	<#if field.onclick??> onclick="${field.onclick?html}"</#if>
	${field.attributesString?html}<#t>
</#macro>

<#macro show_form_field datafield>
<#local field=datafield.component/>
<#if datafield.data>
	<#local __component__=datafield.component.parent/>
	<#if springMacroRequestContext.getErrors(__component__.name)?? && springMacroRequestContext.getBindStatus(__component__.name+"."+field.name).error>
		<div class="control-group error">
	<#else>
		<div class="control-group">
	</#if>
<#else>
	<div class="control-group">
</#if>

  <label class="control-label" for="${datafield.component.name}">${helper.getUILabel(datafield)}</label>
  <div class="controls">
	<#nested/>
	<@form.errors path="${field.name}" cssClass="help-inline error"/>
  </div>
</div>
</#macro>

<#function getUIValue dataComponent>
	<#assign result = ""/>
	<#assign result = dataComponent.component.getUIValue(dataComponent.data)/>
	<#return result?html/>
</#function>

<#function getUILabel dataComponent>
	<#assign result = ""/>
	<#assign result = dataComponent.component.getUILabel(dataComponent.data)/>
	<#return result?html/>
</#function>



<#function toString value, format>
	<#assign result = ""/>
	<#assign format2 = format/> 
	<#if value?is_date>
		<#if format?has_content == false><#assign format2 = "yyyy-MM-dd HH:mm:ss"/></#if>
		<#assign result = value?string(format2)/>
	<#elseif value?is_number>
		<#if format?has_content == false><#assign format2 = "0"/></#if>
		<#assign result = value?string(format2)/>
	<#elseif value?is_boolean>
		<#assign result = value?string("true", "false")/>
	<#else>
		<#assign result = value?html/>
	</#if>
	<#return result/>
</#function>

<#function pageLink action numb=1>
	<#if numb==1>
		<#return action/>
	</#if>
	<#local result = action/>
	<#if action?index_of("?")!=-1>
		<#local result = action + "&pageNo="+numb/>
	<#else>
		<#local result = action + "?pageNo="+numb/>
	</#if>
	<#return result/>
</#function>


<#function link action params...>
	<#if params?size == 0>
		<#return action/>
	</#if>
	<#local result = action/>
	<#if action?index_of("?")!=-1>
		<#local result = action + "&pageNo="+numb/>
	<#else>
		<#local result = action + "?"/>
	</#if>
	<#local index=0/>
	<#list params as p>
		<#if index%2==0>
			<#local result = result + p + "="/>
		<#else>
			<#local result = result + p/>
		</#if>
		<#local index=index+1/>
	</#list>
	<#return result/>
</#function>