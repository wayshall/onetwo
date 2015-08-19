package org.onetwo.common.db.dquery;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.db.dquery.annotation.BatchObject;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;

import com.google.common.collect.ImmutableMap;

public class InvokeContext {

	final private DynamicMethod dynamicMethod;
	final private Object[] parameterValues;
	final private Map<Object, Object> parsedParams;
	
	public InvokeContext(DynamicMethod dynamicMethod, Object[] parameterValues) {
		super();
		this.dynamicMethod = dynamicMethod;
		this.parameterValues = parameterValues;
		this.parsedParams = ImmutableMap.copyOf(dynamicMethod.toMapByArgs(parameterValues));
	}

	public String getQueryName() {
		Object dispatcher = getQueryMatcherValue();
		String queryName = dynamicMethod.getQueryName();
		if(dispatcher!=null){
			return queryName + "(" + dispatcher+")";
		}
		return queryName;
//		return dynamicMethod.getQueryName();
	}
	
	/*public boolean matcher(JFishNamedFileQueryInfo queryInfo){
		if(queryInfo.getMatchers().isEmpty()){
			return !dynamicMethod.hasMatcher();
		}
		return queryInfo.getMatchers().contains(getQueryMatcherValue());
	}*/
	
	private Object getQueryMatcherValue(){
		return dynamicMethod.getMatcherValue(parameterValues);
	}
	
	public Collection<?> getBatchParameter(){
		Collection<?> batchParameter = (Collection<?>)parsedParams.get(BatchObject.class);
		
		if(batchParameter==null){
			if(LangUtils.size(parameterValues)!=1 || !Collection.class.isInstance(parameterValues[0])){
				throw new BaseException("BatchObject not found, the batch method parameter only supported one parameter and must a Collection : " + dynamicMethod.getMethod().toGenericString());
			}
			batchParameter = (Collection<?>)parameterValues[0];
		}
		return batchParameter;
	}
	
	
	public DynamicMethod getDynamicMethod() {
		return dynamicMethod;
	}

	public Object[] getParameterValues() {
		return parameterValues;
	}

	public Map<Object, Object> getParsedParams() {
		return parsedParams;
	}
	
	
	
}
