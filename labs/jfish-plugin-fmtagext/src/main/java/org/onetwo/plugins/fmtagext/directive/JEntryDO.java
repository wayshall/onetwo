package org.onetwo.plugins.fmtagext.directive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.ftl.directive.HtmlElement;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtag.JFieldShowable;
import org.onetwo.plugins.fmtag.annotation.JEntryViewMeta;

public class JEntryDO extends HtmlElement {
	
	public static JEntryDO create(JFishMappedEntry entry){
		JEntryDO entryDo = new JEntryDO();
		if(entry!=null){
			JEntryViewMeta meta = entry.getAnnotationInfo().getAnnotation(JEntryViewMeta.class);
			if(meta!=null){
				entryDo.setLabel(meta.label());
			}
		}
		return entryDo;
	}
	
	private String formMethod;
	private String formAction;
	private String formButtons;
	private List<JFieldDO> fields = new ArrayList<JFieldDO>();
	private String bodyContent;
	private JFieldShowable type;
	private boolean form;
//	private String theme;
//	private Object entity;
	
//	private String idName;
//	private Object idValue;

	public void addField(JFieldDO field){
		fields.add(field);
	}
	
	public JFieldDO getField(String name){
		for(JFieldDO jf : fields){
			if(jf.getName().equals(name))
				return jf;
		}
		return null;
	}
	
	public void sortFields(){
		Collections.sort(fields);
	}

	public Collection<JFieldDO> getFields() {
		return fields;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public String getFormMethod() {
		return formMethod;
	}

	public void setFormMethod(String formMethod) {
		this.formMethod = formMethod;
	}

	public String getFormAction() {
		return formAction;
	}

	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	public boolean isCreate() {
		return JFieldShowable.create==type;
	}

	public boolean isUpdate() {
		return JFieldShowable.update==type;
	}

	public boolean isShow() {
		return JFieldShowable.show==type;
	}

	/*public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}*/

	/*public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public Object getIdValue() {
		return idValue;
	}

	public void setIdValue(Object idValue) {
		this.idValue = idValue;
	}*/

	public JFieldShowable getType() {
		return type;
	}

	public void setType(JFieldShowable type) {
		this.type = type;
	}

	public String getFormButtons() {
		return formButtons;
	}

	public List<String> getFormButtonList() {
		return LangUtils.asList(StringUtils.split(formButtons, "|"));
	}

	public void setFormButtons(String formButtons) {
		this.formButtons = formButtons;
	}

	public boolean isForm() {
		return form;
	}

	public void setForm(boolean form) {
		this.form = form;
	}
	
}
