package org.onetwo.common.fish.orm;

import java.util.Collection;

import org.onetwo.common.utils.AnnotationInfo;

public interface JFishMappedEntryMeta {

	public Collection<AbstractMappedField> getFields();
	public Collection<AbstractMappedField> getFields(JFishMappedFieldType... type);
	
	public JFishMappedField getField(String fieldName);
	
	public AnnotationInfo getAnnotationInfo();

	public boolean contains(String field);

	public boolean containsColumn(String col);


	public JFishMappedField getFieldByColumnName(String columnName);


	public JFishMappedEntryMeta addMappedField(AbstractMappedField field);

	public Class<?> getEntityClass();
	
	public String getEntityName();

	public TableInfo getTableInfo();

	public JFishMappedField getIdentifyField();
	
	public MappedType getMappedType();

	public boolean isJoined();
	public boolean isEntity();
	
	public boolean isInstance(Object entity);
	
}