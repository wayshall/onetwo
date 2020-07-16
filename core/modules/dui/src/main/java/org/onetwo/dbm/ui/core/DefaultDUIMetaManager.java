package org.onetwo.dbm.ui.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.db.generator.dialet.DatabaseMetaDialet;
import org.onetwo.common.db.generator.dialet.DelegateDatabaseMetaDialet;
import org.onetwo.common.db.generator.meta.TableMeta;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.SpringAnnotationUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.onetwo.dbm.mapping.DbmMappedEntry;
import org.onetwo.dbm.mapping.DbmMappedField;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.ui.EnableDbmUI;
import org.onetwo.dbm.ui.annotation.DUICascadeEditable;
import org.onetwo.dbm.ui.annotation.DUIEntity;
import org.onetwo.dbm.ui.annotation.DUIField;
import org.onetwo.dbm.ui.annotation.DUIInput;
import org.onetwo.dbm.ui.annotation.DUIInput.InputTypes;
import org.onetwo.dbm.ui.annotation.DUISelect;
import org.onetwo.dbm.ui.annotation.DUITreeGrid;
import org.onetwo.dbm.ui.exception.DbmUIException;
import org.onetwo.dbm.ui.meta.DUIEntityMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta.DUIInputMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta.DUISelectMeta;
import org.onetwo.dbm.ui.meta.DUITreeGridMeta;
import org.onetwo.dbm.ui.spi.DUILabelEnum;
import org.onetwo.dbm.ui.spi.DUIMetaManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

/**
 * @author weishao zeng
 * <br/>
 */

public class DefaultDUIMetaManager implements InitializingBean, DUIMetaManager {
	
	@Autowired
	private DbmSessionFactory dbmSessionFactory;
	private DatabaseMetaDialet databaseMetaDialet;
	private MappedEntryManager mappedEntryManager;
	private Cache<Class<?>, DUIEntityMeta> entryCaches = CacheBuilder.newBuilder().build();

	private JFishResourcesScanner resourcesScanner = new JFishResourcesScanner();
	private String[] packagesToScan;
	private Map<String, String> duiEntityClassMap = Maps.newConcurrentMap();
	private Map<String, String> duiEntityTableMap = Maps.newConcurrentMap();
	@Autowired
	private ApplicationContext applicationContext;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		Assert.notEmpty(packagesToScan, "packagesToScan can not be empty");
		this.mappedEntryManager = dbmSessionFactory.getMappedEntryManager();
		this.databaseMetaDialet = new DelegateDatabaseMetaDialet(dbmSessionFactory.getDataSource());
		
		Set<String> packsages = this.scanEnableDbmUIPackages();
		
