package org.onetwo.plugins.fmtagext.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class ViewEntry {

	private final Class<?> viewClass;
	private String title;
	
	private List<JFieldViewObject> fields = LangUtils.newArrayList();

	public ViewEntry(Class<?> viewClass) {
		super();
		this.viewClass = viewClass;
	}
	
	public ViewEntry addField(JFieldViewObject field){
		fields.add(field);
		return this;
	}

	public Class<?> getViewClass() {
		return viewClass;
	}

	public String getTitle() {
		return title;
	}

	public Collection<JFieldViewObject> getFields() {
		return fields;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void sortFields(){
		Collections.sort(fields);
	}
	
}
