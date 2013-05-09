
<#assign page = __this__.data/>
<#assign __footui__ = __this__.component/>

<div class="pages">
	<#if page.isHasPre()>
		<a href="${__footui__.buildPageLink(1)}" style="background:none">首页</a>
		<a href="${__footui__.buildPageLink(page.prePage)}" style=" background:none">上一页</a>
	</#if>
	<#assign pageClass=""/>
	<#list page.startPage..page.endPage as pageNumber>
		<#if page.pageNo==pageNumber>
			<#assign pageClass="class='tabOn' style='font-size:18px;color:red;'"/>
		<#else>
			<#assign pageClass="style='font-size:12px;'"/>
		</#if>
		<a href="${__footui__.buildPageLink(pageNumber)}" ${pageClass}>${pageNumber}</a>&nbsp;
	</#list>
	<#if page.isHasNext()>
		<a href="${__footui__.buildPageLink(page.nextPage)}" style=" background:none">下一页</a>
		<a href="${__footui__.buildPageLink(page.totalPages)}" style=" background:none">尾页</a> 
	</#if>
当前第 ${page.pageNo} 页 ，一共 ${page.totalPages} 页，有 ${page.totalCount} 条数据
 </div>