package org.onetwo.common.utils.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.map.IncreaseMap;


@SuppressWarnings("unchecked")
@Deprecated
public class EasyList<T>{
	
	private Collection<T> list;
	private Object defaultValue; 
	
	private Object result; 
	private Iterator<T> iterator;
	private boolean _break;
	private int currentIndex;
	
	private ListFun<T> block;
	
	private TriggerFun<T> timeTrigger;
	
	public EasyList(T...objects){
		this(L.aslist(objects), null);
	}
	
	public EasyList(Collection<T> list){
		this(list, null);
	}
	
	public EasyList(Collection<T> list, Object defaultValue){
		this(list, false, defaultValue);
	}

	
	public EasyList(Collection<T> list, boolean clone){
		this(list, clone, null);
	}
	
	public EasyList(Collection<T> list, boolean clone, Object defaultValue){
		if(clone)
			this.list = new ArrayList<T>(list);
		else{
			this.list = (List)list;
			/*if(List.class.isAssignableFrom(list.getClass()))
				this.list = (List)list;
			else
				this.list = new ArrayList<T>(list);*/
		}
		this.defaultValue = defaultValue;
	}
	
	public EasyList<T> again(Object...objects){
		return this.each(block, objects);
	}
	
	protected void setDefaultResult(){
		this.result = defaultValue;
	}
	
	protected void reset(){
		this.currentIndex = 0;
		this._break = false;
		this.setDefaultResult();
		this.iterator = null;
	}
	
	public EasyList<T> trigger(TriggerFun<T> timeTrigger){
		this.timeTrigger = timeTrigger;
		return this;
	}
	
	public EasyList<T> each(ListFun<T> block, Object...objects){
		this.block = block;
		this.reset();
		if(list==null || list.isEmpty())
			return this;
		currentIndex = 0;
		iterator = getIterator();
		T element = null;
		
		try{
			while(iterator.hasNext()){
				element = iterator.next();
				block.exe(element, currentIndex, this, objects);
				if(this.timeTrigger!=null && this.timeTrigger.isTriggered(element, currentIndex, this, objects)){
					this.timeTrigger.exe(element, currentIndex, this, objects);
				}
				if(isBreak()){
					break;
				}
				if(!isLast())
					currentIndex++;
			}
		}catch(Exception e){
			whenExcepted(e);
		}
		finally{
			if(result==null)
				setDefaultResult();
		}
		return this;
	}
	
	
	private static ListFun AllPredicateBlockInst = new ListFun(){

		@Override
		public void exe(Object element, int index, EasyList easytor, Object... objects) {
			PredicateBlock pblock = (PredicateBlock) objects[0];
			Assert.notNull(pblock, "no block!");
			Boolean rs = pblock.evaluate(element, index);
			if(!rs){
				easytor.setBreak();
			}
			easytor.setResult(rs);
		}
		
	};
	
	private static ListFun AnyPredicateBlockInst = new ListFun(){

		@Override
		public void exe(Object element, int index, EasyList easytor, Object... objects) {
			PredicateBlock pblock = (PredicateBlock) objects[0];
			Assert.notNull(pblock, "no block!");
			Boolean rs = pblock.evaluate(element, index);
			if(rs){
				easytor.setBreak();
			}
			easytor.setResult(rs);
		}
		
	};
	
	public boolean all(PredicateBlock<T> block){
		return (Boolean)this.each(AllPredicateBlockInst, block).getResult();
	}
	
	public boolean any(PredicateBlock<T> block){
		return (Boolean)this.each(AnyPredicateBlockInst, block).getResult();
	}
	
	public void whenExcepted(Exception e){
		e.printStackTrace();
	}
	
	public String toString(){
		final  StringBuilder sb = new StringBuilder("{\n");
		each(new ListFun<T>(){

			@Override
			public void exe(T element, int index, EasyList<T> easytor, Object... objects) {
				sb.append("\t").append("element[").append(element).append("], index[").append(index).append("]\n");
			}
			
		});
		return sb.append("}").toString();
	}
	
	public List<T> getList() {
		if(!List.class.isAssignableFrom(list.getClass()))
			return new ArrayList<T>(list);
		return (List)list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Iterator<T> getIterator() {
		if(this.iterator==null)
			this.iterator = this.list.iterator();
		return iterator;
	}
	
	public int size(){
		return this.list.size();
	}
	
	public boolean isFirst(){
		return this.currentIndex==0;
	}
	
	public boolean isLast(){
		return this.currentIndex == size()-1;
	}
	
	public Integer sum(final String name){
		return sum(name, 0);
	}

	public Integer sum(final String name, Integer def){
		return (Integer)this.each(new ListFun<T>(){

			@Override
			public void exe(T element, int index, EasyList<T> easytor, Object... objects) {
				Object val =  ReflectUtils.getFieldValue(element, name, false);
				if(val==null)
					return ;
				Integer count = MyUtils.simpleConvert(val, Integer.class);
				easytor.setResult(easytor.getResult(0)+count);
			}
			
		}).getResult(def);
	}

	public List getList(final String name){
		return getList(name, null);
	}
	
	public List getList(final String name, List def){
		return (List)this.each(new ListFun<T>(){
			private List rslist = new ArrayList();
			
			@Override
			public void exe(T element, int index, EasyList<T> easytor, Object... objects) {
				easytor.setResult(rslist);
				Object val =  ReflectUtils.getProperty(element, name, false);
				if(val==null)
					return ;
				if(rslist.contains(val))
					return ;
				rslist.add(val);
			}
			
		}).getResult(def);
	}
	
	public IncreaseMap groupby(final String propName){
		final IncreaseMap results = new IncreaseMap();
		this.each(new ListFun<T>(){

			@Override
			public void exe(T element, int index, EasyList<T> easytor, Object... objects) {
				Object pvalue = ReflectUtils.getProperty(element, propName, false);
				results.increasePut(pvalue, element);
			}
			
		});
		return results;
	}

	public <E> E getResult() {
		return (E)getResult(null);
	}

	public <E> E getResult(E def) {
		if(result==null)
			return def;
		return (E)result;
	}

	public void setResult(Object result) {
		if(this.result==result)
			return ;
		this.result = result;
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

	public void setBlock(ListFun<T> block) {
		this.block = block;
	}
	
}
