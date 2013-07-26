<#assign __comp__=__this__.component/>
<input id="${__comp__.id}" name="${__comp__.name}" class="btn btn-large btn-primary ${__comp__.cssClass}" type="button" 
value="${helper.getUILabel(__this__)}" data-confirm="${__comp__.dataConfirm}" 
<#if __comp__.dataParams??>data-params="${__comp__.dataParamJson}"</#if>
 <#if __comp__.locationHref>onclick="location.href='${helper.getUIValue(__this__)}'"<#else>action="${helper.getUIValue(__this__)}"</#if>
/>