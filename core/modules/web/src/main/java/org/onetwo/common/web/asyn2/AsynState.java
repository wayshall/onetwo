package org.onetwo.common.web.asyn2;


public class AsynState {
	private final int value;
	private int count = 0;
	private String name;
	public AsynState(int value, String name) {
		super();
		this.value = value;
		this.name = name;
	}
	void increaseCount(int i) {
		count += i;
	}
	public int getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
	public int getCount() {
		return count;
	}
	
	public String toString(){
		return name+": " + count;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AsynState other = (AsynState) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
