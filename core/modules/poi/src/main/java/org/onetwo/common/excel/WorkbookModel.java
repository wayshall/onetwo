package org.onetwo.common.excel;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.onetwo.common.utils.LangUtils;

public class WorkbookModel implements List<TemplateModel>{
	
	private List<TemplateModel> sheets = LangUtils.newArrayList();

	public int size() {
		return sheets.size();
	}

	public boolean isEmpty() {
		return sheets.isEmpty();
	}

	public boolean contains(Object o) {
		return sheets.contains(o);
	}

	public Iterator<TemplateModel> iterator() {
		return sheets.iterator();
	}

	public Object[] toArray() {
		return sheets.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return sheets.toArray(a);
	}

	public boolean add(TemplateModel e) {
		return sheets.add(e);
	}

	public boolean remove(Object o) {
		return sheets.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return sheets.containsAll(c);
	}

	public boolean addAll(Collection<? extends TemplateModel> c) {
		return sheets.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends TemplateModel> c) {
		return sheets.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return sheets.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return sheets.retainAll(c);
	}

	public void clear() {
		sheets.clear();
	}

	public boolean equals(Object o) {
		return sheets.equals(o);
	}

	public int hashCode() {
		return sheets.hashCode();
	}

	public TemplateModel get(int index) {
		return sheets.get(index);
	}

	public TemplateModel set(int index, TemplateModel element) {
		return sheets.set(index, element);
	}

	public void add(int index, TemplateModel element) {
		sheets.add(index, element);
	}

	public TemplateModel remove(int index) {
		return sheets.remove(index);
	}

	public int indexOf(Object o) {
		return sheets.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return sheets.lastIndexOf(o);
	}

	public ListIterator<TemplateModel> listIterator() {
		return sheets.listIterator();
	}

	public ListIterator<TemplateModel> listIterator(int index) {
		return sheets.listIterator(index);
	}

	public List<TemplateModel> subList(int fromIndex, int toIndex) {
		return sheets.subList(fromIndex, toIndex);
	}

}
