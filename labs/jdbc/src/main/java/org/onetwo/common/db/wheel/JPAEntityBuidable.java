package org.onetwo.common.db.wheel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public class JPAEntityBuidable extends EntityTableInfoBuilder {
	
	public JPAEntityBuidable(){
	}
	
	public JPAEntityBuidable(boolean cacheTableInfo) {
		super(cacheTableInfo);
	}

	@Override
	protected TableInfo _buildTableInfo(Class<?> entityClass){
		if(entityClass.getAnnotation(Entity.class)==null && entityClass.getAnnotation(Table.class)==null)
			return super._buildTableInfo(entityClass);
		TableInfo tableInfo = new TableInfo(entityClass, getTableName(entityClass));
		tableInfo.setSeqName(getSeqName(entityClass));
		
//		List<String> fieldNames = new ArrayList<String>();
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(entityClass);
		if(fields==null){
			return tableInfo;
		}
		String colName;
		for(Field field : fields){
			if(!LangUtils.isBaseType(field.getType()))
				continue;
			Method method = ReflectUtils.findGetMethod(entityClass, field);
//			System.out.println("field:"+field.getName()+"     method:"+method);
			if(method!=null && !Modifier.isTransient(method.getModifiers())){
				colName = field.getName();
				Column anno = method.getAnnotation(Column.class);
				if(anno!=null){
					colName = anno.name();
				}else{
					colName = StringUtils.convert2UnderLineName(colName);
				}
				tableInfo.column(colName, field.getName());
				if(method.getAnnotation(Id.class)!=null){
//					Id id = method.getAnnotation(Id.class);
					tableInfo.id(colName);
					GeneratedValue g = method.getAnnotation(GeneratedValue.class);
					if(g!=null && g.strategy()==GenerationType.IDENTITY){
						tableInfo.setDbCreatePrimaryKey(true);
					}
				}
//				fieldNames.add(colName);
			}
		}
//		LangUtils.println("fieldNames :"+StringUtils.join(fieldNames, ", "));
		return tableInfo;
	}

	public String getTableName(Class<?> clazz) {
		Table table = AnnotationUtils.findAnnotation(clazz, Table.class);
		if(table==null)
			LangUtils.throwBaseException("calzz["+clazz+"] not found table");
		return table.name();
	}
	
	public String getSeqName(Class<?> entityClass){
		String seqName = "";
		SequenceGenerator sg = entityClass.getAnnotation(SequenceGenerator.class);
		if(sg!=null)
			seqName = sg.sequenceName();
		return seqName;
	}

}
