package org.onetwo.boot.core.web.view;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.data.DataResultWrapper;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.spring.mvc.utils.DataResults.MapResultBuilder;
import org.onetwo.common.spring.mvc.utils.ModelAttr;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.BindingResult;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultDataResultWrapper implements DataResultWrapper {
	
	
	@Override
	public Object wrapResult(final Object data) {
		// MappingJacksonValue 类型的不包装
		if (data instanceof MappingJacksonValue) {
			return data;
		}
		Object newData = wrapAsDataResultIfNeed(data);
		return newData;
	}
	
	protected DataResult<?> wrapAsDataResultIfNeed(Object result){
		if(DataResult.class.isInstance(result)){
			return (DataResult<?>)result;
		}
		if(Map.class.isInstance(result)){
			@SuppressWarnings("unchecked")
			MapResultBuilder dataResult = createMapResultBuilder((Map<String, Object>) result);
			return dataResult.build();
		}else{
			SimpleDataResult<Object> dataResult = SimpleDataResult.success("success", result);
			return dataResult;
		}
	}
	
	private MapResultBuilder createMapResultBuilder(Map<String, Object> map){
		MapResultBuilder dataResult = DataResults.map().success();
		for(Entry<String, Object> entry : map.entrySet()){
			if(BindingResult.class.isInstance(entry.getValue())){
				BindingResult br = (BindingResult) entry.getValue();
				if(br.hasErrors()){
					dataResult.error();
				}
				
			}else if(ModelAttr.MESSAGE.equalsIgnoreCase(entry.getKey())){
				dataResult.success(entry.getValue().toString());
				//只返回message，清空data
				dataResult.data(null);
				return dataResult;
				
			}else if(ModelAttr.ERROR_MESSAGE.equalsIgnoreCase(entry.getKey())){
				dataResult.error(entry.getValue().toString());
				//只返回message，清空data
				dataResult.data(null);
				return dataResult;
				
			}else{
				dataResult.put(entry.getKey(), entry.getValue());
			}
		}
		return dataResult;
	}

}
