package org.onetwo.boot.module.qlexpress;

import org.onetwo.common.exception.BaseException;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

/**
 * @author weishao zeng
 * <br/>
 */
public class ExpressExecutor {
	
	private ExpressRunner expressRunner;
	private QLExpressProperties properties;
	
	public ExpressExecutor(ExpressRunner expressRunner, QLExpressProperties properties) {
		super();
		this.expressRunner = expressRunner;
		this.properties = properties;
	}
	

	public Object execute(String expressString, IExpressContext<String,Object> context) {
		Object result = null;
		try {
			result = this.expressRunner.execute(expressString, context, null, properties.isCache(), properties.isTrace());
		} catch (Exception e) {
			throw new BaseException("execute ql error: " + e.getMessage(), e);
		}
		return result;
	}
}
