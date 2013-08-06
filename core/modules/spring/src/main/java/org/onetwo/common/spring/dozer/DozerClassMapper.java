package org.onetwo.common.spring.dozer;

public class DozerClassMapper {
	
	private final Class<?> classa;
	private final Class<?> classb;
	private final boolean mapNull;
	private final boolean mapEmpty;
	
	public DozerClassMapper(Class<?> classa, DozerMapping dozerMapping) {
		super();
		this.classa = classa;
		if(dozerMapping.classb()==Object.class)
			this.classb = this.classa;
		else
			this.classb = dozerMapping.classb();
		this.mapNull = dozerMapping.mapNull();
		this.mapEmpty = dozerMapping.mapEmpty();
	}
	public Class<?> getClassa() {
		return classa;
	}
	public Class<?> getClassb() {
		return classb;
	}
	public boolean isMapNull() {
		return mapNull;
	}
	public boolean isMapEmpty() {
		return mapEmpty;
	}
	
	

}
