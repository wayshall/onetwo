package org.onetwo.plugins.fmtagext.ui.form;

import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.onetwo.plugins.fmtagext.ui.valuer.StringUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;

public class LinkUI extends FmUIComponent{

	protected String target;
	
	private List<LinkParam> paramerters;

	public LinkUI() {
		this(null, "", "");
	}
	public LinkUI(String label, String href) {
		this(null, label, href);
	}
	public LinkUI(FmUIComponent parent, String label, String href) {
		super(parent, "ui-link");
		this.label = label;
		this.value = href;
	}

	public String getHref(){
		return label;
	}
	
	public String getLabel(){
		return label;
	}

	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}

	public LinkUI addParamNames(String... names){
		for(String name : names)
			addParamName(name);
		return this;
	}
	
	public LinkUI addParamName(String name){
		addParam(name, asProperty(name));
		return this;
	}
	
	public LinkUI addParam(String name, String value){
		return addParam(name, value, false);
	}
	
	public LinkUI addParam(String name, String value, boolean required){
		if(this.paramerters==null){
			this.paramerters = LangUtils.newArrayList();
		}
		LinkParam p = new LinkParam(name, value, required);
		this.paramerters.add(p);
		return this;
	}
	
	public List<LinkParam> getParamerters() {
		return paramerters;
	}


	protected UIValuer<?> createUIValuer(){
		return new StringUIValuer(this, value, valueFormat){
			@Override
			public String getUIValue(Object viewValue) {
				String url = super.getUIValue(viewValue);
				if(LangUtils.isEmpty(getParamerters()))
					return url;
				StringBuilder pstr = new StringBuilder();
				int index = 0;
				StringUIValuer uivaluer = null;
				for(LinkParam lp : getParamerters()){
					uivaluer = new StringUIValuer(null, lp.getValue());
					String val = uivaluer.getUIValue(viewValue);
					if(StringUtils.isBlank(val)){
						if(lp.isRequired()){
							throw new BaseException("the link param can not be null : " + lp.getName());
						}else{
							continue;
						}
					}
					if(index!=0){
						pstr.append("&");
					}
					pstr.append(lp.getName()).append("=").append(val);
					index++;
				}
				if(pstr.length()==0)
					return url;
				if(!url.contains("?")){
					url += "?";
				}
				url += pstr;
				return url;
			}
		};
	}
	
	private static class LinkParam {
		private String name;
		private String value;
		private boolean required;
		public LinkParam(String name, String value, boolean required) {
			super();
			this.name = name;
			this.value = value;
			this.required = required;
		}
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
		public boolean isRequired() {
			return required;
		}
		
	}
}
