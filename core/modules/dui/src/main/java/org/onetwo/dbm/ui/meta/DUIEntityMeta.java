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
import org.onetwo.dbm.mapping.DbmMappedEntry;
import org.onetwo.dbm.ui.annotation.DUICascadeEditable;
import org.onetwo.dbm.ui.exception.DbmUIException;

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
	private Collection<DUIEntityMeta> editableEntities;
	
	private DUIEntityMeta parent;
	private boolean editableEntity;
	
	private String cascadeField;
	
	public Class<?> getEntityClass() {
		return this.mappedEntry.getEntityClass();
	}
	
	public void addEditableEntity(DUIEntityMeta entityMeta) {
		if (this.editableEntities==null) {
			this.editableEntities = Sets.newHashSet();
		}
		entityMeta.setParent(this);
		entityMeta.setEditableEntity(true);
		this.editableEntities.add(entityMeta);
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
	
	public Collection<DUIFieldMeta> getSearchableFields() {
		return getFields().stream().filter(f -> f.isSearchable()).collect(Collectors.toList());
	}
	
	public Collection<DUIFieldMeta> getFormFields() {
		return getFields().stream().filter(f -> f.isInsertable() || f.isUpdatable()).collect(Collectors.toList());
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
