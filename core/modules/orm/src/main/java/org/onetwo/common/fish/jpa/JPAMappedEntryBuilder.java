package org.onetwo.common.fish.jpa;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.onetwo.common.fish.orm.AbstractDBDialect.StrategyType;
import org.onetwo.common.fish.orm.AbstractMappedField;
import org.onetwo.common.fish.orm.BaseColumnInfo;
import org.onetwo.common.fish.orm.ColumnInfo;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedEntryBuilder;
import org.onetwo.common.fish.orm.JFishMappedEntryImpl;
import org.onetwo.common.fish.orm.JFishMappedField;
import org.onetwo.common.fish.orm.TableInfo;
import org.onetwo.common.utils.AnnotationInfo;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;

public class JPAMappedEntryBuilder extends JFishMappedEntryBuilder {
	
	public JPAMappedEntryBuilder(){
		this.setOrder(0);
	}


	@Override
	protected AbstractMappedField newMappedField(JFishMappedEntry entry, JFishProperty prop){
		AbstractMappedField mfield = null;
		if(prop.hasAnnotation(ManyToOne.class)){
			mfield = this.newManyToOneField(entry, prop);
		}else if(prop.hasAnnotation(OneToMany.class)){
			mfield = this.newOneToManyField(entry, prop);
		}else if(prop.hasAnnotation(ManyToMany.class)){
			mfield = this.newManyToManyField(entry, prop);
		}else if(prop.hasAnnotation(OneToOne.class)){
			mfield = this.newOneToOneField(entry, prop);
		}else{
			mfield = super.newMappedField(entry, prop);
		}
		return mfield;
	}
	
	protected AbstractMappedField newManyToOneField(JFishMappedEntry entry, JFishProperty prop){
		throw new UnsupportedOperationException("unsupported many to one : " + entry.getEntityClass());
	}
	
	protected AbstractMappedField newManyToManyField(JFishMappedEntry entry, JFishProperty prop){
		throw new UnsupportedOperationException("unsupported many to many : " + entry.getEntityClass());
	}
	
	protected AbstractMappedField newOneToManyField(JFishMappedEntry entry, JFishProperty prop){
		throw new UnsupportedOperationException("unsupported one to many : " + entry.getEntityClass());
	}
	
	protected AbstractMappedField newOneToOneField(JFishMappedEntry entry, JFishProperty prop){
		throw new UnsupportedOperationException("unsupported one to one : " + entry.getEntityClass());
	}

	@Override
	public boolean isSupported(Object entity){
		if(MetadataReader.class.isInstance(entity)){
			MetadataReader metadataReader = (MetadataReader) entity;
			AnnotationMetadata am = metadataReader.getAnnotationMetadata();
			return am.hasAnnotation(Entity.class.getName());
		}else{
			Class<?> entityClass = ReflectUtils.getObjectClass(entity);
			return entityClass.getAnnotation(Entity.class)!=null;
		}
	}
	
	@Override
	protected JFishMappedEntry createJFishMappedEntry(AnnotationInfo annotationInfo) {
		TableInfo tableInfo = newTableInfo(annotationInfo);
		JFishMappedEntry entry = new JFishMappedEntryImpl(annotationInfo, tableInfo);
		return entry;
	}

	@Override
	protected boolean ignoreMappedField(JFishProperty field){
		return super.ignoreMappedField(field) || field.hasAnnotation(Transient.class);
	}

	@Override
	protected String buildTableName(AnnotationInfo annotationInfo){
		Table table = (Table) annotationInfo.getAnnotation(Table.class);
		String tname = table.name();
		return tname;
	}

	@Override
	protected String buildSeqName(AnnotationInfo annotationInfo, TableInfo tableInfo){
		String sname = null;
		Class<?> entityClass = annotationInfo.getSourceClass();
		SequenceGenerator sg = entityClass.getAnnotation(SequenceGenerator.class);
		if(sg!=null){
			sname = sg.sequenceName();
			if(StringUtils.isBlank(sname))
				sname = sg.name();
		}else{
			sname = super.buildSeqName(annotationInfo, tableInfo);
		}
		return sname;
	}

	protected void buildMappedField(JFishMappedField mfield){
		if(mfield.getPropertyInfo().hasAnnotation(Id.class)){
			mfield.setIdentify(true);
			this.buildIdMappedField(mfield);
		}
	}
	
	private void buildIdMappedField(JFishMappedField mfield){
		GeneratedValue g = mfield.getPropertyInfo().getAnnotation(GeneratedValue.class);
		if(g!=null){
			if(g.strategy()==GenerationType.AUTO){
				if(this.getDialect().getDbmeta().isMySQL()){
					mfield.setStrategyType(StrategyType.increase_id);
				}else{
					mfield.setStrategyType(StrategyType.seq);
				}
			}else if(g.strategy()==GenerationType.IDENTITY){
//					tableInfo.setDbCreatePrimaryKey(true);
				mfield.setStrategyType(StrategyType.increase_id);
			}else if(g.strategy()==GenerationType.SEQUENCE){
				mfield.setStrategyType(StrategyType.seq);
			}
		}
	}
	
	@Override
	protected BaseColumnInfo buildColumnInfo(TableInfo tableInfo, JFishMappedField field){
//		Method method = field.getReadMethod();
//		Method method = ReflectUtils.findMe(getEntityClass(), field.getName());
		String colName = field.getName();
//		if("id".equals(field.getName()))
//			System.out.println("id");
		ColumnInfo col = null;
		Column anno = field.getPropertyInfo().getAnnotation(Column.class);
		if(anno!=null){
			colName = anno.name();
			col = new ColumnInfo(tableInfo, colName);
			col.setInsertable(anno.insertable());
			col.setUpdatable(anno.updatable());
		}else{
			colName = StringUtils.convert2UnderLineName(colName);
			col = new ColumnInfo(tableInfo, colName);
		}
		col.setPrimaryKey(field.isIdentify());
		Basic basic = field.getPropertyInfo().getAnnotation(Basic.class);
		if(basic!=null){
			col.setFetchType(basic.fetch());
		}
		
		return col;
	}
	
}