		resourcesScanner.scan((metadataReader, res, index)->{
			if( metadataReader.getAnnotationMetadata().hasAnnotation(DUIEntity.class.getName()) ){
				Map<String, Object> uiclassAttrs = metadataReader.getAnnotationMetadata().getAnnotationAttributes(DUIEntity.class.getName());
				String name = (String)uiclassAttrs.get("name");
				if (StringUtils.isBlank(name)) {
					name = metadataReader.getClassMetadata().getClassName();
				}
//				Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName(), false);
				if (duiEntityClassMap.containsKey(name)) {
					throw new DbmUIException("duplicate ui name: " + name);
				}
				duiEntityClassMap.put(name, metadataReader.getClassMetadata().getClassName());
				

				Map<String, Object> tableAttrs = metadataReader.getAnnotationMetadata().getAnnotationAttributes(Table.class.getName());
				String tableName = (String)tableAttrs.get("name");
				if (StringUtils.isNotBlank(tableName)) {
					duiEntityTableMap.put(tableName.toLowerCase(), metadataReader.getClassMetadata().getClassName());
				}
			}
			return null;
		}, packsages.toArray(new String[0]));
	}
	
	private Set<String> scanEnableDbmUIPackages(){
		Set<String> packageNames = SpringAnnotationUtils.scanAnnotationPackages(applicationContext, EnableDbmUI.class);
		if (!LangUtils.isEmpty(packagesToScan)) {
			packageNames.addAll(Arrays.asList(packagesToScan));
		}
		return packageNames;
	}
	

	public DUIEntityMeta getByTable(String tableName) {
		tableName = tableName.toLowerCase();
		if (!duiEntityTableMap.containsKey(tableName)) {
			throw new DbmUIException("dui entity class not found for table: " + tableName);
		}
		String className = duiEntityTableMap.get(tableName);
		Class<?> uiclass = ReflectUtils.loadClass(className);
		return get(uiclass);
	}


	public Optional<DUIEntityMeta> findByTable(String tableName) {
		try {
			return Optional.of(getByTable(tableName));
		} catch (Exception e) {
		}
		return Optional.empty();
	}

	public DUIEntityMeta get(String uiname) {
		if (!duiEntityClassMap.containsKey(uiname)) {
			throw new DbmUIException("dui entity class not found for name: " + uiname);
		}
		String className = duiEntityClassMap.get(uiname);
		Class<?> uiclass = ReflectUtils.loadClass(className);
		return get(uiclass);
	}
	
	public Optional<DUIEntityMeta> find(Class<?> uiEntityClass) {
		DUIEntity uiclassAnno = uiEntityClass.getAnnotation(DUIEntity.class);
		if (uiclassAnno==null) {
			return Optional.empty();
		}
		DUIEntityMeta meta = null;
		try {
			meta = get(uiEntityClass);
		} catch (Exception e) {
		}
		return Optional.ofNullable(meta);
	}
	
	public DUIEntityMeta get(Class<?> uiclass) {
		try {
			DUIEntityMeta entityMeta = entryCaches.get(uiclass, () -> {
				return buildUIClassMeta(uiclass);
			});
			DUICascadeEditable[] cascadeEditableList = entityMeta.getCascadeEditableList();
			if (!LangUtils.isEmpty(cascadeEditableList)) {
				buildEditableEntities(entityMeta);
			}
			if (entityMeta.getTreeGrid()!=null && entityMeta.getTreeGrid().hasCascadeEntity()) {
				DUIEntityMeta editableMeta = get(entityMeta.getTreeGrid().getCascadeEntity());
				entityMeta.getTreeGrid().setCascadeEntityMeta(editableMeta);
			}
			return entityMeta;
		} catch (Exception e) {
			if (e.getCause() instanceof BaseException) {
				throw (BaseException) e.getCause();
			}
			throw new DbmUIException("get EntityUIMeta error", e);
		}
	}

	protected void buildEditableEntities(DUIEntityMeta entityMeta) {
		for (DUICascadeEditable cascadeEditable : entityMeta.getCascadeEditableList()) {
//				DUIEntityMeta editableMeta = get(editableEntityClass);
			DUIEntityMeta editableMeta = get(cascadeEditable.entityClass());
			if (editableMeta==null) {
				continue;
			}
			editableMeta.setCascadeField(cascadeEditable.cascadeField());
			entityMeta.addEditableEntity(editableMeta);
		}
	}
	
	protected DUIEntityMeta buildUIClassMeta(Class<?> uiEntityClass) {
		DUIEntity uiclassAnno = uiEntityClass.getAnnotation(DUIEntity.class);
		
		if (uiclassAnno==null) {
			throw new DbmUIException("@DUIEntity not found on the ui class: " + uiEntityClass);
		}
		
		DbmMappedEntry entry = mappedEntryManager.getEntry(uiEntityClass);
		if (entry==null) {
//			return null;
			throw new DbmUIException("ui class must be a dbm entity: " + uiEntityClass);
		}
		
		String entityName = uiclassAnno.name();
		if (StringUtils.isBlank(entityName)) {
			entityName = entry.getEntityName();
		}
		
		TableMeta table = databaseMetaDialet.getTableMeta(entry.getTableInfo().getName());
		DUIEntityMeta entityMeta = new DUIEntityMeta();
		entityMeta.setLabel(uiclassAnno.label());
		entityMeta.setName(entityName);
		entityMeta.setMappedEntry(entry);
		entityMeta.setTable(table);
		entityMeta.setDeletable(uiclassAnno.deletable());
		entityMeta.setDetailPage(uiclassAnno.detailPage());
		
		Collection<DbmMappedField> fields = entry.getFields();
		fields.forEach(field -> {
			buildField(field).ifPresent(uifield -> {
				uifield.setClassMeta(entityMeta);
				if (uifield.getDbmField().getColumn()!=null) {
					uifield.setColumn(table.getColumn(field.getColumn().getName()));
				}
				entityMeta.addField(uifield);
			});
		});
		
		DUITreeGrid treeGridAnno = uiEntityClass.getAnnotation(DUITreeGrid.class);
		if (treeGridAnno!=null) {
			DUITreeGridMeta treeGrid = new DUITreeGridMeta(entityMeta);
			DUIFieldMeta parentField = entityMeta.getField(treeGridAnno.parentField());
			treeGrid.setParentField(parentField);
			treeGrid.setRootId(treeGridAnno.rootId());
			treeGrid.setStyle(treeGridAnno.style());
			treeGrid.setCascadeField(treeGridAnno.cascadeField());
			if (treeGridAnno.cascadeEntity()!=void.class) {
				treeGrid.setCascadeEntity(treeGridAnno.cascadeEntity());
			}
			entityMeta.setTreeGrid(treeGrid);
		}
		
		DUICascadeEditable[] editableEntities = uiclassAnno.cascadeEditableEntities();
		entityMeta.setCascadeEditableList(editableEntities);
		
		return entityMeta;
	}
	
	protected Optional<DUIFieldMeta> buildField(DbmMappedField field) {
		DUIField uifield = field.getPropertyInfo().getAnnotation(DUIField.class);
		if (uifield==null) {
			return Optional.empty();
		}
		DUIFieldMeta uifieldMeta = DUIFieldMeta.builder()
										.name(field.getName())
										.listField(uifield.listField())
//										.listValueWriter(uifield.listValueWriter())
										.label(uifield.label())
										.insertable(uifield.insertable())
										.notnull(field.getPropertyInfo().hasAnnotation(NotNull.class) 
												|| field.getPropertyInfo().hasAnnotation(NotBlank.class))
										.listable(uifield.listable())
										.updatable(uifield.updatable())
										.searchable(uifield.searchable())
										.defaultValue(uifield.defaultValue())
										.dbmField(field)
										.order(uifield.order())
										.build();
		if (StringUtils.isBlank(uifieldMeta.getListField())) {
			uifieldMeta.setListField(uifieldMeta.getName());
		}
		
		// input
		DUIInputMeta input = uifieldMeta.new DUIInputMeta();
		input.setType(InputTypes.TEXT);
		DUIInput inputAnno = field.getPropertyInfo().getAnnotation(DUIInput.class);
		if (inputAnno!=null) {
			input.setType(inputAnno.type());
			input.setValueWriter(inputAnno.valueWriter());
		}
		uifieldMeta.setInput(input);
		
		
		// select
		if (field.isEnumerated()) {
			Class<?> propType = field.getPropertyInfo().getType();
			if (DUILabelEnum.class.isAssignableFrom(propType)) {
				uifieldMeta.setListField(field.getName()+"Label");
			}
			if (Enum.class.isAssignableFrom(propType)) {
				@SuppressWarnings("unchecked")
				DUISelectMeta uiselectMeta = buildSelectMeta(uifieldMeta, null, (Class<? extends Enum<?>>)propType);
				uifieldMeta.setSelect(uiselectMeta);
			}
		}
		DUISelect uiselect = field.getPropertyInfo().getAnnotation(DUISelect.class);
		if (uiselect!=null) {
			DUISelectMeta uiselectMeta = buildSelectMeta(uifieldMeta, uiselect, null);
			uifieldMeta.setSelect(uiselectMeta);
		}
		
		return Optional.of(uifieldMeta);
	}
	
	private DUISelectMeta buildSelectMeta(DUIFieldMeta uifieldMeta, DUISelect uiselect, Class<? extends Enum<?>> enumClass) {
		DUISelectMeta uiselectMeta = uifieldMeta.new DUISelectMeta();
		uiselectMeta.setDataEnumClass(enumClass);
		if (uiselect!=null) {
			uiselectMeta.setDataEnumClass(uiselect.dataEnumClass());
			uiselectMeta.setDataProvider(uiselect.dataProvider());
			uiselectMeta.setLabelField(uiselect.labelField());
			uiselectMeta.setValueField(uiselect.valueField());
			if (uiselect.cascadeEntity()!=Void.class) {
				uiselectMeta.setCascadeEntity(uiselect.cascadeEntity());
				if (uiselect.cascadeQueryFields().length==0) {
					uiselectMeta.setCascadeQueryFields(new String[] {uiselect.labelField()});
				} else {
					uiselectMeta.setCascadeQueryFields(uiselect.cascadeQueryFields());
				}
			}
			uiselectMeta.setTreeSelect(uiselect.treeSelect());
		}
		return uiselectMeta;
	}

	public void setPackagesToScan(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

}
