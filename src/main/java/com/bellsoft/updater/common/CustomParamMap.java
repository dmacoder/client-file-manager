package com.bellsoft.updater.common;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class CustomParamMap {

	HashMap<String, Object> paramMap = new HashMap<String, Object>();

	public CustomParamMap() {

	}

	public CustomParamMap(HashMap<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	public Object get(String key) {
		return paramMap.get(key);
	}

	public void put(String key, Object value) {
		paramMap.put(key, value);
	}

	public Object remove(String key) {
		return paramMap.remove(key);
	}

    public boolean containsKey(String key){
        return paramMap.containsKey(key);
    }

    public boolean containsValue(Object value){
        return paramMap.containsValue(value);
    }

    public void clear(){
    	paramMap.clear();
    }

    public Set<Entry<String, Object>> entrySet(){
        return paramMap.entrySet();
    }

    public Set<String> keySet(){
        return paramMap.keySet();
    }

    public boolean isEmpty(){
        return paramMap.isEmpty();
    }

    public void putAll(HashMap<? extends String, ?extends Object> m){
    	paramMap.putAll(m);
    }

    public HashMap<String,Object> getHashMap(){
        return paramMap;
    }

    public String toString() {
    	return String.valueOf(paramMap);
    }
}
