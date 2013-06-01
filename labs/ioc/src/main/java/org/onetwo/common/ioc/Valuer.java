package org.onetwo.common.ioc;

public class Valuer {
	
	public static enum Type {
		value,
		local,
		remote;
	}
	
	private Type type;
	private Object value;
	
	public static Valuer val(Object val){
		if(val instanceof Valuer)
			return (Valuer) val;
		return new Valuer(val);
	}
	
	public static Valuer local(Object val){
		if(val instanceof Valuer)
			return (Valuer) val;
		return new Valuer(Type.local, val);
	}
	
	private Valuer(Object value){
		this.type = Type.value;
		this.value = value;
	}
	
	private Valuer(Type type, Object value){
		this.type = type;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
