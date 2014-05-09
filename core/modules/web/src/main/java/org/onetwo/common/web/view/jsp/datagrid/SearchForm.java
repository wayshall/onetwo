package org.onetwo.common.web.view.jsp.datagrid;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;

public class SearchForm {
	private List<FieldTagBean> fields;
	
	public void addField(FieldTagBean field){
		if(this.fields==null)
			this.fields = LangUtils.newArrayList();
		this.fields.add(field);
	}

	public List<FieldTagBean> getFields() {
		if(this.fields==null)
			return Collections.EMPTY_LIST;
		return fields;
	}
	
	
}
