package com.sophia.cache;
import java.io.*;
import com.sophia.Service;
import com.sophia.ServiceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


public class CachedServiceImpl implements Service {
	private static String crunchify_file_location = "/Users";
	private static Gson gson = new Gson();

	private CacheProcessor<String, Object> firstLevelCache;

	private ServiceImpl target = new ServiceImpl();
	//using atomic integer because this maybe used in a multithreading environment later
	private AtomicInteger cache1stSize = new AtomicInteger(0);

	public CachedServiceImpl(int capacity, int maxTTLInSecond) {

		firstLevelCache = new CacheProcessor<>(capacity, maxTTLInSecond);
	}

	@Override
	public Object get(String key) {
		//read from cache
		Object cached = firstLevelCache.read(key);

		if (firstLevelCache != null) {
			return firstLevelCache.read(key).orElseGet(() -> target.get(key));
		}
		// if 1st level miss, check 2nd level
		Object secondLevelCache = readCacheFile(key);
		if (secondLevelCache != null) {
			// if 2nd level hit, promote 2nd to 1st
			//here is a problem: promoting data will not check size > capacity. //TODO
			firstLevelCache.add(key, secondLevelCache);
			return secondLevelCache;//.get(key).orElseGet(() -> target.get(key));
		}
		return target.get(key);
	}

	@Override
	public void put(String key, Object value) {
		//update target real data.
		target.put(key, value);
		// update 1st level cache
		firstLevelCache.add(key, value);
		cache1stSize.incrementAndGet();
		//maintain the size of 1st level cache.
		//if size is bigger than capacity.
		//we need to evict some of the 1st level to 2nd level.
		//we use LRU starategy here.


	}


	private Object readCacheFile(String key) {
		//Assuming that there is a data access service with the following API signature…
		// this code will read cache file.
		// Save data to file

		return null;
	}
}
