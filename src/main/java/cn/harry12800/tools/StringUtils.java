package cn.harry12800.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public class StringUtils {
	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static final String yyyy_MM_dd_HH24_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyy_MM_dd = "yyyy-MM-dd";
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String ISO88591 = "ISO-8859-1";
	public static final String GBK = "GBK";
	public static final String UTF8 = "UTF-8";
	/**
	 * 读取配置文件
	 */
	static Properties props = new Properties();
	static {
		try {
			InputStream in = StringUtils.class
					.getResourceAsStream("/cn/harry12800/tools/StringUtils.properties");
			if (in != null)
				props.load(in);
		} catch (IOException e) {
		}
	}

	/**
	 * MD5值计算
	 * @param sourceStr
	 * @return
	 */
    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
//            System.out.println("MD5(" + sourceStr + ",32) = " + result);
//            System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
	/**
	 * 将list拼凑成Sql中的形势，例如 list中有字符串 字样 "a","b","c" 拼凑后的结果是： ('a','b','c');
	 * 
	 * @param list
	 * @return
	 */
	public static String List2String(List<String> list) {
		if (list == null || list.isEmpty())
			return "";
		StringBuffer str = new StringBuffer("(");
		for (String key : list) {
			str.append("'").append(key).append("',");
		}
		String ret = str.substring(0, str.length() - 1);
		return ret + ")";
		// return list.toString().replace("[", "('").replace(",",
		// "','").replace(" ", "").replace("]", "')");//值中如果包含空格，此种处理方式会过滤掉空格
	}

	/**
	 * 将list拼凑成Sql中的形势，例如 list中有字符串 字样 "a","b","c" 拼凑后的结果是：" where columnName in
	 * ('a','b','c');
	 * <p style="color:'red';">
	 * 特别注意 其中的or语句
	 * </p>
	 * 
	 * @param list
	 * @param columnName
	 * @return 是的
	 */
	public static String List2String(List<String> list, String columnName) {
		if (list == null || list.isEmpty())
			return "where 1=1";
		String str = " where ";
		int length = list.size();
		if (length < 1000)
			str = str + columnName + " in " + List2String(list);
		else {
			int size = 998;
			int count = (length / size) + 1;
			for (int i = 0; i < count - 1; i++) {
				str = str + columnName + " in "
						+ List2String(list.subList(i * size, (i + 1) * size))
						+ " or ";
			}
			str = str
					+ columnName
					+ " in "
					+ List2String(list.subList((count - 1) * size, list.size()));
		}
		return str;
	}

	/**
	 * 得到当前时间的字符串。"2016-04-19 13:28:34" 占十九个字符
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return new Timestamp(System.currentTimeMillis()).toString().substring(
				0, 19);
	}

	/**
	 * 得到当前时间的字符串。"2016-04-19" 占十个字符
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return new Timestamp(System.currentTimeMillis()).toString().substring(
				0, 10);
	}

	/**
	 * 得到当前时间的字符串。"201604" 占六个字符
	 * 
	 * @return
	 */
	public static String getCurrentMonth() {
		return (new Timestamp(System.currentTimeMillis()).toString().substring(
				0, 10)).replaceAll("-", "").substring(0, 6);
	}

	/**
	 * 得到day天前的时间的字符串。"2016-04-19" 占十个字符
	 * 
	 * @param day
	 * @return
	 */
	public static String getBeforeDate(int day) {
		return new Timestamp(System.currentTimeMillis() - day * 24 * 60 * 60
				* 1000).toString().substring(0, 10);
	}

	/**
	 * 
	 * 将list拼凑成Sql中的形势，例如 list中有字符串 字样 "a","b","c"
	 * 拼凑后的结果是：" fieldName in ('a','b','c') ";
	 * <p style="color:'red';">
	 * 特别注意 其中的or语句
	 * </p>
	 * 
	 * @param codes
	 * @return
	 */
	public static String getSQLInSentence(Set<String> codes, String fieldName) {
		if (codes == null || codes.isEmpty())
			return " (1!=1) ";
		StringBuffer str = new StringBuffer(" (" + fieldName + " in (");
		int i = 0;
		for (String key : codes) {
			i++;
			if (i % 999 == 0) {
				str.deleteCharAt(str.length() - 1).append(
						") or " + fieldName + " in (");
			}
			str.append("'").append(key).append("',");
		}
		String ret = str.substring(0, str.length() - 1);
		ret = ret + ")";
		return ret + ") ";
	}

	/**
	 * 
	 * @param codes
	 * @param fieldName
	 * @return
	 */
	public static String getSQLNotInSentence(Set<String> codes, String fieldName) {
		if (codes == null || codes.isEmpty())
			return " (1=1) ";
		StringBuffer str = new StringBuffer(" (" + fieldName + " not in (");
		int i = 0;
		for (String key : codes) {
			i++;
			if (i % 999 == 0) {
				str.deleteCharAt(str.length() - 1).append(
						") and " + fieldName + " not in (");
			}
			str.append("'").append(key).append("',");
		}
		String ret = str.substring(0, str.length() - 1);
		ret = ret + ")";
		return ret + ") ";
	}

	/**
	 * 
	 * 将list拼凑成Sql中的形势，例如 list中有字符串 字样 "a","b","c"
	 * 拼凑后的结果是：" fieldName in ('a','b','c') ";
	 * <p style="color:'red';">
	 * 特别注意 其中的or语句
	 * </p>
	 * 
	 * @param codes
	 * @return
	 */
	public static String getSQLInSentence(List<String> codes, String fieldName) {
		if (codes == null || codes.isEmpty())
			return " (1!=1) ";
		StringBuffer str = new StringBuffer(" (" + fieldName + " in (");
		int i = 0;
		for (String key : codes) {
			i++;
			if (i % 999 == 0) {
				str.deleteCharAt(str.length() - 1).append(
						") or " + fieldName + " in (");
			}
			str.append("'").append(key).append("',");
		}
		String ret = str.substring(0, str.length() - 1);
		ret = ret + ")";
		return ret + ") ";
	}

	/**
	 * 打印语句
	 * 
	 * @param o
	 */
	public static void outln(Object o) {
		if ("1".equals(props.get("print")))
			System.out.println(o);
	}

	/**
	 * 打印语句
	 * 
	 * @param o
	 */
	public static void errorln(Object o) {
		if ("1".equals(props.get("print")))
			System.err.println(o);
	}

	/**
	 * 打印语句
	 * 
	 * @param o
	 */
	public static void errorln() {
		System.out.println();
	}

	/**
	 * 打印语句
	 * 
	 * @param o
	 */
	public static void outlnforce(Object o) {
		System.out.println(o);
	}

	/**
	 * 打印语句
	 * 
	 * @param o
	 */
	public static void errorlnforce(Object o) {
		System.err.println(o);
	}

	/**
	 * 打印语句
	 * 
	 * @param o
	 */
	public static void printList(String title, List<String> titles) {
		if (titles == null || titles.isEmpty()) {
			System.out.println(title + ":size(" + 0 + ")");
			return;
		} else {
			System.out.print(title + ":size(" + titles.size() + ")");
			StringBuffer str = new StringBuffer("[");
			for (String string : titles) {
				str.append(string + ",");
			}
			str.setCharAt(str.length() - 1, ']');
			System.out.println(str.toString());
		}
	}

	/**
	 * 打印日志
	 * 
	 * @param o
	 */
	public static void printLog(Object o) {
		try {
			LogUtils.print(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印语句
	 * 
	 * @param o
	 */
	public static void printList(List<String> titles) {
		if (titles == null || titles.isEmpty()) {
			System.out.println("size(" + 0 + ")");
			return;
		} else {
			System.out.print("size(" + titles.size() + ")");
			StringBuffer str = new StringBuffer("[");
			for (String string : titles) {
				str.append(string + ",");
			}
			str.setCharAt(str.length() - 1, ']');
			System.out.println(str.toString());
		}
	}

	/**
	 * 如果内容中包含有特殊字符可能会出错。 将list中的map数据转换成json字符串
	 * 
	 * @param list
	 *            数据
	 * @param cols
	 *            需要的数据列
	 * @param id
	 *            主键列名称
	 * @param parentid
	 *            外键列数据
	 * @return Json字符串数据
	 */
	@SuppressWarnings("unchecked")
	public static String getJsonForMapTree(List<Map<String, Object>> list,
			Map<String, String> cols, String id, String parentid) {
		/**
		 * 建立预处理数据
		 */
		Map<String, Map<String, Object>> cache = new LinkedHashMap<String, Map<String, Object>>(
				0);
		for (Map<String, Object> map : list) {
			cache.put(map.get(id) + "", map);
		}
		/**
		 * 找到根节点
		 */
		List<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>(
				0);

		/**
		 * 遍历预处理数据
		 */
		for (Entry<String, Map<String, Object>> entry : cache.entrySet()) {
			String parentValue = entry.getValue().get(parentid) + ""; // 外键值
			Map<String, Object> tmp = cache.get(parentValue);
			if (tmp == null) { // 根据外键列去找父级，如果没有找到，说明自己是根级节点。
				rootList.add(entry.getValue());
				continue;
			}
			// 如果有父级节点的情况
			if (null == tmp.get("child_tmp")) {
				ArrayList<Map<String, Object>> childList = new ArrayList<Map<String, Object>>(
						0);
				childList.add(entry.getValue());
				tmp.put("child_tmp", childList);
			} else {
				((ArrayList<Map<String, Object>>) (tmp.get("child_tmp")))
						.add(entry.getValue());
			}
		}
		if (rootList == null || rootList.isEmpty()) {
			StringUtils.errorln("没找到root");
		}
		/**
		 * 一个根节点
		 */
		if (rootList.size() == 1)
			return realWith(rootList.get(0), cols);
		else {// 多个根节点
			StringBuffer json = new StringBuffer("[");
			for (int i = 0; i < rootList.size(); i++) {
				json.append(realWith(rootList.get(i), cols) + ",");
			}
			json.setCharAt(json.length() - 1, ']');
			return json.toString();
		}
	}

	@SuppressWarnings("unchecked")
	private static String realWith(Map<String, Object> rootMap,
			Map<String, String> cols) {
		StringBuffer json = new StringBuffer("{");
		for (Entry<String, String> col : cols.entrySet())
			// 添加属性值
			json.append(col.getValue() + ":'" + rootMap.get(col.getKey())
					+ "',");
		/* 添加孩子 */
		ArrayList<Map<String, Object>> childList = (ArrayList<Map<String, Object>>) rootMap
				.get("child_tmp");
		if (null == childList || childList.isEmpty())
			return json.substring(0, json.length() - 1) + "}";
		json.append("child:[");
		for (Map<String, Object> map : childList) {
			json.append(realWith(map, cols) + ",");
		}
		json = json.deleteCharAt(json.length() - 1);
		json.append("]}");
		return json.toString();
	}

	/**
	 * 得到32位UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 分词 使得每行只有fontLimit个字符
	 * 
	 * @param content
	 * @param fontLimit
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> split(String content, int fontLimit) {
		List<String> contentList = new ArrayList<String>(1);
		if (content == null)
			content = "";
		// content = content.replaceAll("\\s", "");
		int byteLen = 0;
		int start = 0;
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (ch > 128)
				byteLen += 2;
			else
				byteLen++;
			if (byteLen >= fontLimit * 2) {
				contentList.add(content.substring(start, i + 1));
				start = i + 1;
				byteLen = 0;
			}
		}
		if (start != content.length() || start == 0)
			contentList.add(content.substring(start, content.length()));
		return contentList;
	}

	/**
	 * 获得第一行限制词
	 * 
	 * @param content
	 * @param fontLimit
	 * @return
	 */
	public static String getFontSize(String content, int fontLimit) {
		if (content == null)
			content = "";
		content = content.replaceAll("\\s", "");
		int byteLen = 0;
		int start = 0;
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (ch > 128)
				byteLen += 2;
			else
				byteLen++;
			if (byteLen >= fontLimit * 2) {
				return content.substring(start, i + 1);
			}
		}
		return content;
	}

	/**
	 * 是不是空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		boolean flag = false;
		if (obj == null)// 一般对象
			flag = true;
		else if (obj instanceof Collection && ((Collection<?>) obj).isEmpty())// 没装数据的集合
			flag = true;
		else if (obj instanceof Map && ((Map<?, ?>) obj).isEmpty())// 没装数据的Map
			flag = true;
		else if ("".equals((obj + "").trim())
				|| "null".equals((obj + "").trim()))// 字符串判断
			flag = true;
		else if (obj.getClass().isArray() && Array.getLength(obj) <= 0) {// 数组判断
			flag = true;
		}
		return flag;
	}

	/**
	 * 打印数组
	 * 
	 * @param a
	 */
	public static void printArray(String[] a) {
		StringUtils.errorln(Arrays.toString(a));
	}

	/**
	 * 
	 * @param urlPath
	 * @return
	 */
	public static String getHttpContentByUrl(String urlPath) throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.connect();
		String content = "";
		try (DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());) {
			out.writeBytes(content);
		}
		// String content = "user=" +
		// URLEncoder.encode(SmSHelper.smsConfig.get("user"), "UTF-8");
		// content += "&pwd=" +
		// URLEncoder.encode(SmSHelper.smsConfig.get("pwd"), "UTF-8");
		// content += "&mobiles="+phone;
		// content += "&contents=" + URLEncoder.encode(smsdesc, "UTF-8");
		int responseCode = connection.getResponseCode();
		StringUtils.errorln(responseCode);
		try (InputStreamReader inputStreamReader = new InputStreamReader(
				connection.getInputStream());
				BufferedReader br = new BufferedReader(inputStreamReader);) {
			StringBuffer sb = new StringBuffer();
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		}
	}

	/**
	 * 
	 * @param urlPath
	 * @param val
	 * @return
	 */
	public static String getStringByUrl(String urlPath, Map<String, String> val)
			throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.connect();
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());
		StringBuilder content = new StringBuilder();
		for (Entry<String, String> map : val.entrySet()) {
			content.append("&" + map.getKey() + "="
					+ URLEncoder.encode(map.getValue(), "UTF-8"));
		}
		if (content.length() > 0)
			content.deleteCharAt(0);
		out.writeBytes(content.toString());
		out.flush();
		out.close();
		int responseCode = connection.getResponseCode();
		StringUtils.errorln(responseCode);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 将字符串内容根据换行符来分割成数组。
	 * 
	 * @param content
	 * @return
	 */
	public static String[] getStringList(String content) {
		return content.split("\\n");
	}

	/**
	 * 获取分成几页个数
	 * 
	 * @author sunqq
	 * @param size
	 * @param pageSize
	 * @return
	 */
	public static int getMaxPage(int size, int pageSize) {
		return size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
	}

	/**
	 * like in
	 * 
	 * @param fieldName
	 * @param whereStr
	 * @param splitStr
	 * @return
	 */
	public static String getSearchByLikeInStr(String fieldName,
			String whereStr, String splitStr) {
		String extendsSqlStr = " and (";
		String[] fieldValues = whereStr.split(splitStr);
		for (int i = 0; i < fieldValues.length; i++) {
			extendsSqlStr += fieldName + " like '%" + fieldValues[i] + "%'";
			if (i < (fieldValues.length - 1)) {
				extendsSqlStr += " or ";
			}
		}
		extendsSqlStr += ")";
		return extendsSqlStr;
	}

	/***
	 * 
	 * @param date
	 * @param dateFormat
	 *            : e.g:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatDateByPattern(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	/***
	 * convert Date to cron ,eg. "0 06 10 15 1 ? 2014"
	 * 
	 * @param date
	 *            : 时间点
	 * @return
	 */
	public static String getCronByDate(Date date) {
		String dateFormat = "ss mm HH dd MM ? yyyy";
		return formatDateByPattern(date, dateFormat);
	}

	/***
	 * convert Date to cron ,eg. "0 06 10 15 1 ? 2014"
	 * 
	 * @param date
	 *            : 时间点
	 * @return
	 */
	public static String getCronByDateStr(String dateStr) {
		Date date = StringUtils.string2date(dateStr,
				StringUtils.yyyy_MM_dd_HH_mm_ss);
		String dateFormat = "ss mm HH dd MM ? yyyy";
		return formatDateByPattern(date, dateFormat);
	}

	/**
	 * * 获取指定日期是星期几 参数为null时表示获取当前日期是星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];
	}

	public static boolean isEmpty(Object obj) {
		return (obj == null) ? true : false;
	}

	/**
	 * 判断是否为空 if null || "" return true else false;
	 * 
	 * @return
	 */
	public static boolean isEmpty(String obj) {
		if (obj == null || "".equals(obj) || "undefined".equals(obj)
				|| "null".equals(obj) || "NULL".equals(obj)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * * 获取指定日期是星期几 参数为null时表示获取当前日期是星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(String dateStr) {
		Date date = StringUtils.string2date(dateStr, StringUtils.yyyyMMdd);
		String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];
	}

	/**
	 * @param source
	 *            日期
	 * @param dateFormat
	 *            日期格式
	 * @return
	 */

	public static Date string2date(String source, String dateFormat) {
		Date d = null;
		if (isEmpty(source)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			d = sdf.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStrig(Date dateDate, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * @param source
	 *            日期
	 * @param dateFormat
	 *            日期格式
	 * @return
	 */
	public static java.sql.Date string2BySqldate(String source,
			String dateFormat) {
		Date d = null;
		if (isEmpty(source)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			d = sdf.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sqlDate = new java.sql.Date(d.getTime());
		return sqlDate;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateBySqlToString(java.sql.Date dateDate,
			String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将科学计数法转换成为字符串，保留几位
	 * 
	 * @param doubleStr
	 * @param newScale
	 * @param roundingMode
	 * @return
	 */
	public static String doubleByEstr(String doubleStr, int newScale,
			int roundingMode) {
		BigDecimal bigDecilmal = new BigDecimal(doubleStr);
		doubleStr = bigDecilmal.setScale(newScale, roundingMode)
				.toPlainString();
		return doubleStr;
	}

	/**
	 * 将科学计数法转换成为字符串
	 * 
	 * @param doubleStr
	 * @param newScale
	 * @param roundingMode
	 * @return
	 */
	public static String doubleByEstr(double doubleStr) {
		BigDecimal bigDecilmal = new BigDecimal(doubleStr);
		return bigDecilmal.toPlainString();
	}

	/**
	 * 将科学计数法转换成为字符串，保留几位
	 * 
	 * @param doubleStr
	 * @param newScale
	 * @param roundingMode
	 * @return
	 */
	public static double doubleByEDouble(String doubleStr, int newScale,
			int roundingMode) {
		BigDecimal bigDecilmal = new BigDecimal(doubleStr);
		bigDecilmal.setScale(newScale, roundingMode);
		return bigDecilmal.doubleValue();
	}

	/**
	 * 将字符串省略掉中间的部分
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String ellipsisMid(String str, int len) {
		if (str.length() <= len || len <= 4)
			return str;
		else if (str.length() > len) {
			return str.substring(0, len / 2 - 2) + "...."
					+ str.substring(str.length() - len / 2 + 2);
		}
		return str;
	}

	public static void printMap(Map<?, ?> map) {
		for (Entry<?, ?> enty : map.entrySet()) {
			StringUtils.errorlnforce(enty.getKey() + ":" + enty.getValue());

		}
	}

	/**
	 * 首字母转大写
	 * 
	 * @param cs
	 * @return
	 */
	public static String capitalize(String cs) {
		if (cs == null)
			return null;
		if (cs.length() == 0)
			return "";
		char ch = cs.charAt(0);
		if (ch <= 'z' && ch >= 'a') {
			return (char) (ch - 32) + cs.substring(1);
		}
		return cs;
	}

	/**
	 * 逗号分隔
	 * 
	 * @param list
	 * @return
	 */
	public static String getCommaMerge(List<String> list) {
		StringBuffer sBuffer = new StringBuffer();
		for (String string : list) {
			sBuffer.append(string).append(",");
		}
		if (sBuffer.length() > 1)
			sBuffer.deleteCharAt(sBuffer.length() - 1);
		return sBuffer.toString();
	}

	/**
	 * 逗号分隔
	 * 
	 * @param list
	 * @return
	 */
	public static String getCommaMerge(String[] list) {
		StringBuffer sBuffer = new StringBuffer();
		for (String string : list) {
			sBuffer.append(string).append(",");
		}
		if (sBuffer.length() > 1)
			sBuffer.deleteCharAt(sBuffer.length() - 1);
		return sBuffer.toString();
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0)
			return true;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(cs.charAt(i)))
				return false;

		return true;
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static boolean isNotBlank(String cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0)
			return false;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(cs.charAt(i)))
				return true;
		return false;
	}
	/**
	 * 去掉标点符号切割
	 * @param content
	 * @return
	 */
	public static List<String> split(String content) {
		String[] authors = content.split("[，；：;, \t:?!]|(\r\n)");
		List<String> asList = Arrays.asList(authors);
		List<String> result  = Lists.newArrayList();
		for (String string : asList) {
			if(!string.trim().equals("")){
				result.add(string);
			}
		}
		return result;
	}

	/**
	 * 计算字符串相似度
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static double calcSimilar(String a,String b) {
		int la = a.length();
		int lb = b.length();
		int N = la > lb ? la : lb;
		int dp[][] = new int[N + 1][N + 1];
		int apath[] = new int[N + 1];
		int bpath[] = new int[N + 1];
		for (int i = 1; i <= la; i++) {
			for (int j = 1; j <= lb; j++) {
				if (a.charAt(i - 1) ==b.charAt(j - 1)){
					dp[i][j] = dp[i - 1][j - 1] + 1;
				}else{
					dp[i][j] = dp[i - 1][j] > dp[i][j - 1] ? dp[i - 1][j]
							: dp[i][j - 1];
				}
					
			}
		}
		
		/* 
         * 矩阵中，如果matrix[m][n]的值不等于matrix[m-1][n]的值也不等于matrix[m][n-1]的值， 
         * 则matrix[m][n]对应的字符为相似字符元，并将其存入result数组中。 
         */  
		int i = la, j = lb, k = 0;
		while (dp[i][j] != 0) {
			//满足头两个条件证明文本不同
			if (dp[i][j] == dp[i - 1][j])
				i--;
			else if (dp[i][j] == dp[i][j - 1])
				j--;
			else {
				//如果文本相同,那么储存相似节点值的id为上次循环中摄入的值,存该id
				apath[k] = i - 1;
				bpath[k++] = j - 1;
				i--;
				j--;
			}
		}
		System.out.println("similar length is" + dp[la][lb]);
//		for (int t = k - 1; t >= 0; t--)
//			System.out.print(listTitles1[apath[t]].info_name + ",");
//		
//		System.out.println();
//		for (int t = k - 1; t >= 0; t--)
//			System.out.print(listTitles2[bpath[t]].info_name + ",");
//		过滤文本内容中的特殊字符
//		for (int t = k - 1; t >= 0; t--)
//		{
//			String str1 = "";
//			String str2 = "";
//			if (listTitles1[apath[t]].content != null)
//				str1 = listTitles1[apath[t]].content
//						.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "")
//						.replaceAll("</[a-zA-Z]+[1-9]?>", "")
//						.replaceAll("\\s", "").replaceAll("&nbsp;", "");
//			if (listTitles2[bpath[t]].content != null)
//				str2 = listTitles2[bpath[t]].content
//						.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "")
//						.replaceAll("</[a-zA-Z]+[1-9]?>", "")
//						.replaceAll("\\s", "").replaceAll("&nbsp;", "");
//			if (str1.trim().equals(str2.trim())) {
//				listTitles1[apath[t]].isSimilar = 1;
//			}
//		}
		return 1.0*dp[la][lb]*2/(a.length()+b.length());
	}
//	public static void main(String[] args) {
//		String content= "adsfa,a s d f:aasdf?,a;,ds ：asdf a,asf d\r\nasdf?asdf";
//List<String> split = split(content);
//for (String string : split) {
//	System.out.println(string);
//}
//	}
	/**
	 * 去掉文件名后缀
	 * @param title
	 * @return
	 */
	public static String moveSuffix(String title) {
		if(title == null){
			return "";
		}
		int lastIndexOf = title.lastIndexOf(".");
		if(lastIndexOf<0)
			return title;
		return title.substring(0, lastIndexOf);
	}
}
