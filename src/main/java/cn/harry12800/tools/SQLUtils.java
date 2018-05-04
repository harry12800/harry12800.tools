package cn.harry12800.tools;

import java.util.List;
import java.util.Set;

public class SQLUtils {

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

	public static String getOraclePageSQL(String sql, int pageSize, int pageIdex) {
		String s = "SELECT * FROM   " +
				"(  " +
				"SELECT A.*, ROWNUM RN   " +
				"FROM (" + sql + ") A   " +
				"WHERE ROWNUM <= " + pageSize * pageIdex +
				")  " +
				"WHERE RN > " + (pageIdex - 1) * pageSize;
		return s;
	}

	public static String getMysqlPageSQL(String sql, int pageSize, int pageIdex) {
		String s = "SELECT * " +
				"FROM (" + sql + ") A   " +
				"limit " + (pageIdex - 1) * pageSize + "," + pageSize;
		return s;
	}

	public static void main(String[] args) {
		String pageSQL = getMysqlPageContent("select * from aa");
		System.err.println(pageSQL);
	}

	public static String getMysqlPageContent(String sql) {
		String s = "SELECT count(*)FROM (" + sql + ") A";
		return s;
	}

}
