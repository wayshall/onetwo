<@helper.show_form_field __this__>
<textarea <@helper.form_field_attributes __this__.component/> rows="${__this__.component.rows!'10'}" cols="${__this__.component.cols}">
${helper.getUIValue(__this__)}<#rt/></textarea>
</@helper.show_form_field>