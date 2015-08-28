package org.onetwo.common.xml.jaxb;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.onetwo.common.date.DateUtil;

public class DateAdapter extends XmlAdapter<String, Date>{

	@Override
	public Date unmarshal(String v) throws Exception {
		return DateUtil.parse(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		return DateUtil.formatDateTime(v);
	}

}
