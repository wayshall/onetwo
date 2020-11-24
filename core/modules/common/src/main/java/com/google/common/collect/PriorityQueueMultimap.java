package com.google.common.collect;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class PriorityQueueMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {

	public static <K, V> PriorityQueueMultimap<K, V> create(Comparator<? super V> comparator) {
		return new PriorityQueueMultimap<K, V>(comparator);
	}
	  
	private Comparator<? super V> comparator;
	
	protected PriorityQueueMultimap(Comparator<? super V> comparator) {
		super(new HashMap<K, Collection<V>>());
	}

	@Override
	PriorityQueue<V> createCollection() {
		if (comparator==null) {
			return new PriorityQueue<V>();
		} else {
			return new PriorityQueue<V>(comparator);
		}
	}

	@Override
	public PriorityQueue<V> get(K key) {
		return (PriorityQueue<V>) super.get(key);
	}

}
