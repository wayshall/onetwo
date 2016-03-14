package org.onetwo.common.db.filter.annotation;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.convert.Types;
import org.onetwo.common.db.filter.DataQueryParamaterEnhancer;
import org.onetwo.common.db.filter.IDataQueryParamterEnhancer;
import org.onetwo.common.db.sqlext.ExtQuery;
import org.onetwo.common.db.sqlext.ExtQuery.K;
import org.onetwo.common.db.sqlext.ExtQueryInner;
import org.onetwo.common.db.sqlext.ExtQueryListenerAdapter;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.util.Assert;

//@Component
public class DataQueryFilterListener extends ExtQueryListenerAdapter{

	@Override
	public void onInit(ExtQuery q) {
		ExtQueryInner query = (ExtQueryInner) q;
		Object dataFilterValue = LangUtils.firstNotNull(query.getParams().remove(DataQueryFilter.class), query.getParams().remove(K.DATA_FILTER));
		boolean isEnabledDataFilter = dataFilterValue!=null?Types.convertValue(dataFilterValue, Boolean.class):true;
		if(isEnabledDataFilter){
			DataQueryFilter qdf = (DataQueryFilter)query.getEntityClass().getAnnotation(DataQueryFilter.class);
	//		DataQueryFilter qdf = (DataQueryFilter)ReflectUtils.getIntro(query.getEntityClass()).getAnnotationWithParent(DataQueryFilter.class);
			if(qdf!=null)
				addParameterByDataQueryFilter(query, qdf);
		}


		dataFilterValue = LangUtils.firstNotNull(query.getParams().remove(DataQueryParamaterEnhancer.class), query.getParams().remove(K.DATA_FILTER));
		isEnabledDataFilter = dataFilterValue!=null?Types.convertValue(dataFilterValue, Boolean.class):true;
		if(isEnabledDataFilter){
			DataQueryParamaterEnhancer dqpe = query.getEntityClass().getAnnotation(DataQueryParamaterEnhancer.class);
			if(dqpe!=null)
				addParameterByIDataQueryParamterEnhancer(query, dqpe);
		}
		
	}
	
	private void addParameterByDataQueryFilter(ExtQueryInner query, DataQueryFilter qdf){
		Assert.notNull(qdf);
		String[] fields = qdf.fields();
		String[] values = qdf.values();
		if(fields.length!=values.length)
			throw new BaseException("the length is not equals of QueryDataFilter");
		int index = 0;
		for(String field : fields){
			if(query.getParams().containsKey(field)){
				Object val = query.getParams().get(field);
				if(val!=null && StringUtils.isNotBlank(val.toString()))
					return ;
				query.getParams().put(field, values[index++]);
			}else{
				query.getParams().put(field, values[index++]);
			}
		}
	}
	
	private void addParameterByIDataQueryParamterEnhancer(ExtQueryInner query, DataQueryParamaterEnhancer dqpe){
		Assert.notNull(dqpe);
		Class<? extends IDataQueryParamterEnhancer> enhancerCls = dqpe.value();
		IDataQueryParamterEnhancer enhancer = ReflectUtils.newInstance(enhancerCls);
		Map<Object, Object> params = enhancer.filterParameters();
		if(LangUtils.isEmpty(params))
			return;

		for(Entry<Object, Object> field : params.entrySet()){
			if(query.getParams().containsKey(field.getKey())){
				Object val = query.getParams().get(field.getKey());
				if(val!=null && StringUtils.isNotBlank(val.toString()))
					return ;
				query.getParams().put(field.getKey(), field.getValue());
			}else{
				query.getParams().put(field.getKey(), field.getValue());
			}
		}
		/*Set<String> queryFields = query.getAllParameterFieldNames();
		for(Entry<String, Object> field : params.entrySet()){
			if(queryFields.contains(field.getKey()))
				continue;
			query.getParams().put(field.getKey(), field.getValue());
		}*/
	}

}
