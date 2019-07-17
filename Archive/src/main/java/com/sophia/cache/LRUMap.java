package com.sophia.cache;

import java.util.concurrent.ConcurrentHashMap;

public class LRUMap<K,V extends CacheProcessor.CacheData> extends ConcurrentHashMap<K,V> {

	private int capacity;

	public LRUMap(int capacity) {
		super();
		this.capacity = capacity;
	}

	public V put(K key, V value) {
		if(this.size() >= capacity){
			this.remove(
					this.entrySet().stream()
							.min((e1, e2) -> (int)(e1.getValue().getLastAccessed() - e2.getValue().getLastAccessed()))
							.get()
							.getKey()
			);
		}
		return super.put(key, value);
	}

	@Override
	public V get(Object key) {
		V res = super.get(key);
		if(res != null) {
			res.setLastAccessed(System.currentTimeMillis());
		}
		return res;
	}
}
