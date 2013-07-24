<#assign __iteratorList = __this__.component.getUIValue(__this__.data)/>
<#if !(__iteratorList??) || __iteratorList.size() <= 0>
 <tr class="page-no-datas"><td colspan="${__this__.component.parent.colspan}" style="text-align:center">没有数据</td></tr>
</#if>

<#list __iteratorList as __currentEntity__>
	<tr>
	<#list row.columns as field>
		<td <@helper.td_attributes field/> ><#t>
			<#if field.hasContainer()>
				<@jfishui component=field.container data=__currentEntity__/>
			<#else>
				${field.getUIValue(__currentEntity__)}
			</#if>
		</td>
	</#list>
	</tr>
</#list>