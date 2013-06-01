package org.onetwo.plugins.fmtagext.ui.datagrid;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.springframework.web.servlet.support.RequestContext;

public class PaginationFooterUI extends FmUIComponent {

	private String action = ":qstr";
	
	public PaginationFooterUI(DataGridUI datagrid) {
		super(datagrid, "ui-grid-pagination");
	}

	public DataGridUI getDatagrid() {
		return (DataGridUI)getParent();
	}
	
	public String buildPageLink(int pageNumb){
		String action = getActionString();
		if(pageNumb==1)
			return action;
		if(action.contains("?")){
			return action + "&pageNo="+pageNumb;
		}else{
			return action+"?pageNo="+pageNumb;
		}
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	protected String getActionString() {
		RequestContext request = JFishWebUtils.webHelper().getRequestContext();
		if (StringUtils.isBlank(action))
			return request.getRequestUri();
		if(!action.startsWith(":")){
			return action;
		}
		String surl = request.getRequestUri();
		String[] symbols = StringUtils.split(action, "|");
		int index = 0;
		for (String symbol : symbols) {
			if (StringUtils.isBlank(symbol))
				continue;
			String qstr = this.processUrlSymbol(symbol);
			if (StringUtils.isNotBlank(qstr)) {
				if (index == 0)
					surl += "?";
				else
					surl += "&";
				surl += qstr;
			}
		}

		return surl;
	}

	protected String processUrlSymbol(String symbol) {
		HttpServletRequest request = JFishWebUtils.request();
		String str = null;
		if (symbol.equals(":qstr")) {
			str = request.getQueryString();
			if(StringUtils.isBlank(str))
				return "";
			CasualMap params = new CasualMap(str);
			params.filter("pageNo", "order", "orderBy");
			str = params.toParamString();
		} else if (symbol.equals(":post2get")) {
			str = RequestUtils.getPostParametersWithout(request, "pageNo", "order", "orderBy").toParamString();
		}
		return str;
	}

}
