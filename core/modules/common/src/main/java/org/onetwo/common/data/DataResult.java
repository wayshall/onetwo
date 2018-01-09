package org.onetwo.common.data;



public interface DataResult<DATA> extends Result {
	String SUCCESS = "SUCCESS";

	public DATA getData();
	public DATA dataOrThrows();
}
