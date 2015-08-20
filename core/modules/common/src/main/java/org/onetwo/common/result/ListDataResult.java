package org.onetwo.common.result;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class ListDataResult<T> extends AbstractDataResult<List<T>>{

	public static <E> ListDataResult<E> createFailed(String message){
		return create(FAILED, message);
	}

	@SafeVarargs
	public static <E> ListDataResult<E> createSucceed(E...objects){
		return create(SUCCEED, "SUCCEED", objects);
	}
	
	@SafeVarargs
	public static <E> ListDataResult<E> create(int code, String message, E...objects){
		ListDataResult<E> result = new ListDataResult<>();
		result.setCode(code);
		result.setMessage(message);
		result.setData(Arrays.asList(objects));
		return result;
	}
	
	private List<T> data;
	

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
}
