<#assign evalue = helper.getUIValue(__this__)/>
${__this__.component.label}<input <@helper.form_field_attributes __this__.component/> type="checkbox" value="${evalue}" />