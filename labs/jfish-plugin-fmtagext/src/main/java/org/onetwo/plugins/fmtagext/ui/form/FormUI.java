package org.onetwo.plugins.fmtagext.ui.form;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.plugins.fmtagext.ui.ButtonUI;
import org.onetwo.plugins.fmtagext.ui.ContainerUIComponent;
import org.onetwo.plugins.fmtagext.ui.DefaultContainerUIComponent;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.valuer.ExprUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.SimpleComponentUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.StringUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;

public class FormUI extends FmUIComponent {

	public static enum FormMethod {
		post,
		get,
		put,
		delete
	}
	private final List<FormFieldUI> fields = LangUtils.newArrayList();
	
	private ExprUIValuer<?> actionValuer = new StringUIValuer(this);
	private FormMethod method = FormMethod.post;
	private JFishList<ButtonUI> buttons = JFishList.create();
	private ContainerUIComponent children = new DefaultContainerUIComponent("");
	
	public FormUI(String title){
		this("dataForm", title);
	}
	
	public FormUI(String name, String title){
		this(null, name, title);
	}
	
	public FormUI(FmUIComponent parent, String name, String title) {
		super(parent, "ui-form");
		this.name = name;
		this.title = title;
//		this.buttons.add(FormButtonUI.ok());
	}
	
	public FormUI addTextInputs(String... fields){
		Map<String, String> fieldMap = CUtils.arrayIntoMap(new LinkedHashMap<String, String>(), (Object[])fields);
		return addTextInputs(fieldMap);
	}
	
	public FormUI addTextInputs(Map<String, String> fields){
		for(Entry<String, String> entry : fields.entrySet()){
			addTextInput(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public FormFieldUI getField(String name){
		for(FormFieldUI field : fields){
			if(name.equals(field.getName()))
				return field;
		}
		return null;
	}
	
	public <T> T getFieldAs(String name, Class<T> formFieldClass){
		FormFieldUI field = getField(name);
		if(field==null)
			throw new JFishException("no field found : " + name);
		return formFieldClass.cast(field);
	}
	
	public FormTextInputUI addTextInput(String name, String text){
		FormTextInputUI input = new FormTextInputUI(this, name, text, FmUIComponent.asProperty(name));
		addFormField(input);
		return input;
	}
	
	public FormHiddenUI addHidden(String name, String value){
		FormHiddenUI hidden = new FormHiddenUI(this, name, value);
		addFormField(hidden);
		return hidden;
	}
	
	public FormUI addFormField(FormFieldUI field){
		field.setParent(this);
		this.fields.add(field);
		return this;
	}

	public List<FormFieldUI> getFields() {
		return fields;
	}
	
	public boolean isRenderFormBody(){
		return !fields.isEmpty();
	}

	public void setAction(String action) {
		this.actionValuer.setValue(action);
	}

	public String getMethodString() {
		return method.toString();
	}

	public FormMethod getMethod() {
		return method;
	}

	public void setMethod(FormMethod method) {
		this.method = method;
	}

	public FormUI setFormAction(String method, String action) {
		this.method = FormMethod.valueOf(method);
		this.setAction(action);
		return this;
	}

	public List<ButtonUI> getButtons() {
		return buttons;
	}

	public FormUI addButtons(ButtonUI... buttons) {
		for(ButtonUI btn : buttons){
			if(btn==null)
				continue;
			btn.setParent(this);
			this.buttons.add(btn);
		}
		return this;
	}

	public UIValuer<?> getActionValuer() {
		return actionValuer;
	}

	public ContainerUIComponent getChildren() {
		return children;
	}

	public void setChildren(ContainerUIComponent children) {
		this.children = children;
	}

	@Override
	protected UIValuer<?> createUIValuer(String value, String valueFormat) {
		return new SimpleComponentUIValuer(this, value);
	}
	
//	public void setTargetAction(Class<? extends BaseController<?>> controllerClass, String methodName, Object...params){
//		String url = JFishUtils.getControllerPath(controllerClass, methodName, params);
//		setAction(url);
//	}
	
	

}
