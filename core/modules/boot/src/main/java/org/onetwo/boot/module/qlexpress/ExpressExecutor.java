package org.onetwo.boot.module.qlexpress;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.Operator;

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
	
	public ExpressExecutor addOperator(String name, Operator op) {
		return addOperator(name, "*", op);
	}
	
	/****
	 * 添加操作符
	 * @author weishao zeng
	 * @param name
	 * @param aRefOpername
	 * @param op
	 * @return
	 */
	public ExpressExecutor addOperator(String name,String aRefOpername,Operator op) {
		try {
			this.expressRunner.addOperator(name, aRefOpername, op);
		} catch (Exception e) {
			throw new ServiceException("添加自定义操作符错误：" + name);
		}
		return this;
	}
	
	public ExpressExecutor addFunction(String name, Operator op) {
		try {
			this.expressRunner.addFunction(name, op);
		} catch (Exception e) {
			throw new ServiceException("添加自定义操作符错误：" + name);
		}
		return this;
	}
	

	public ExpressExecutor addOperatorWithAlias(String keyWordName, String realKeyWordName) {
		try {
			this.expressRunner.addOperatorWithAlias(keyWordName, realKeyWordName, null);
		} catch (Exception e) {
			throw new ServiceException("添加操作符别名错误, keyWordName: " + keyWordName + ", realKeyWordName: " + realKeyWordName);
		}
		return this;
	}

	public Object execute(String expressString, Map<String,Object> context) {
		DefaultContext<String, Object> calcContext = new DefaultContext<String, Object>();
		calcContext.putAll(context);
		
		if (StringUtils.isBlank(expressString)) {
			throw new IllegalArgumentException("公式不能为空");
		}
		
		
		Object result = null;
		if (properties.isShowExpression()) {
			logger.info("execute expression: {}", expressString);
		}
		
		checkVars(expressString, calcContext);
		
		try {
			result = this.expressRunner.execute(expressString, calcContext, null, properties.isCache(), properties.isTrace());
		} catch (Exception e) {
			throw new BaseException("execute ql error, expression: " + expressString + ", message: " + e.getMessage(), e);
		}
		return result;
	}
	
	private void checkVars(String expressString, DefaultContext<String,Object> calcContext) {
		String[] varNames;
		try {
			varNames = expressRunner.getOutVarNames(expressString);
		} catch (Exception e) {
			throw new ServiceException("表达式[" + expressString + "]语法可能有错: " + e.getMessage(), e);
		}
		for (String varName : varNames) {
			if (!calcContext.containsKey(varName)) {
				throw new ServiceException("变量未定义：" + varName);
			}
		}
	}
	

	public void checkSyntax(String expressString) {
		boolean result = this.expressRunner.checkSyntax(expressString);
		if (!result) {
			throw new BaseException("ql syntax error: " + expressString);
		}
	}

	public ExpressRunner getExpressRunner() {
		return expressRunner;
	}
	
}
