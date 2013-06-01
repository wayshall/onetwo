<@helper.show_form_field __this__>
<select <@helper.form_field_attributes __this__.component/> >
<#list __this__.component.getOptions(__this__.data) as opt>
	<option value="${opt.getUIValue(__this__.data)}" <#if opt.selected>selected</#if> >${opt.getUILabel(__this__.data)}</option>
</#list>
</select>
</@helper.show_form_field>