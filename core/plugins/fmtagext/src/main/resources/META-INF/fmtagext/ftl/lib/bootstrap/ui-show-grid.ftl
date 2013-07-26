<#import "[fmtagext]/lib/ui-helper.ftl" as helper>

<#assign __dg__=__this__.component/>

<#if __dg__.title?has_content>
<div class="page-header">
	<h2>${__dg__.title}</h2>
</div>
</#if>
	
<#if __dg__.toolbar??>
<div class="well">
	<@jfishui component=__dg__.toolbar data=__this__.data/>
</div>
</#if>

<table border="0" cellspacing="0"  class="table table-bordered table-striped">
<#t>
	
	<#-- table title 
	<@table.renderTitle __dg__/><#t>
	-->	
	
	<#-- table row -->	
	<#if __dg__.rows??>
		<#list __dg__.rows as row>
			<@jfishui component=row data=__this__.data/>
		</#list>
		
	<#else>
		没有定义表格字段。
	</#if>
	
	
</table>

<#if __dg__.footer??>
<@jfishui component=__dg__.footer data=__this__.data/>
</#if>
	


