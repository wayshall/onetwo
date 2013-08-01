
<#assign form = JspTaglibs["http://www.springframework.org/tags/form"]/>

<#macro showGrid jentryDO entryData>
	<table class="table table-bordered table-striped">
			<thead>
				<tr>
				<th colspan="2">
				<h2>${jentryDO.label}</h2>详细信息
				<a class="btn pull-right" href="${siteConfig.baseURL}${urlHelper.listAction}">返   回 </a>
				<a class="btn pull-right" href="${siteConfig.baseURL}${urlHelper.editAction(entryData.entity)}">修   改 </a>
				</th>
				</tr>
			</thead>
		<tbody>
		<#list jentryDO.fields as jfield>
			<@showGridField field=jfield entryData=entryData/>
		</#list>
		
		</tbody>
	</table>
</#macro>

<#macro showGridField field entryData={}>
	
	<#if field.templateField>
		<#include "${field.template}.ftl"/>
	<#else>
		<#local evalue = entryData.entity[field.name]/>
		<tr>
			<th class="span3">${field.label}</th>
			<td>
			<#if field.autoRender==false>
				${field.render()}
			<#else>
				${jhelper.toString(evalue, field.format)}
			</#if>
			</td>
		</tr>
	</#if>
</#macro> 


<#macro wrapHtmlField entry field entryData={}>
	<#if entryData.entity>
		<#local bstatus = springMacroRequestContext.getBindStatus(entry.name+"."+field.name)/>
		<div class="control-group <#if bstatus.error>error</#if>">
	<#else>
		<div class="control-group">
	</#if>
	  <label class="control-label" for="${field.name}">${field.label}：</label>
      <div class="controls">
		<#nested>
      </div>
    </div>
</#macro> 

<#macro formField entry field entryData={}>
	<#local evalue = (entryData.entity[field.name])!""/>
	
	
	<#if field.hidden>
		<@hidden id="${field.id}" name="${field.name}" value=evalue />
	<#else>
		<@wrapHtmlField entry=entry field=field entryData=entryData>
			<#if field.input>
				<@text id="${field.id}" name="${field.name}" value=evalue />
		    	<@form.errors path="${field.name}" cssClass="help-inline error"/>
			<#elseif field.select>
				<@select id="${field.id}" name="${field.name}" value=evalue datas=field.dataProvider dataKey=field.dataKey dataValue=field.dataValue/>
			<#elseif field.datepicker>
				<@datepicker id="${field.id}" name="${field.name}" value="${toString(evalue, field.format)}" format="${field.format}"/>
			<#else>
				<#lt><${field.formTag} class="input-xlarge" <@html_attributes field/>>${toString(evalue, field.format)}</${field.formTag}>
		    	<@form.errors path="${field.name}" cssClass="help-inline error"/>
			</#if>
		</@wrapHtmlField>
	</#if>
	
</#macro> 


<#macro hidden id="" name="" value="">
<input id="${id}" name="${name}" type="hidden" value="${value}" />
</#macro>

<#macro text id="" name="" value="" class="input-xlarge" onclick="" attributes="">
<input id="${id}" name="${name}" type="text" value="${value}" class="${class}" onclick="${onclick}" ${attributes}"/>
<@form.errors path="${name}" cssClass="help-inline error"/>
</#macro>

<#macro datepicker id="" name="" value="" format="yyyy-MM-dd HH:mm:ss" class="Wdate input-xlarge" attributes="">
<input id="${id}" name="${name}" type="text" value="${value}" class="${class}" onclick="WdatePicker({dateFmt:'${format}'})" ${attributes} readOnly/>
<@form.errors path="${name}" cssClass="help-inline error"/>
</#macro>

<#macro select id="" name="" value="" datas=[] dataKey="key" dataValue="value" class="" attributes="">
<select id="${id}" name="${name}" class="${class}">
	<option value="">请选择……</option>
	<#if datas?is_sequence>
		<#list datas as data>
		<option value="${data[dataValue]}" <#if value==data[dataValue]>selected</#if>>${data[dataKey]}</option>
		</#list>
	<#elseif datas?is_hash>
		<#list datas?keys as key>
		<option value="${datas[key]}" <#if value==datas[key]>selected</#if>>${key}</option>
		</#list>
	<#else>
		<#list datas?keys as key>
		<option value="${datas(key)}" <#if value==datas(key)>selected</#if>>${key}</option>
		</#list>
	</#if>
</select>
<@form.errors path="${name}" cssClass="help-inline error"/>
</#macro>