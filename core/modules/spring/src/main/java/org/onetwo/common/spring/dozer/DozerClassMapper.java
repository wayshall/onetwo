package org.onetwo.common.spring.dozer;


public class DozerClassMapper {
	
	private final Class<?> classa;
	private final Class<?>[] classb;
	private final boolean mapNull;
	private final boolean mapEmpty;
	
	private final String fieldSplit;
	
	public DozerClassMapper(Class<?> classa, DozerMapping dozerMapping) {
		super();
		this.classa = classa;
		this.classb = dozerMapping.classb();
		this.mapNull = dozerMapping.mapNull();
		this.mapEmpty = dozerMapping.mapEmpty();
		this.fieldSplit = dozerMapping.fieldSplit();
	}
	public Class<?> getClassa() {
		return classa;
	}
	public Class<?>[] getClassb() {
		return classb;
	}
	public boolean isMapNull() {
		return mapNull;
	}
	public boolean isMapEmpty() {
		return mapEmpty;
	}
	public String getFieldSplit() {
		return fieldSplit;
	}
	

}
