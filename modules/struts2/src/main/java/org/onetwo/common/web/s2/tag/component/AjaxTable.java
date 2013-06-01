package org.onetwo.common.web.s2.tag.component;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxanywhere.AAUtils;

import com.opensymphony.xwork2.util.ValueStack;

public class AjaxTable extends StrutsTable {

	private String zoneName;

	public AjaxTable(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	protected boolean ajaxStart(Writer writer){
		try {
			writer.write(AAUtils.getZoneStartDelimiter(zoneName));
		} catch (IOException e) {
            logger.error("Could not open template", e);
            e.printStackTrace();
		}
		if (skipIfNotIncluded && AAUtils.isAjaxRequest(request) && !AAUtils.getZonesToRefresh(request).contains(name))
			return false;
		else
			return true;
	}
	

	@Override
	public void evaluateExtraParams() {
		super.evaluateExtraParams();
		this.addParameter("zoneName", this.zoneName);
	}

	protected boolean ajaxEnd(Writer writer, String body) {
		try {
			writer.write(AAUtils.getZoneEndDelimiter(zoneName));
		} catch (IOException e) {
            logger.error("Could not open template", e);
            e.printStackTrace();
		}
		return true;
	}

	public boolean start(Writer writer) {
		if(this.ajaxStart(writer))
			return super.start(writer);
		return false;
	}

	public boolean end(Writer writer, String body) {
		super.end(writer, body);
		return ajaxEnd(writer, body);
	}
	
	public void setName(String name){
		super.setName(name);
		this.zoneName = name + "Zone";
	}

	public String getZoneName() {
		return zoneName;
	}
}
