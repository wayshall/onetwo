<#import "${selfConfig.templateBasePath}/lib/dg/table-utils.ftl" as table>

<#attempt>

<#if !__dg__.isPagebarOnly()>
	
	<#-- search fields -->
	<#if __dg__.searchFields.empty == false>
		<div>
		<@h.form name="searchForm">
		<#list __dg__.searchFields as field>
			${field.label}：<@h.text name="${field.name}"/>
		</#list>
				<@h.submit value="   查   询   "/> 
		</@h.form>
		</div>
	</#if>
	
<#-- table body -->	
	<@table.formStart dg=__dg__ />
	
	${__dg__.bodyContent?default("")}<#t>
	
	<table border="0" cellspacing="0"  <@table.attrs __dg__/>>
<#t>

	<#-- table toolbar -->	
	<#if __dg__.hasToolbar()>
		<thead>
			<tr style="text-align:left;">
				<th colspan="${__dg__.colspan}">
				<#list __dg__.toolbarList as bar>
					<#--
					<#if bar?starts_with("/")>
						<#include "${bar}.ftl"/>
					<#else>
						<#include "${__dg__.themeDir}/${bar}.ftl"/>
					</#if>
					-->
					<#if bar?starts_with(":")>
						<#include "${__dg__.themeDir}/${bar?substring(1)}.ftl"/>
					<#else>
						<#include "${bar}.ftl"/>
					</#if>
					
				</#list>
				</th>
			</tr>
		</thead>
	</#if>
	
	<#-- table title -->	
	<@table.renderTitle __dg__/><#t>
	
	<#-- table row -->	
	<#if __dg__.rows??>
		<#list __dg__.rows as row>
			<#if row.iterator>
				<@table.dataIterator row __dg__/>
			<#else>
				<@table.dataRow row __dg__/>
			</#if>
		</#list>
	<#else>
		没有定义表格字段。
	</#if>
	
	<#-- table pagination -->	
	<#if __dg__.isPagination() && __dg__.isPagiOnGridBottom()>
	<tr>
		<td colspan="${__dg__.colspan}">
		<#include "${__dg__.themeDir}/page-${__dg__.paginationTemplate}.ftl" />
		</td>
	</tr>
	</#if>
	
	</table>
	<@table.formClose __dg__/>
</#if>


	<#if __dg__.isPagination() && (__dg__.isPagiOuterGrid() || __dg__.isPagebarOnly())>
		<#include "${__dg__.themeDir}/page-${__dg__.paginationTemplate}.ftl" />
	</#if>

<#recover>
	<span style="color:red;"><b>有虫子爬进表格了，先抓一抓吧……</b><span>
</#attempt>

