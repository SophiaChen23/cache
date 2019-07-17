package com.sophia.test;

import com.sophia.Service;
import com.sophia.cache.CachedServiceImpl;
import org.junit.Test;
import org.apache.log4j.Logger;
public class TestCache {
	final  static Logger logger = Logger.getLogger(TestCache.class);

	@Test
	public void testCache() {
		Service service = new CachedServiceImpl(10, 0);
		service.put("A","Dummy string");
		service.put("B","Dummy string2");

		logger.info(service.get("A"));
		logger.info(service.get("B"));
	}

	@Test
	public void testCacheExpired() throws InterruptedException {
		Service service = new CachedServiceImpl(10, 3);
		service.put("A","Dummy string");
		service.put("B","Dummy string2");

		logger.info(service.get("A"));
		logger.info(service.get("B"));
		Thread.sleep(5000);
		logger.info(service.get("A"));

	}

	@Test
	public void testCacheFull() throws InterruptedException {
		Service service = new CachedServiceImpl(2, 0);
		service.put("A","Dummy string");
		service.put("B","Dummy string2");
		service.put("C","Dummy string3");


		logger.info(service.get("A"));
		logger.info(service.get("B"));
		logger.info(service.get("C"));


	}
}
