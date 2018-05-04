package cn.harry12800.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ExceptionUtils {

	//	public static final String configfilename= "cn/harry12800/tools/ExceptionUtils.properties";
	public static ExceptionUtils instance = new ExceptionUtils();
	/**
	 * 读取配置文件
	 */
	static Properties props = new Properties();
	static String srcFilePath = "";
	static String configFilePath = "";
	static Map<String, String> configMap = new HashMap<String, String>(0);
	//	static {
	//		try {
	////			cheackConfigFile(configfilename);
	//			configFilePath = StringUtils.class.getResource("").getPath()
	//					+ "cn.harry12800.tools/ExceptionUtils.properties";
	//			FileInputStream in = new FileInputStream(configFilePath);
	//			props.load(in);
	//			props.entrySet();
	//			for (Entry<Object, Object> s : props.entrySet()) {
	//				configMap.put(s.getKey()+"", s.getValue()+"");
	//				StringUtils.errorln(s.getValue());
	//			}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	private ExceptionUtils() {
	}

	public static void cheackConfigFile(String fileName) throws IOException {
		srcFilePath = FileUtils.createFile(ExceptionUtils.class, fileName);
		StringUtils.errorln(srcFilePath);
	}

	public static synchronized void addException(String exceptionCode, Exception e) {
		if (configMap.containsKey(exceptionCode))
			return;
		// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
		try (FileWriter writer = new FileWriter(srcFilePath, true);) {
			writer.write(exceptionCode + "=" + e.getMessage() + "\r\n");
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ExceptionUtils.addException("10010", new Exception("中文的"));
		ExceptionUtils.addException("100d01", new Exception("asdasd"));
	}
}
