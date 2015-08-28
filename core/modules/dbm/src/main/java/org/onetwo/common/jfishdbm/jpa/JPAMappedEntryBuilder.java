package org.onetwo.common.jfishdbm.jpa;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
import javax.persistence.Version;

import org.onetwo.common.annotation.AnnotationInfo;
import org.onetwo.common.jfishdbm.dialet.AbstractDBDialect.StrategyType;
import org.onetwo.common.jfishdbm.dialet.DBDialect;
import org.onetwo.common.jfishdbm.exception.JFishOrmException;
import org.onetwo.common.jfishdbm.mapping.AbstractMappedField;
import org.onetwo.common.jfishdbm.mapping.BaseColumnInfo;
import org.onetwo.common.jfishdbm.mapping.ColumnInfo;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntryBuilder;
import org.onetwo.common.jfishdbm.mapping.JFishMappedEntryImpl;
import org.onetwo.common.jfishdbm.mapping.JFishMappedField;
import org.onetwo.common.jfishdbm.mapping.TableInfo;
import org.onetwo.common.jfishdbm.mapping.version.DateVersionableType;
import org.onetwo.common.jfishdbm.mapping.version.IntegerVersionableType;
import org.onetwo.common.jfishdbm.mapping.version.LongVersionableType;
import org.onetwo.common.jfishdbm.mapping.version.VersionableType;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;

public class JPAMappedEntryBuilder extends JFishMappedEntryBuilder {
	
	private static final Map<Class<?>, VersionableType<? extends Object>> versionTypes;
	static {
		Map<Class<?>, VersionableType<? extends Object>> tem = LangUtils.newHashMap(5);
		tem.put(int.class, new IntegerVersionableType());
		tem.put(Integer.class, new IntegerVersionableType());
		tem.put(long.class, new LongVersionableType());
		tem.put(Long.class, new LongVersionableType());
		tem.put(Date.class, new DateVersionableType());
		
		versionTypes = Collections.unmodifiableMap(tem);
	}
	
	public JPAMappedEntryBuilder(DBDialect dialect) {
		super(dialect);
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
	public JFishMappedEntry buildMappedEntry(Object object) {
		Object entity = LangUtils.getFirst(object);
		Class<?> entityClass = ReflectUtils.getObjectClass(entity);
		Optional<Field> idField = Intro.wrap(entityClass).getAllFields()
								.stream()
								.filter(f->f.getAnnotation(Id.class)!=null)
								.findAny();
							
		return buildMappedEntry(entityClass, !idField.isPresent());
	}
	
	@Override
	protected JFishMappedEntry createJFishMappedEntry(AnnotationInfo annotationInfo) {
		TableInfo tableInfo = newTableInfo(annotationInfo);
		JFishMappedEntryImpl entry = new JFishMappedEntryImpl(annotationInfo, tableInfo);
		entry.setSqlBuilderFactory(this.getDialect().getSqlBuilderFactory());
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

	@Override
	protected void buildMappedField(JFishMappedField mfield){
		if(mfield.getPropertyInfo().hasAnnotation(Id.class)){
			mfield.setIdentify(true);
			this.buildIdMappedField(mfield);
		}
		if(mfield.getPropertyInfo().hasAnnotation(Version.class)){
			if(!versionTypes.containsKey(mfield.getPropertyInfo().getType())){
				throw new JFishOrmException("the type of field["+mfield.getName()+"] is not a supported version type. supported types: " + versionTypes.keySet());
			}
			mfield.setVersionableType(versionTypes.get(mfield.getPropertyInfo().getType()));
		}
	}
	
	private void buildIdMappedField(JFishMappedField mfield){
		GeneratedValue g = mfield.getPropertyInfo().getAnnotation(GeneratedValue.class);
		if(g!=null){
			if(g.strategy()==GenerationType.AUTO){
				if(this.getDialect().getDbmeta().isMySQL()){
					mfield.setStrategyType(StrategyType.INCREASE_ID);
				}else{
					mfield.setStrategyType(StrategyType.SEQ);
				}
			}else if(g.strategy()==GenerationType.IDENTITY){
//					tableInfo.setDbCreatePrimaryKey(true);
				mfield.setStrategyType(StrategyType.INCREASE_ID);
			}else if(g.strategy()==GenerationType.SEQUENCE){
				mfield.setStrategyType(StrategyType.SEQ);
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
