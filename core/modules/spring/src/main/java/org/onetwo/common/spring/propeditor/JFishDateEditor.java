package org.onetwo.common.spring.propeditor;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.onetwo.common.date.DateUtil;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

public class JFishDateEditor extends PropertyEditorSupport {
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			setValue(DateUtil.parse(text));
		} catch (Exception ex) {
			logger.error("parse date error : " + ex.getMessage());
		}
	}

	@Override
	public String getAsText() {
		return DateUtil.formatDateTime((Date) getValue());
	}

}
