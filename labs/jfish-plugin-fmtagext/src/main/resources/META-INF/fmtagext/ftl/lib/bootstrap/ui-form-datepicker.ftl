<@helper.show_form_field __this__>
<input <@helper.form_field_attributes __this__.component/> type="text" value="${helper.getUIValue(__this__)}" onclick="WdatePicker({dateFmt:'${__this__.component.valueFormat}'})" readOnly/>
</@helper.show_form_field>