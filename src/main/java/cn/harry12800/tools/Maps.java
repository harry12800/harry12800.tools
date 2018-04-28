package cn.harry12800.tools;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Maps {
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
	
	public static <K, V> HashMap<K, V> newLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}
}
