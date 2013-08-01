package org.onetwo.plugins.fmtagext.ui;

import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.ui.valuer.StringUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;



abstract public class FmUIComponent extends AbstractHtmlElement implements UIComponent, ShowableUI {


	public static final Expression VALUE_EXPR = Expression.BRACE;
	public static final String PROPERTY_MARK = ":";

	public static String trimPropertyMark(String property){
		if(isProperty(property)){
			return property.substring(PROPERTY_MARK.length());
		}else{
			return property;
		}
	}
	public static boolean isProperty(String property){
		return property!=null && property.startsWith(PROPERTY_MARK);
	}
	public static boolean isExpresstion(String text){
		return StringUtils.isNotBlank(text) && VALUE_EXPR.isExpresstion(text);
	}
	public static String asProperty(String property){
		if(isProperty(property))
			return property;
		return PROPERTY_MARK + property;
	}
	
	private UIComponent parent;
	
	private String template;
	private String theme = "bootstrap";
	protected String label;
	protected String value;
	protected String valueFormat;

	protected UIValuer<?> uivaluer;
	protected UIValuer<?> uilabeler;

	public FmUIComponent(FmUIComponent parent, String template) {
		super();
		this.template = template;
		this.parent = parent;
	}
	public FmUIComponent(String template) {
		super();
		this.template = template;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getName() {
		if(isProperty(name)){
			name = trimPropertyMark(name);
		}
		return name;
	}
	
	@Override
	public String getComponentTemplate(){
		return getTemplateBasePath()+"/"+getTheme()+"/"+getTemplate()+".ftl";
	}
	
	protected String getTemplateBasePath(){
		return "[fmtagext]/lib";
	}
	public UIComponent getParent() {
		return parent;
	}
	public void setParent(UIComponent parent) {
		this.parent = parent;
	}
//	@Override
//	public void onRender(UIRender render) {
//		render.setObjectVariable("__this__", this);
//		render.renderTemplate(getComponentTemplate());
//	}
	public String getValue() {
//		if(StringUtils.isBlank(value))
//			value = getName();
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public FmUIComponent val(String value){
		this.value = value;
		return this;
	}
	private UIValuer<?> getUivaluer() {
		if(uivaluer==null){
			uivaluer = createUIValuer();
		}
		return uivaluer;
	}
	
	public Object getUIValue(Object viewValue){
		Object val = getUivaluer().getUIValue(viewValue);
		return val;
	}
	
	public String getUILabel(Object viewValue){
		this.uilabeler = uilabeler==null?createUIValuer(label, ""):uilabeler;
		Object labelValue = uilabeler.getUIValue(viewValue);
		return labelValue==null?"":labelValue.toString();
	}
	
	protected UIValuer<?> createUIValuer(){
		return createUIValuer(value, valueFormat);
	}
	
	protected UIValuer<?> createUIValuer(String value, String valueFormat){
		return new StringUIValuer(this, value, valueFormat);
	}
	public void setUivaluer(UIValuer<?> uivaluer) {
		this.uivaluer = uivaluer;
	}
	
	public boolean hasContainer(){
		return false;
	}
	
	public ContainerUIComponent getContainer(){
		return null;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("name:").append(getName()).append(", ui:").append(getClass());
		if(getParent()!=null){
			sb.append(", parent: {").append(getParent()).append("}");
		}
		return sb.toString();
	}
	public String getValueFormat() {
		return valueFormat;
	}
	public void setValueFormat(String valueFormat) {
		this.valueFormat = valueFormat;
	}
	@Override
	public boolean isUIShowable(Object data) {
		return true;
	}
	public void setUilabeler(UIValuer<?> uilabeler) {
		this.uilabeler = uilabeler;
	}

}
