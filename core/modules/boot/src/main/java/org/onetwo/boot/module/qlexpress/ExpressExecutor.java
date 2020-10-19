package org.onetwo.boot.module.qlexpress;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

/**
 * @author weishao zeng
 * <br/>
 */
public class ExpressExecutor {
	
	private final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	private ExpressRunner expressRunner;
	private QLExpressProperties properties;
	
	public ExpressExecutor(ExpressRunner expressRunner, QLExpressProperties properties) {
		super();
		this.expressRunner = expressRunner;
		this.properties = properties;
	}
	

	public Object execute(String expressString, IExpressContext<String,Object> context) {
//		DefaultContext<String, Object> ctx = new DefaultContext<String, Object>();
		if (StringUtils.isBlank(expressString)) {
			throw new IllegalArgumentException("公式不能为空");
		}
		Object result = null;
		if (properties.isShowExpression()) {
			logger.info("execute expression: {}", expressString);
		}
		try {
			result = this.expressRunner.execute(expressString, context, null, properties.isCache(), properties.isTrace());
		} catch (Exception e) {
			throw new BaseException("execute ql error, expression: " + expressString + ", message: " + e.getMessage(), e);
		}
		return result;
	}
	

	public void checkSyntax(String expressString) {
		boolean result = this.expressRunner.checkSyntax(expressString);
		if (!result) {
			throw new BaseException("ql syntax error: " + expressString);
		}
	}
}
