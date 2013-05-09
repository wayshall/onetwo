package org.onetwo.common.utils.map;

import java.util.Map;
import java.util.Map.Entry;

@Deprecated
public interface MapFun<K, V> {

	@Deprecated
	public static abstract class SimpleMapFun<K, V> implements MapFun<K, V> {

		private EasyMap<K, V> easymap;
		
		public EasyMap<K, V> getThis(){
			return easymap;
		}

		@Override
		public void exe(Entry<K, V> element, int index, EasyMap<K, V> easymap, Object... objects) {
			this.easymap = easymap;
			doMap(element, index, objects);
		}
		
		abstract public void doMap(Entry<K, V> element, int index, Object... objects);
		
	}

	public void exe(Map.Entry<K, V> element, int index, EasyMap<K, V> easymap, Object...objects);
	
}
