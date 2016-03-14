package org.onetwo.plugins.fmtagext.directive;

import org.onetwo.common.jfishdbm.mapping.JFishMappedField;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.ftl.DirectiveRender;
import org.onetwo.plugins.fmtag.directive.AbstractJFieldView;
import org.onetwo.plugins.fmtagext.FmtagextUtils;

public class JFieldDO extends AbstractJFieldView {
	
	public static JFieldDO create(JEntryDO entryDO, JFishMappedField field){
		JFieldDO jf = new JFieldDO(entryDO, field.getName());
		FmtagextUtils.setJFieldViewMeta(jf, field);
		return jf;
	}
	
	public static final String TEMPATE_PREFIX = ":";

	private JEntryDO entry;
	private String formTag;
//	private String theme = "";
	private DirectiveRender render;
//	private String format = "";
	
//	private int showOrder;
//	private boolean showable = true;

	//for input tag
	private String inputType;
	
	//for select tag
	private Object dataProvider;
	private String dataKey;
	private String dataValue;
	
	public JFieldDO(JEntryDO entryDO){
		this.entry = entryDO;
		this.setFormTag("input_text");
	}
	
	public JFieldDO(JEntryDO entryDO, String name){
		this(entryDO);
		this.name = name;
	}
	
	
	final public void setFormTag(String formTag) {
		String[] strs = StringUtils.split(formTag, "_");
		this.formTag = strs[0];
		if(strs.length==1){
			this.inputType = null;
		}else if(strs.length==2 && this.isInput()){
			this.inputType = strs[1];
		}
	}

	public boolean isAutoRender() {
		return this.render==null;
	}

	public boolean isTemplateField() {
		return this.formTag.startsWith(TEMPATE_PREFIX);
	}

	public String getTemplate() {
		return this.formTag.substring(TEMPATE_PREFIX.length());
	}
	
	public String getFormTag() {
		return formTag;
	}

	public void render(){
		this.render.render();
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	
	public boolean isHidden(){
		return this.formTag.equals(HtmlType.input.toString()) && HtmlType.hidden.toString().equals(this.inputType);
	}
	
	public boolean isInput(){
		return this.formTag.equals(HtmlType.input.toString());
	}
	
	public boolean isDatepicker(){
		return this.formTag.equals(HtmlType.datepicker.toString());
	}

	public boolean isSelect(){
		return this.formTag.equals(HtmlType.select.toString());
	}


	public void setRender(DirectiveRender render) {
		this.render = render;
	}

	public Object getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(Object dataProvider) {
		this.dataProvider = dataProvider;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}


	public JEntryDO getEntry() {
		return entry;
	}
	
	public boolean isShowable(){
		return canShow(entry.getType());
	}
}
