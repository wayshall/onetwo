package org.onetwo.common.ejb.jpa;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.onetwo.common.db.SelectExtQueryImpl;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.BaseMap;

@SuppressWarnings("rawtypes")
public class JPAExtQuery extends SelectExtQueryImpl {
	//prop to column
	private Map<String, String> fieldMapping;

	public JPAExtQuery(Class<?> entityClass, String alias, Map params, SQLSymbolManager symbolManager) {
		super(entityClass, alias, params, symbolManager);
	}

	public void initQuery(){
		super.initQuery();
		if(!isSqlQuery())
			return ;
		this.fieldMapping = new BaseMap<String, String>();
		PropertyDescriptor[] props = ReflectUtils.desribProperties(entityClass);
		String field = "";
		Method readMethod = null;
		for(PropertyDescriptor pd : props){
			readMethod = ReflectUtils.getReadMethod(entityClass, pd);
			Column col = AnnotationUtils.findAnnotation(readMethod, Column.class);
			if(col==null)
				continue;
//			field = (col==null?StringUtils.convert2UnderLineName(pd.getName()):col.name());
			field = col.name();
//			LangUtils.println("map: ${0}->${1}", pd.getName(), field);
			fieldMapping.put(pd.getName(), field);
		}
	}

	protected String getFromName(Class<?> entityClass){
		String fromName = entityClass.getSimpleName();
		if(isSqlQuery()){
			Table table = AnnotationUtils.findAnnotation(entityClass, Table.class);
			fromName = table==null?StringUtils.convert2UnderLineName(fromName):table.name();
		}
		return fromName;
	}
	
	protected String getDefaultSelectFields(Class<?> entityClass, String alias){
		if(!isSqlQuery())
			return alias;
		if(LangUtils.isEmpty(fieldMapping))
			return alias;
		StringBuilder fields = new StringBuilder();
		int index = 0;
		String field = "";
		for(Map.Entry<String, String> entry : fieldMapping.entrySet()){
			field = super.getFieldName(entry.getValue());// + " as " + entry.getKey();
			fields.append(field);
			if(index!=fieldMapping.size()-1)
				fields.append(", ");
			index++;
		}
		return fields.toString();
	}
	

	public String getFieldName(String f) {
		if(!isSqlQuery())
			return super.getFieldName(f);
		String col;
		if(this.fieldMapping.containsKey(f)){
			col = this.fieldMapping.get(f);
		}else{
			col = f;
		}
		return super.getFieldName(col);
	}

}
