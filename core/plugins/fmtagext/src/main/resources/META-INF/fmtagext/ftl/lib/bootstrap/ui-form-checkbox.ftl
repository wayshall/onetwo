<@helper.show_form_field __this__>
<#assign evalue = helper.getUIValue(__this__)/>
${__this__.component.label}<input <@helper.form_field_attributes __this__.component/> type="checkbox" value="${evalue}" />
</@helper.show_form_field>
