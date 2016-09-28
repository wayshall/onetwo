package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PagableList<E> implements List<E> {

	private long total = 0;
	private List<E> datas = new ArrayList<>();

	public PagableList() {
		super();
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<E> datas() {
		return datas;
	}

	public void datas(List<E> datas) {
		this.datas = datas;
	}

	public int size() {
		return datas.size();
	}

	public boolean isEmpty() {
		return datas.isEmpty();
	}

	public boolean contains(Object o) {
		return datas.contains(o);
	}

	public Iterator<E> iterator() {
		return datas.iterator();
	}

	public Object[] toArray() {
		return datas.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return datas.toArray(a);
	}

	public boolean add(E e) {
		return datas.add(e);
	}

	public boolean remove(Object o) {
		return datas.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return datas.containsAll(c);
	}

	public boolean addAll(Collection<? extends E> c) {
		return datas.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return datas.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return datas.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return datas.retainAll(c);
	}


	public void clear() {
		datas.clear();
	}

	public boolean equals(Object o) {
		return datas.equals(o);
	}

	public int hashCode() {
		return datas.hashCode();
	}

	public E get(int index) {
		return datas.get(index);
	}

	public E set(int index, E element) {
		return datas.set(index, element);
	}

	public void add(int index, E element) {
		datas.add(index, element);
	}


	public E remove(int index) {
		return datas.remove(index);
	}


	public int indexOf(Object o) {
		return datas.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return datas.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return datas.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return datas.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return datas.subList(fromIndex, toIndex);
	}


}
