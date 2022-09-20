package org.onetwo.common.spring.propeditor;

import java.beans.PropertyEditorSupport;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

public class JsonNodeEditor extends PropertyEditorSupport {
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	private JsonMapper jsonMapper = JsonMapper.IGNORE_NULL;

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.isBlank(text)) {
			return ;
		}
		try {
			setValue(jsonMapper.fromJson(text));
		} catch (Exception ex) {
			logger.error("parse json error : " + ex.getMessage());
		}
	}

	@Override
	public String getAsText() {
		return jsonMapper.toJson(getValue());
	}

}
