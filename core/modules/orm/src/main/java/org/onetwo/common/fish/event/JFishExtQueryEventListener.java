package org.onetwo.common.fish.event;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.fish.event.JFishExtQueryEvent.ExtQueryType;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;

public class JFishExtQueryEventListener extends AbstractJFishEventListener {

	@SuppressWarnings("unchecked")
	@Override
	public void doEvent(JFishEvent event) {
		JFishExtQueryEvent extEvent = (JFishExtQueryEvent) event;
		JFishEventSource es = event.getEventSource();
//		JFishMappedEntry entry = es.getMappedEntryManager().getEntry(event.getEntityClass());

		Map<Object, Object> props = extEvent.getProperties()==null?new LinkedHashMap<Object, Object>():extEvent.getProperties();
		ExtQuery extQuery = es.createExtQuery(extEvent.getEntityClass(), props);
		
		if(extEvent.getExtQueryType()==ExtQueryType.UNIQUE){
			extQuery.setMaxResults(1);//add: support unique
			extQuery.build();
			List<?> list = es.createAsDataQuery(extQuery).getResultList();
			Object rs = null;
			if(LangUtils.hasElement(list)){
				if(list.size()>1){
					logger.warn(list.size() + " entity["+extEvent.getEntityClass()+"] found when findUnique");
				}
				rs = list.get(0);
			}
			extEvent.setResultObject(rs);
			
		}else if(extEvent.getExtQueryType()==ExtQueryType.PAGINATION){
			extQuery.build();
			if(!Page.class.isInstance(extEvent.getObject()))
				throw new JFishOrmException("not a page object");
			Page<Object> page = (Page<Object>)extEvent.getObject();
			
			if (Page.ASC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
				props.put(ExtQuery.K.ASC, page.getOrderBy());
			}

			if (Page.DESC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
				props.put(ExtQuery.K.DESC, page.getOrderBy());
			}
			
			if (page.isAutoCount()) {
//				Long totalCount = (Long)this.findUnique(extQuery.getCountSql(), (Map)extQuery.getParamsValue().getValues());
				Long totalCount = 0l;
				if(extQuery.isSqlQuery()){
					DataQuery countQuery = es.createAsDataQuery(extQuery.getCountSql(), Long.class);
					countQuery.setParameters((List<Object>)extQuery.getParamsValue().getValues());
					totalCount = (Long)countQuery.getSingleResult();
				}else{
					Number countNumber = (Number)es.findUnique(extQuery.getCountSql(), (Map<String, ?>)extQuery.getParamsValue().getValues(), Number.class);
					totalCount = countNumber.longValue();
				}
				page.setTotalCount(totalCount);
				if(page.getTotalCount()<1){
					return ;
				}
			}
			List<Object> datalist = es.createAsDataQuery(extQuery).setPageParameter(page).getResultList();
			page.setResult(datalist);
			
			extEvent.setResultObject(page);
			
		}else if(extEvent.getExtQueryType()==ExtQueryType.COUNT){
			extQuery.build();
			Number countNumber = (Number)es.findUnique(extQuery.getCountSql(), (Map<String, ?>)extQuery.getParamsValue().getValues(), Number.class);
			extEvent.setResultObject(countNumber);
		}else{
			extQuery.build();
			List<?> datalist = es.createAsDataQuery(extQuery).getResultList();
			extEvent.setResultObject(datalist);
		}
	}
	

}
