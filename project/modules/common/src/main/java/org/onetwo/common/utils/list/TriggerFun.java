package org.onetwo.common.utils.list;

@Deprecated
public interface TriggerFun<T> extends ListFun<T>{
	
	public boolean isTriggered(T element, int index, EasyList<T> easytor, Object...objects);
	
}
