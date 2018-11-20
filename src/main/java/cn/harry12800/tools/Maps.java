package cn.harry12800.tools;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Maps {
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static <K, V> HashMap<K, V> newLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}
	/**
     * Creates a new fastest {@link ConcurrentMap} implementation for the current platform.
     */
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }
}
