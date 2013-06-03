
<#macro formStart dg class="">
<form id="${dg.formName}" name="${dg.formName}" action="${dg.action?default("")}" method="${dg.actualFormMethod}" class="${class}">
<input name="_method" value="${dg.formMethod}" type="hidden"/>
</#macro>

 
<#macro formClose __dg__>
		
	</form>
		<script>
		
		function printTable(page){
			if(!page)
				page = "${siteConfig.baseURL}/print.jsp";
      		window.open(page,'print', 'toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
		}
		
		jQuery("#${__dg__.formName}").initDatagrid();
		
	<#if __dg__.ajax>
		<#assign ajaxInstName = __dg__.ajaxInstName/>
		var ${ajaxInstName} = new AjaxAnywhere();
		${ajaxInstName}.id = "${ajaxInstName}";
		${ajaxInstName}.formName = "${__dg__.formName}";
		${ajaxInstName}.bindById();
		
		    ref_All = false;
		    ${ajaxInstName}.getZonesToReload = function (url){
		        if (ref_All)
		            return "document.all";
		
		        return "${__dg__.zoneName}";
		    };
		    
		    ${ajaxInstName}.showLoadingMessage = function(){
		    	jQuery('#AA_${ajaxInstName}_loading_div').css('display', 'block');
		    };
		    
		    ${ajaxInstName}.onAfterResponseProcessing = function(){
		    };
		    
		    ${ajaxInstName}.handleHttpErrorCode = function(code){
			    alert("request error!");
		    };
		    
		    if(window.page_loadCompleted){
		    	window.page_loadCompleted();
		    }
		    
		    if(window.${__dg__.name}_loadCompleted){
		    	window.${__dg__.name}_loadCompleted();
		    }
		    
	</#if>
		
		</script>
</#macro>

<#macro checkbox name, value, id="", class="">
	<input id="${id?default("id_"+name)}" name="${name}" type="checkbox" value="${value}" class="${class}"/>
</#macro>

<#macro pagefoot __dg__>
	<#assign formName = "#${__dg__.formName?default('')}"/>
	<#assign pageNo = "${__dg__.dataSource}.pageNo"/>
	<#local page = __dg__.page/>
	<#assign ajaxInstName = 'null'/>
	<#if __dg__.ajax>
		<#assign ajaxInstName = '${__dg__.ajaxInstName}'/>
	</#if>
	当前第 ${page.pageNo} 页 ，一共 ${page.totalPages} 页，有 ${page.totalCount} 条数据
		<#if page.isHasPre()>
			<a href="javascript:Common.jumpPage('${formName}', 1, ${ajaxInstName})">首页</a>
			<a href="javascript:Common.jumpPage('${formName}', ${page.prePage}, ${ajaxInstName})">前一页</a>
		</#if>
		<#if page.isHasNext()>
			<a href="javascript:Common.jumpPage('${formName}', ${page.nextPage}, ${ajaxInstName})">下一页</a>
			<a href="javascript:Common.jumpPage('${formName}', ${page.totalPages}, ${ajaxInstName})">尾页</a>
		</#if>
			&nbsp;&nbsp;跳到<input type="text" id="pageNo" size="2" name="${pageNo}" value="${page.pageNo}"/>
			<a href="javascript:Common.jumpPage('${formName}', null, ${ajaxInstName})">GO</a>
</#macro>

<#macro pagefootByJsNumber __dg__>
	<#assign formName = "#${__dg__.formName?default('')}"/>
	<#assign pageNo = "${__dg__.dataSource}.pageNo"/>
	<#local page = "${__dg__.page}"/>
	<#assign ajaxInstName = 'null'/>
	<#if isAjax>
		<#assign ajaxInstName = '${__dg__.ajaxInstName}'/>
	</#if>
	
	<div class="pages">
	当前第 ${page.pageNo} 页 ，一共 ${page.totalPages} 页，有 ${page.totalCount} 条数据
		<#if page.isHasPre()>
			<a href="javascript:Common.jumpPage('${formName}', 1, ${ajaxInstName})" style=" background:none"><img src="${siteConfig.baseURL}/business/images/page/page_top.gif" /></a>
			<a href="javascript:Common.jumpPage('${formName}', ${page.prePage}, ${ajaxInstName})" style=" background:none"><img src="${siteConfig.baseURL}/business/images/page/page_previous.gif" /></a>
		</#if>
		<#list page.startPage..page.endPage as pageNumber>
			<a href="javascript:Common.jumpPage('${formName}', ${pageNumber}, ${ajaxInstName})" <#if page.pageNo==pageNumber>class="${parameters.currentPageClass?default('currentPageClass')}"<#else>class="${parameters.nonCurrentPageClass?default('nonCurrentPageClass')}"</#if>>${pageNumber}</a><#rt/>
		</#list>
		<#if page.isHasNext()>
			<a href="javascript:Common.jumpPage('${formName}', ${page.nextPage}, ${ajaxInstName})" style=" background:none"><img src="${siteConfig.baseURL}/business/images/page/page_next.gif" /></a>
			<a href="javascript:Common.jumpPage('${formName}', ${page.totalPages}, ${ajaxInstName})" style=" background:none"><img src="${siteConfig.baseURL}/business/images/page/page_last.gif" /></a> 
		</#if>
			&nbsp;&nbsp;跳到<input type="text" id="pageNo" size="2" name="${pageNo}" value="${page.pageNo}"/>
			<a href="javascript:Common.jumpPage('${formName}', null, ${ajaxInstName})">GO</a>
	 </div>
</#macro>

<#macro pagefootByNumber __dg__>
	<#assign formName = __dg__.formName?default('')/>
	<#assign pageNo = __dg__.dataSource + ".pageNo"/>
	<#local page = __dg__.page/>
	<#assign ajaxInstName = 'null'/>
	<#if __dg__.ajax>
		<#assign ajaxInstName = '${__dg__.ajaxInstName}'/>
	</#if>
	
	<div class="pages">
		<#if page.isHasPre()>
			<a href="${pageLink(__dg__.action, 1)}" style="background:none">首页</a>
			<a href="${pageLink(__dg__.action, page.prePage)}" style=" background:none">上一页</a>
		</#if>
		<#local pageClass=""/>
		<#list page.startPage..page.endPage as pageNumber>
			<#if page.pageNo==pageNumber>
				<#local pageClass="class='tabOn' style='font-size:18px;color:red;'"/>
			<#else>
				<#local pageClass="style='font-size:12px;'"/>
			</#if>
			<a href="${pageLink(__dg__.action, pageNumber)}" ${pageClass}>${pageNumber}</a>&nbsp;
		</#list>
		<#if page.isHasNext()>
			<a href="${pageLink(__dg__.action, page.nextPage)}" style=" background:none">下一页</a>
			<a href="${pageLink(__dg__.action, page.totalPages)}" style=" background:none">尾页</a> 
		</#if>
	当前第 ${page.pageNo} 页 ，一共 ${page.totalPages} 页，有 ${page.totalCount} 条数据
	 </div>
</#macro>

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

<#macro renderTitle __dg__>
	<#if __dg__.title??>
		<thead>
			<tr>
				<th colspan="${__dg__.colspan}">${__dg__.title}</th>
			</tr>
		</thread>
	</#if>
</#macro>
	
<#macro mainAttrs e>
	<#if e.id??>
	 id="${e.id?html}"<#rt/>
	</#if>
	<#if e.name??>
	 name="${e.name?html}"<#rt/>
	</#if>
</#macro>

<#macro attrs e>
	<#if e.cssStyle??>
	 style="${e.cssStyle?html}"<#rt/>
	</#if>
	<#if e.cssClass??>
	 class="${e.cssClass?html}"<#rt/>
	</#if>
	<#if e.onclick??>
	 onclick="${e.onclick?html}"<#rt/>
	</#if>
	<#if e.attributes??>
	 ${e.attributes?html}<#rt/>
	</#if>
</#macro>


<#macro dataIterator row, __dg__>

	<#if row.renderHeader>
	<thead>
		<tr>
		<#list row.fields as field>
			<th colspan="${field.colspan}" <@attrs field/>><#rt>
			<#if field.order>
				<a href="${field.appendOrderBy(__dg__.action)}">${field.label?default("")}<#if field.ordering && field.orderType==":desc">↑<#elseif field.ordering && field.orderType==":asc">↓</#if><a>
			<#else>
				${field.label?default("")}
			</#if>
			<#if field.type?has_content>
				<#if field.type=="checkbox">
					<@checkbox name="all_"+field.name value="" id="id_all_"+field.name class="dg-checkbox-all"/>
				</#if>
			</#if>
			</th>
		</#list><#t>
		</tr>
	</thead>
	</#if>
	
	<#if __dg__.page.result.size() <= 0>
	 <tr class="page-no-datas"><td colspan="${__dg__.colspan}" style="text-align:center">没有数据</td></tr>
	</#if>

	<#list __dg__.page.result as __currentEntity__>
		<tr>
			<#list row.fields as field>
				<@dataField field, __currentEntity__/><#t>
			</#list>
		</tr>
	</#list>

</#macro>


<#macro dataField field, __entity__={}>
	<#-- 变量改为__entity__，但兼容以前的代码 -->
	<#local __currentEntity__=__entity__/>
	
	<td colspan="${field.colspan}"
	<#if field.id??>
	 id="${field.id?html}"<#rt/>
	</#if>
	<#if field.name??>
	 name="${field.name?html}"<#rt/>
	</#if>
	<#if field.cssStyle??>
	 style="${field.cssStyle?html}"<#rt/>
	</#if>
	<#if field.cssClass??>
	 class="${field.cssClass?html}"<#rt/>
	</#if>
	<#if field.onclick??>
	 onclick="${field.onclick?html}"<#rt/>
	</#if>
	<#if field.attributes??>
	 ${field.attributes?html}<#rt/>
	</#if> ><#t>
	
	<#if field.autoRender>
		<#local evalue = __entity__[field.value]/>
		<#lt>${toString(evalue, field.format!"")}&nbsp;
	<#elseif field.getRender()=="checkbox">
		<#local chbValue = __entity__[field.value]?default(__entity__)/>
		<@checkbox name=field.name value=chbValue id=field.name+chbValue class="dg-checkbox-field"/>
	<#elseif field.getRender()=="button">
		<#list field.typeList as btn>
			<#if btn=="edit">
				<@h.edit_link entity=__entity__ class="btn"/>
			<#elseif btn="delete">
				<@h.delete_link entity=__entity__ class="btn"/>
			<#elseif btn="show">
				<@h.show_link entity=__entity__ class="btn"/>
			</#if>
		</#list>
	<#elseif field.getRender()=="html">
		${field.render()}
	<#else>
		${field.render()}
	</#if>
	
	</td>
</#macro>

<#macro dataRow row, dg>
	<#assign fieldHtmlElement = "td">
	<#if row.header>
		<thead>
		<#assign fieldHtmlElement = "th">
	</#if>
		<tr <@attrs row/>>
		
	<#list row.fields as field>
		<@dataField field/><#t>
	</#list>
		
	</tr><#if row.header></thead></#if>
</#macro>

<#function toString value, format>
	<#assign result = value/>
	<#assign format2 = format/> 
	<#if value?is_date>
		<#if format?has_content == false><#assign format2 = "yyyy-MM-dd HH:mm:ss"/></#if>
		<#assign result = value?string(format2)/>
	<#elseif value?is_number>
		<#if format?has_content == false><#assign format2 = "0"/></#if>
		<#assign result = value?string(format2)/>
	<#elseif value?is_boolean>
		<#assign result = value?string("true", "false")/>
	</#if>
	<#return result?html/>
</#function>
