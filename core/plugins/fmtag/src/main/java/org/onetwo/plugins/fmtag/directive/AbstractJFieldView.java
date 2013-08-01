package org.onetwo.plugins.fmtag.directive;

import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.web.view.HtmlElement;
import org.onetwo.plugins.fmtag.JFieldShowable;

abstract public class AbstractJFieldView extends HtmlElement implements Comparable<AbstractJFieldView>{
	
	private JFieldShowable[] showables;

	private int showOrder;

	private String format = "";


	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int order) {
		this.showOrder = order;
	}

	@Override
	public int compareTo(AbstractJFieldView o) {
		return this.showOrder-o.showOrder;
	}

	public JFieldShowable[] getShowables() {
		return showables;
	}

	public void setShowable(JFieldShowable[] showable) {
		this.showables = showable;
	}
	
	public boolean isShowInGrid(){
		return canShow(JFieldShowable.grid);
	}
	
	public boolean canShow(JFieldShowable showable){
		return ArrayUtils.contains(showables, showable);
	}

}
