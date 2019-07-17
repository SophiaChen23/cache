package com.sophia;

import java.util.HashMap;
import java.util.Map;

public class ServiceImpl implements Service {

	Map<String, Object> data = new HashMap<String, Object>();

	public Object get(String key) {
		try {
			System.out.printf("Getting real data using key : %s%n", key);
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return data.get(key);
	}

	public void put(String key, Object value) {
		try {
			System.out.printf("Putting data using key - value : %s - %s%n", key, value);
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		data.put(key, value);
	}
}
