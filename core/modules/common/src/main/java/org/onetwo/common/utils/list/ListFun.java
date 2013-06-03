package org.onetwo.common.utils.list;



@SuppressWarnings("unchecked")
@Deprecated
public interface ListFun<T>{
	
	public static abstract class SimpleListFun<T> implements ListFun<T> {

		private EasyList<T> easylist;
		
		public EasyList<T> getThis(){
			return easylist;
		}

		@Override
		public void exe(T element, int index, EasyList<T> easytor, Object... objects) {
			this.easylist = easytor;
			this.doList(element, index, objects);
		}

		abstract public void doList(T element, int index, Object... objects);
		
	}

	abstract public void exe(T element, int index, EasyList<T> easytor, Object...objects);
	
}
