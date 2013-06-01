<#import "${selfConfig.templateBasePath}/lib/dg/table-utils.ftl" as table>

<#assign formName = __dg__.formName?default('')/>
<#assign pageNo = __dg__.dataSource + ".pageNo"/>
<#assign page = __dg__.page/>
<#assign ajaxInstName = 'null'/>
<#if __dg__.ajax>
	<#assign ajaxInstName = '${__dg__.ajaxInstName}'/>
</#if>

<#if page.isHasPre()>
	<a href="${table.pageLink(__dg__.action, 1)}" style="background:none">首页</a>
	<a href="${table.pageLink(__dg__.action, page.prePage)}" style=" background:none">上一页</a>
</#if>
<#assign pageClass=""/>
<#list page.startPage..page.endPage as pageNumber>
	<#if page.pageNo==pageNumber>
		<#assign pageClass="class='cur'"/>
	<#else>
		<#assign pageClass="style='font-size:12px;'"/>
	</#if>
	<a href="${table.pageLink(__dg__.action, pageNumber)}" ${pageClass}>${pageNumber}</a>&nbsp;
</#list>
<#if page.isHasNext()>
	<a href="${table.pageLink(__dg__.action, page.nextPage)}" style=" background:none">下一页</a>
	<a href="${table.pageLink(__dg__.action, page.totalPages)}" style=" background:none">尾页</a> 
</#if>