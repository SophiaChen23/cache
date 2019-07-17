package com.sophia.cache;

import java.util.Map;
import java.util.Optional;
import org.apache.log4j.Logger;
public class CacheProcessor<K, V> {

	/**
	 * 0 means no expiration time.
	 */
	private int maxTTLms;
	final  static Logger logger = Logger.getLogger(CacheProcessor.class);

	Map<K, CacheData<V>> cacheDataMap;

	public CacheProcessor(int capacity, int maxTTLInSecond) {
		this.cacheDataMap = new LRUMap<>(capacity);
		this.maxTTLms = maxTTLInSecond * 1000;
		if (maxTTLms > 0) {
			Thread cleanUpThread = new Thread(()->{
				while(true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					cleanUpExpiredCache();
				}
			});
			cleanUpThread.setDaemon(true);
			cleanUpThread.start();
		}
	}

	public Optional<V> read(K key) {
		CacheData<V> cd = cacheDataMap.get(key);
		if(cd == null) return Optional.empty();
		add(key, cd.value);
		return Optional.ofNullable(cd.value);
	}

	public void add(K key, V value) {
		cacheDataMap.put(key, new CacheData<>(value));
	}

	private void cleanUpExpiredCache() {
		cacheDataMap.entrySet().stream().forEach(entry->{
			if (entry.getValue().expTime <= System.currentTimeMillis()) {
				cacheDataMap.remove(entry.getKey());
				System.out.printf("removed key %s%n", entry.getKey());
			}
		});
	}

	public class CacheData<V> {
		private final long expTime = System.currentTimeMillis() + maxTTLms;
		private long lastAccessed = System.currentTimeMillis();
		private V value;

		public long getLastAccessed() {
			return lastAccessed;
		}

		public void setLastAccessed(long lastAccessed) {
			this.lastAccessed = lastAccessed;
		}

		public CacheData(V value) {
			this.value = value;
		}
	}
}
