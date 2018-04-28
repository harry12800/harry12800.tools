package cn.harry12800.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 对象属性的转化
 * @author BGYJJ440
 *
 */
public class EntityMent {
	static public final Map<String,String> db2attrMap = new HashMap<String,String>(0);
	static {
		db2attrMap.put("NVARCHAR", "String");
		db2attrMap.put("NVARCHAR2", "String");
		db2attrMap.put("VARCHAR", "String");
		db2attrMap.put("CHAR", "String");
		db2attrMap.put("LONG", "String");
		db2attrMap.put("VARCHAR2", "String");
		db2attrMap.put("NUMBER", "Integer");
		db2attrMap.put("TIMESTAMP", "Date");
		db2attrMap.put("BLOB", "byte[]");
	}
	
	
	
	/**
	 * 将对象属性转化成键值对的list
	 * @param <T>
	 * @param <T>
	 * @param <T>
	 * @param entity
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> list2Entity(List<Map<String, Object>> list,Class<?> clazz) throws Exception{
		List<T> entityList=new ArrayList<T>();
		for(Map<String, Object>map:list)
		{
			Object entity=map2Entity(map,clazz);
			entityList.add((T) entity);
		}
		return entityList;
	}
	
	 /**
	  *  将对象属性转化成键值对的map
	  * @param map
	  * @param clazz
	  * @return
	  */
	public static Object map2Entity(Map<String, Object> map,Class<?> clazz)  {
	
	     try {
			Object object = clazz.newInstance();
			for(String key:map.keySet()){
				Object value=map.get(key);
				key=columnName2EntityAttrName(key);
				Field field=null;
				try {
//				StringUtils.errorln("key:"+key+"\tvalue :"+value+"\t value.type:"+value.getClass());
					field = clazz.getDeclaredField(key);
					field.setAccessible(true);//暴力访问，取消age的私有权限。让对象可以访问
					field.set(object, value);
				
				} catch (Exception e) {
//					e.printStackTrace();
					if(value instanceof java.lang.Byte){
						if((Byte)value==0){
							field.set(object, false);
						}
						else{
							field.set(object, true);
						}
					}

					if(value instanceof java.lang.Integer){
						if((Integer)value==0){
							field.set(object, false);
						}
						else{
							field.set(object, true);
						}
					}
				
				}
			}
			return object;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 类属性名称映射成数据库字段名
	 * @param clazz
	 * @return
	 */
	public static Map<String,String> entityAttr2TableColumnMap(Class<?> clazz){
		Field[] fields = clazz.getFields();
		Map<String, String> map =new HashMap<String, String>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			map.put(field.getName(), entityAttrName2ColumnName(field.getName()));
		}
		return map;
	}
	
	
	
	/**
	 * 数据表字段名转化为对象属性名
	 * @param columnName
	 * @return
	 */
	public static String columnName2EntityAttrName(String columnName) {
		String attrName;
		byte[] bs = columnName.toLowerCase().getBytes();
		boolean isDown = false;

		attrName = "";
		for (int i = 0; i < bs.length; i++) {
			byte b = bs[i];
			if (b == 95) {
				isDown = true;
			} else {
				if (isDown) {
					b -= 32;
					isDown = false;
				}
				attrName += (char) b;
			}
		}
		if(JavaKeyWord.keywords.contains(attrName))return "_"+attrName;
		return attrName;
	}
	/**
	 * 数据表名转化为名
	 * @param tableName
	 * @return
	 */
	public static String columnName2EntityClassName(String tableName) {
		String attrName;
		byte[] bs = tableName.toLowerCase().getBytes();
		boolean isDown = false;
		attrName = "";
		for (int i = 0; i < bs.length; i++) {
			byte b = bs[i];
			if (b == 95) {
				isDown = true;
			} else {
				if (isDown) {
					b -= 32;
					isDown = false;
				}
				if(i==0&&b>96&&b<123){
					b-=32;
				}
				attrName += (char) b;
			}
		}
		if(JavaKeyWord.keywords.contains(attrName))return "_"+attrName;
		return attrName;
	}
	
	/**
	 * 将对象属性转化成键值对的map
	 * @param entityName
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap<?, ?> entity2Map(Object entity){
		try {
			if(entity instanceof Map){
				return (LinkedHashMap<?, ?>) entity;
			}
			Map<String, Object> map =getProperty(entity);
			LinkedHashMap<String,Object> mapNew=new LinkedHashMap<String, Object>();
			for(String key:map.keySet()){
				Object value = map.get(key);
				key = entityAttrName2ColumnName(key);
				mapNew.put(key, value);
			}
			return mapNew;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new LinkedHashMap<String, Object>();
	}
	
	/**
	 * 对象属性名==>数据表字段名
	 * @param columnName
	 * @return
	 */
	private static String entityAttrName2ColumnName(String key) {
		byte[] c = key.getBytes();
		key="";
		for (int i = 0; i < c.length; i++) {
			byte b = c[i];
			
			if(b<=90&&b>=65){
				key+="_"+(char)(b+32);
			}
			else{
				key+=(char)b;
			}
		}
		return key.toUpperCase();
	}
	
	
	/**
	 * 获得一个对象各个属性的字节流
	 */
	public static void printEntity(Object entityName){
		try {
			Class<?> c = entityName.getClass();
			Field field[] = c.getDeclaredFields();
			for (Field f : field) {
				Object value = invokeMethod(entityName, f.getName(), null);
				StringUtils.errorln(f.getName() + "\t===\t" + value );
			}
		} catch (SecurityException e) {
		} catch (Exception e) {
		}
	}
	/**
	 * 获得一个对象各个属性的字节流
	 */
	public static Map<String,Object> getProperty(Object entityName) throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		Class<?> c = entityName.getClass();
		Field field[] = c.getDeclaredFields();
		for (Field f : field) {
			Object value = invokeMethod(entityName, f.getName(), null);
//			StringUtils.errorln(f.getName() + "\t" + value + "\t" + f.getType());
			map.put(f.getName(), value);
		}
		return map;
	}
	/**
	 * 获得对象属性的值
	 */
	private static Object invokeMethod(Object owner, String methodName,
			Object[] args) throws Exception {
		Class<?> ownerClass = owner.getClass();
		methodName = methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + methodName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		}
		return method.invoke(owner);
	}

	public static String getDb2attrMap(String type) {
		 
		if(type.equalsIgnoreCase("TIMESTAMP"))
			return "Date";
		if(type.equalsIgnoreCase("VARCHAR")){
			return "String";
		}
		if(type.startsWith("VARCHAR")){
			return "String";
		}
		if(type.startsWith("varchar")){
			return "String";
		}
		if(type.equalsIgnoreCase("INT")){
			return "Integer";
		}
		if(type.equalsIgnoreCase("NUMBER")){
			return "Double";
		}
		if(type.equalsIgnoreCase("CHAR")){
			return "String";
		}
		if(type.equalsIgnoreCase("LONG")){
			return "String";
		}
		if(type.equalsIgnoreCase("BLOB")){
			return "byte[]";
		}
		if(type.equalsIgnoreCase("mediumtext")){
			return "String";
		}
		if(type.equalsIgnoreCase("text")){
			return "String";
		}
		if(type.equalsIgnoreCase("tinyint")){
			return "Integer";
		}
		if(type.equalsIgnoreCase("datetime")){
			return "java.util.Date";
		}
		return type;
	}
}