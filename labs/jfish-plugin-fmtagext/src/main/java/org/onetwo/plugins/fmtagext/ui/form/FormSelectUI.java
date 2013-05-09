package org.onetwo.plugins.fmtagext.ui.form;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;



public class FormSelectUI extends FormFieldUI {

	private List<FormOptionUI> options = LangUtils.newArrayList();
	
	public FormSelectUI(String name, String label) {
		this(null, name, label, FmUIComponent.asProperty(name));
	}
	public FormSelectUI(FormUI parent, String name, String label, String value) {
		super(parent, "ui-form-select");
		this.label = label;
		this.name = name;
		this.value = value;
	}
	

	public List<FormOptionUI> getOptions(Object viewValue) {
		Object val = getUivaluer().getUIValue(viewValue);
		List<FormOptionUI> opts = createOptions(viewValue);
		for(FormOptionUI opt : opts){
			if(opt.getValue().equals(val)){
				opt.setSelected(true);
			}
		}
		return opts;
	}
	
	protected List<FormOptionUI> createOptions(Object viewValue){
		return options;
	}
	
	public FormOptionUI addOption(String label, String value){
		FormOptionUI opt = new FormOptionUI(this);
		opt.setLabel(label);
		opt.setValue(value);
		this.options.add(opt);
		return opt;
	}
	
	public void addOption(FormOptionUI opt){
		opt.setParent(this);
		this.options.add(opt);
	}
	
	public void addOptions(Map<String, ?> options){
		for(Entry<String, ?> entry : ((Map<String, ?>)options).entrySet()){
			addOption(entry.getKey(), entry.getValue().toString());
		}
	}

	public static class FormOptionUI extends FmUIComponent {

		private boolean selected;
		
		public FormOptionUI(FormSelectUI parent) {
			super(parent, "");
		}
		
		public FormOptionUI(FormSelectUI parent, String label, String value){
			super(parent, "");
			this.setLabel(label);
			this.setValue(value);
		}

		@Override
		public String getComponentTemplate() {
			throw new UnsupportedOperationException();
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
	}
}
