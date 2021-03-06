package org.onetwo.dbm.ui.meta;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.onetwo.common.db.generator.meta.TableMeta;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.mapping.DbmMappedEntry;
import org.onetwo.dbm.ui.annotation.DUICascadeEditable;
import org.onetwo.dbm.ui.annotation.DUIChildEntity;
import org.onetwo.dbm.ui.annotation.DUIEntity.DetailPages;
import org.onetwo.dbm.ui.annotation.NullDUIJsonValueWriter;
import org.onetwo.dbm.ui.exception.DbmUIException;
import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class DUIEntityMeta {
	
	private String name;
	private String label;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	final private Map<String, DUIFieldMeta> fieldMap = Maps.newLinkedHashMap();
	private List<DUIFieldMeta> fields = Lists.newArrayList();
	 
//	private Collection<UIFieldMeta> fields = Sets.newTreeSet(Comparator.comparingInt(f -> f.getOrder()));
	private DbmMappedEntry mappedEntry;
	private TableMeta table;
	
	
	
	private DUICascadeEditable[] cascadeEditableList;
	/***
	 * 级联编辑的实体
	 */
	private Collection<DUIEntityMeta> editableEntities;

	private DUIEntityMeta parent;
	private boolean editableEntity;
	private String cascadeField;
	
	
	

	private DUIChildEntity[] childEntities;
	/****
	 * 子实体
	 */
	private Collection<DUIEntityMeta> childrenEntities;
	private String refParentField;
	private boolean childEntity;
	
	
	
	
	private DUITreeGridMeta treeGrid;

	/****
	 * 如果父组件是树形组件，则不为null
	 */
	private DUIEntityMeta treeParent;
	
	private boolean deletable;
	private DetailPages detailPage;
	
	/****
	 * 在生成的上下文设置
	 */
	String stripPrefix;
	String moduleName;
	
	public boolean isDetailPageEditable() {
		return this.detailPage.equals(DetailPages.EDIT);
	}
	
	public boolean isTree() {
		return treeGrid!=null;
	}
	
	public void setStripPrefix(String stripPrefix) {
		this.stripPrefix = stripPrefix;
		if (this.table!=null) {
			this.table.setStripPrefix(stripPrefix);
		}
	}
	
	public Class<?> getEntityClass() {
		return this.mappedEntry.getEntityClass();
	}
	
	public boolean hasFileField() {
		return getFormFields().stream()
							.filter(f -> f.getInput().isFileType())
							.findAny()
							.isPresent();
	}
	
	public void addEditableEntity(DUIEntityMeta entityMeta) {
		if (this.editableEntities==null) {
			this.editableEntities = Sets.newHashSet();
		}
		entityMeta.setParent(this);
		entityMeta.setEditableEntity(true);
		this.editableEntities.add(entityMeta);
	}
	
	public void addChildrenEntity(DUIEntityMeta entityMeta) {
		if (this.childrenEntities==null) {
			this.childrenEntities = Sets.newHashSet();
		}
		entityMeta.setParent(this);
		entityMeta.setChildEntity(true);
		this.childrenEntities.add(entityMeta);
	}
	
	public Collection<DUIEntityMeta> getEditableEntities() {
		return this.editableEntities;
	}
	
	public void addField(DUIFieldMeta field) {
		fieldMap.put(field.getName(), field);
		fields.add(field);
		Collections.sort(fields, Comparator.comparingInt(f -> f.getOrder()));
	}
	
	public DUIFieldMeta getField(String fieldName) {
		DUIFieldMeta field =  fieldMap.get(fieldName);
		if (field==null) {
			throw new DbmUIException("ui field not found for name: " + fieldName);
		}
		return field;
	}
	
	public Collection<DUIFieldMeta> getFields() {
		return fields;
	}
	
	public Collection<DUIFieldMeta> getListableFields() {
		return getFields().stream().filter(f -> f.isListable()).collect(Collectors.toList());
	}
	
	public Collection<DUIFieldMeta> getSelectableFields() {
		return getFields().stream().filter(f -> f.getSelect()!=null).collect(Collectors.toList());
	}
	
	public Collection<DUIFieldMeta> getSearchableFields() {
		List<DUIFieldMeta> fields = getFields().stream().filter(f -> f.isSearchable()).collect(Collectors.toList());
		return fields;
	}
	
	public Collection<DUIFieldMeta> getFormFields() {
//		return getFields().stream().filter(f -> f.isInsertable() || f.isUpdatable()).collect(Collectors.toList());
		Collection<DUIFieldMeta> formFields = getFields();
		return formFields;
	}
	
	public Collection<DUIFieldMeta> getHasDefaultFields() {
		return getFields().stream().filter(f -> StringUtils.isNotBlank(f.getDefaultValue())).collect(Collectors.toList());
	}
	
	public String getComponentName() {
		String componentName = StringUtils.capitalize(this.moduleName) + this.name;
		return componentName;
	}
	
	public String getRefParentFieldKebabCase() {
		return StringUtils.convertWithSeperator(refParentField, "-");
	}
	
	public static boolean hasValueWriter(Class<? extends DUIJsonValueWriter> valueWriter) {
		return valueWriter!=null && valueWriter!=NullDUIJsonValueWriter.class;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DUIEntityMeta other = (DUIEntityMeta) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	

}
