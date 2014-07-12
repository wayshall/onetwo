package org.onetwo.common.db;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.annotation.DataQueryFilter;
import org.onetwo.common.db.sqlext.ExtQueryListenerAdapter;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;
import org.springframework.stereotype.Component;

@SuppressWarnings("unchecked")
@Component
public class DataQueryFilterListener extends ExtQueryListenerAdapter{

	@Override
	public void onInit(ExtQuery query) {
		Object dataFilterValue = LangUtils.firstNotNull(query.getParams().remove(DataQueryFilter.class), query.getParams().remove(K.DATA_FILTER));
		if(dataFilterValue!=null){
			boolean isEnabledDataFilter = Types.convertValue(dataFilterValue, Boolean.class);
			if(!isEnabledDataFilter)
				return ;
		}
		
		DataQueryFilter qdf = (DataQueryFilter)query.getEntityClass().getAnnotation(DataQueryFilter.class);
//		DataQueryFilter qdf = (DataQueryFilter)ReflectUtils.getIntro(query.getEntityClass()).getAnnotationWithParent(DataQueryFilter.class);
		if(qdf==null)
			return ;

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

}
