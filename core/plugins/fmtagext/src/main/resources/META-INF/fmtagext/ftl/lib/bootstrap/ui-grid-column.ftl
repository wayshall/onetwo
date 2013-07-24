	
<td<#if field.colspan gt 1> colspan="${field.colspan}"</#if>
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
 ${field.attributesString?html}<#rt/>
><#t>

<#assign evalue = __currentEntity__[field.text]/>
<#lt>${helper.toString(evalue, field.format!"")}&nbsp;
</td>
