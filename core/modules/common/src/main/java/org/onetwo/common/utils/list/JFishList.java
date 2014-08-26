package org.onetwo.common.utils.list;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.SimpleBlock;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;
import org.onetwo.common.utils.map.ListMap;


@SuppressWarnings("unchecked")
public class JFishList<E> implements List<E>, Serializable {
	private static final String SELF_KEY = ":this";
	
	
	public static class EachContext {
		private int total;
		private int index;
		
		public boolean isFinished(){
			return total == index;
		}
		
		public boolean isBreak(){
			return total != index;
		}
		
	}
	

	public static <T> JFishList<T> wrap(Collection<T> list){
		if(JFishList.class.isInstance(list))
			return (JFishList<T>)list;
		if(list==null)
			return newList();
		return new JFishList<T>(list);
	}
	

	public static <T> JFishList<T> wrap(Collection<?> list, SimpleBlock<Object, T> block){
		if(list==null)
			return newList();
		return new JFishList<T>(list, block);
	}
	
	public static <T> JFishList<T> wrap(T... e){
		return new JFishList<T>(e);
	}
	
	public static JFishList<Integer> intList(int start, int length){
		JFishList<Integer> list = newList(length);
		int end = start + length;
		for (int i = start; i < end; i++) {
			list.add(i);
		}
		return list;
	}
	
	public static <T> JFishList<T> wrapObject(Object e){
		if(JFishList.class.isInstance(e))
			return (JFishList<T>)e;
		
		List<T> list = LangUtils.asList(e);
		return new JFishList<T>(list);
	}
	
	public static <T> JFishList<T> create(){
		return new JFishList<T>();
	}
	
	public static <T> JFishList<T> newList(){
		return new JFishList<T>();
	}
	
