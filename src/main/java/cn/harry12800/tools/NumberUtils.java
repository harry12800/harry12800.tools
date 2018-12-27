package cn.harry12800.tools;

public class NumberUtils {
	
	/**
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int toInt(String value, int defaultValue) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
}
