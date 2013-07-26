package org.onetwo.plugins.fmtagext.ui;

import java.util.Map;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtagext.ui.form.FormUI.FormMethod;

public class ButtonUI extends FmUIComponent {

	protected FormMethod dataMethod;
//	protected String dataAction;
	protected String dataConfirm = "false";
	protected Map<String, Object> dataParams;


	public ButtonUI(String template) {
		super(template);
	}
	
	public ButtonUI setMethodAction(String method, String action){
		this.dataMethod = FormMethod.valueOf(method);
		this.value = action;
		return this;
	}
	
	public FormMethod getDataMethod() {
		return dataMethod;
	}
	public void setDataMethod(FormMethod dataMethod) {
		this.dataMethod = dataMethod;
	}
	public String getDataAction() {
		return this.getValue();
	}
	public void setDataAction(String dataAction) {
		this.setValue(dataAction);
	}
	
	public Map<String, Object> getDataParams() {
		if(dataParams==null)
			this.dataParams = LangUtils.newHashMap();
		return dataParams;
	}
	public void setDataParams(Map<String, Object> dataParams) {
		this.dataParams = dataParams;
	}
	@SuppressWarnings("unchecked")
	public ButtonUI addParams(Object... dataParams) {
		this.dataParams = LangUtils.asMap(dataParams);
		return this;
	}
	public String getDataParamJson(){
		return JsonMapper.IGNORE_EMPTY.toJson(dataParams);
	}
	public String getDataConfirm() {
		return dataConfirm;
	}
	public void setDataConfirm(String dataConfirm) {
		this.dataConfirm = dataConfirm;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