	public static <T> JFishList<T> newList(int size){
		return new JFishList<T>(size);
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3308735018101552664L;

	private ArrayList<E> list;
	
	public JFishList(){
		super();
		list = new ArrayList<E>();
	}
	
	public JFishList(int size){
		list = new ArrayList<E>(size);
	}

	public JFishList(E...objects){
		if(LangUtils.isEmpty(objects)){
			list = new ArrayList<E>();
		}else{
			list = new ArrayList<E>(objects.length+5);
			for(E e : objects){
				list.add(e);
			}
		}
	}
	
	public ArrayList<E> getList() {
		return list;
	}


	public JFishList<E> addArray(E...objects) {
		if(LangUtils.isEmpty(objects))
			return this;
		for(E e : objects)
			add(e);
		return this;
	}

	public JFishList<E> addArrayIgnoreNull(E...objects) {
		if(LangUtils.isEmpty(objects))
			return this;
		for(E e : objects)
			if(e!=null)
				add(e);
		return this;
	}
	
	public JFishList<E> addCollection(Collection<E> objects) {
		if(LangUtils.isEmpty(objects))
			return this;
		for(E e : objects){
			add(e);
		}
		return this;
	} 
	public JFishList<E> addCollectionIgnoreNull(Collection<E> objects) {
		if(LangUtils.isEmpty(objects))
			return this;
		for(E e : objects){
			if(e!=null)
				add(e);
		}
		return this;
	} 
	
	public JFishList(Collection<E> col){
		Assert.notNull(col);
		list = new ArrayList<E>(col.size()+5);
		list.addAll(col);
	}
	
	public JFishList(Collection<?> col, SimpleBlock<Object, E> block){
		Assert.notNull(col);
		Assert.notNull(block);
		list = new ArrayList<E>(col.size()+5);
		for(Object ele : col){
			E e = block.execute(ele);
			if(e!=null)
				add(e);
		}
	}
	
	public JFishList<E> addWith(Object obj, SimpleBlock<Object, E> block){
		E e = block.execute(obj);
		if(e!=null){
			add(e);
		}
		return this;
	}
	
	public <T> T[] asArray(Class<T> arrayClass){
		T[] arrays = (T[])Array.newInstance(arrayClass, size());
		return toArray(arrays);
	}
	
	public String asString(String separator){
		return StringUtils.join(this, separator);
	}

	public JFishList<E> sort(Comparator<? super E> c){
		Collections.sort(this, c);
		return this;
	}

	public JFishList<E> sortByProperty(final String property){
		Collections.sort(this, new Comparator<E>() {
		    public int compare(E o1, E o2){
		    	Comparable<Object> p1 = (Comparable<Object>) ReflectUtils.getProperty(o1, property);
		    	Comparable<Object> p2 = (Comparable<Object>) ReflectUtils.getProperty(o2, property);
		    	return p1.compareTo(p2);
		    }
		});
		return this;
	}

	public <T> List<T> getPropertyList(final String name){
		final List<T> propValues = new JFishList<T>();
		this.each(new NoIndexIt<E>() {

			@Override
			protected void doIt(E element) {
				T val = (T)ReflectUtils.getExpr(element, name);
				if(val==null)
					return ;
				propValues.add(val);
			}
			
		});
		return propValues;
	}

	public void addPropertiesTo(final String name, final Collection<Object> cols){
		this.each(new NoIndexIt<E>() {

			@Override
			protected void doIt(E element) {
				Object val = ReflectUtils.getExpr(element, name);
				if(val==null)
					return ;
				cols.add(val);
			}
			
		});
	}
	
	public EachContext each(It<E> it){
		EachContext c = new EachContext();
		c.total = size();
		c.index = 0;
		for(E e : list){
//			try {
			if(!it.doIt(e, c.index)){
				break;
			}
			/*} catch (Exception e2) {
				throw new BaseException("iterator has breaked. ", e2);
			}*/
			c.index++;
		}
		return c;
	}
	
	private class SumResult {
		
		private Double total = 0D;

		public Double getTotal() {
			return total;
		}

		public void setTotal(Double total) {
			this.total = total;
		}
		
	}
	
	public String join(final String joiner, final SimpleBlock<E, String> block){
		final StringBuilder str = new StringBuilder();
		each(new It<E>() {

			@Override
			public boolean doIt(E element, int index) {
				if(index!=0)
					str.append(joiner);
				str.append(block.execute(element));
				return true;
			}
			
		});
		return str.toString();
	}

	public String join(final String joiner, String prop){
		final String propName = StringUtils.isBlank(prop)?SELF_KEY:prop;
		return join(joiner, new SimpleBlock<E, String>() {

			@Override
			public String execute(E object) {
				Object value = SELF_KEY.equals(propName)?StringUtils.emptyIfNull(object):ReflectUtils.getProperty(object, propName);
				return value==null?"":value.toString();
			}
			
		});
	}
	
	public Double sum(final SimpleBlock<E, Double> block){
		final SumResult total = new SumResult();
		each(new NoIndexIt<E>() {

			@Override
			protected void doIt(E element) throws Exception {
				Double rs = block.execute(element);
				if(rs!=null)
					total.setTotal(total.getTotal()+rs);
			}
			
		});
		return total.getTotal();
	}
	
	public Double sum(final String propName){
		return sum(new SimpleBlock<E, Double>() {

			@Override
			public Double execute(E object) {
				Object value = ReflectUtils.getProperty(object, propName);
				return value==null?null:Types.convertValue(value, Double.class);
			}
			
		});
	}

	public <K, V> Map<K, V> toMap(final String keyProperty){
		return toMap(keyProperty, null, false);
	}

	public <K, V> Map<K, V> toMap(final String keyProperty, final String valueProperty, final boolean throwIfRepeatKey){
		final Map<K, V> maps = LangUtils.newHashMap();
		each(new NoIndexIt<E>() {

			@Override
			protected void doIt(E element) throws Exception {
				K k = (K)(StringUtils.isBlank(keyProperty)?element:ReflectUtils.getProperty(element, keyProperty));
				Assert.notNull(k);
				if(throwIfRepeatKey && maps.containsKey(k))
					throw new BaseException("key["+k+"] has exist, it's value : " + maps.get(k));
				V v = (V)(StringUtils.isBlank(valueProperty)?element:ReflectUtils.getProperty(element, valueProperty));
				maps.put(k, v);
			}
			
		});
		return maps;
	}
	
	public <K> ListMap<K, E> groupBy(final SimpleBlock<E, K> block){
		final ListMap<K, E> maps = ListMap.newLinkedListMap();
		each(new NoIndexIt<E>() {

			@Override
			protected void doIt(E element) throws Exception {
				if(element==null)
					return ;
				K rs = block.execute(element);
				if(rs!=null)
					maps.putElement(rs, element);
			}
			
		});
		return maps;
	}
	
	public <K> ListMap<K, E> groupBy(final String propName){
		return groupBy(new SimpleBlock<E, K>() {

			@Override
			public K execute(E object) {
				return (K)ReflectUtils.getProperty(object, propName);
			}
			
		});
	}
	
	public boolean isPropertyEqualsAllOfElement(String propertyOfElement){
		ListMap<String, E> map = this.groupBy(propertyOfElement);
		return map.size()==1;
	}
	
	/*public void doInResult(It<E> it, final List<?> result){
		List<String> strs = new ArrayList<String>();
		this.each(new It<E>(){

			@Override
			public boolean doIt(E element, int index) throws Exception {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
	}*/

	public boolean all(final PredicateBlock<E> block){
		EachContext c = this.each(new It<E>() {

			@Override
			public boolean doIt(E element, int index) {
				return block.evaluate(element, index);
			}
			
		});
		return c.isFinished();
	}

	public boolean any(final PredicateBlock<E> block){
		EachContext c = this.each(new It<E>() {

			@Override
			public boolean doIt(E element, int index) {
				if(block.evaluate(element, index)){
					return false;
				}
				return true;
			}
			
		});
		return c.isBreak();
	}
	
	
	public void addEnumeration(Enumeration<E> enums){
		if(enums==null)
			return ;
		while(enums.hasMoreElements()){
			add(enums.nextElement());
		}
	}

	public boolean isNotEmpty() {
		return !list.isEmpty();
	}

	public void trimToSize() {
		list.trimToSize();
	}

	public void ensureCapacity(int minCapacity) {
		list.ensureCapacity(minCapacity);
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	public Iterator<E> iterator() {
		return list.iterator();
	}

	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	public Object clone() {
		return list.clone();
	}

	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	public Object[] toArray() {
		return list.toArray();
	}

	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
	
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	public E get(int index) {
		return list.get(index);
	}

	public E set(int index, E element) {
		return list.set(index, element);
	}

	public boolean add(E e) {
		return list.add(e);
	}

	public void add(int index, E element) {
		list.add(index, element);
	}

	public String toString() {
		return list.toString();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	public E remove(int index) {
		return list.remove(index);
	}

	public boolean remove(Object o) {
		return list.remove(o);
	}

	public boolean equals(Object o) {
		return list.equals(o);
	}

	public void clear() {
		list.clear();
	}

	public boolean addAll(Collection<? extends E> c) {
		return list.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return list.addAll(index, c);
	}

	public int hashCode() {
		return list.hashCode();
	}

}
