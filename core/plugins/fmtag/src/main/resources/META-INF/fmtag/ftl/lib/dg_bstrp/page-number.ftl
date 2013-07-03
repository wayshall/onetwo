<div class="page-count">
	<@table.pagefootByNumber __dg__/>
	<#if __dg__.ajax>
	<span id="AA_${__dg__.ajaxInstName}_loading_div" class="load_info" style="display:none;"><img src="${siteConfig.baseURL}/images/loading2.gif" align="absmiddle"/>&nbsp;Loading data...</span>
	</#if>
</div>
