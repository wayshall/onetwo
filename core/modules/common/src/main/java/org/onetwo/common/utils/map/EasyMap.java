package org.onetwo.common.utils.map;

import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unchecked")
@Deprecated
public class EasyMap<K, V>{
	
	private Map<K, V> map;
	private Iterator<Map.Entry<K, V>> iterator;
	
	private int currentIndex;
	private boolean _break;
	
	public EasyMap(Map map){
		this.map = map;
	}
	
	public Iterator<Map.Entry<K, V>> getIterator() {
		if(this.iterator==null)
			this.iterator = this.map.entrySet().iterator();
		return iterator;
	}

	public boolean isBreak() {
		return _break;
	}

	public void setBreak() {
		this._break = true;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public void whenExcepted(Exception e){
		e.printStackTrace();
	}

	public int size(){
		return this.map.size();
	}
	
	public boolean isFirst(){
		return this.currentIndex==0;
	}
	
	public boolean isLast(){
		return this.currentIndex == size()-1;
	}

	protected void reset(){
		this.currentIndex = 0;
		this._break = false;
		this.iterator = null;
	}
	
	public EasyMap each(MapFun<K, V> block, Object...objects){
		if(map==null || map.isEmpty())
			return this;
		reset();
		this.iterator = getIterator();
		Map.Entry<K, V> element = null;
		while(iterator.hasNext()){
			element = iterator.next();
			block.exe(element, currentIndex, this, objects);
			if(isBreak()){
				break;
			}
			if(!isLast())
				currentIndex++;
		}
		return this;
	}

	public Map<K, V> getMap() {
		return map;
	}

	public void setMap(Map<K, V> map) {
		this.map = map;
	}
	
	public String toString(){
		return this.map.toString();
	}
}
