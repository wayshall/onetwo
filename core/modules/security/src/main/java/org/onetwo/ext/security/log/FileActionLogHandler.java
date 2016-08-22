package org.onetwo.ext.security.log;

import org.onetwo.common.jackson.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileActionLogHandler implements ActionLogHandler<AdminActionLog> {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private JsonMapper jsonMapper;
	
	public FileActionLogHandler(JsonMapper jsonMapper) {
		super();
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void saveLog(AdminActionLog actionLog) {
		String msg = jsonMapper.toJson(actionLog);
		logger.info(msg);
	}

}
