package cn.harry12800.tools;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegularExpression {

	/**
	 * 匹配Ipv4地址
	 */
	public final static String ip = "(\\d+[.]\\d+[.]\\d+[.]\\d+)(\\s+)(\\d+[.]\\d+[.]\\d+[.]\\d+)(\\s+)(.*)";
	/**
	 * 匹配Ipv4地址
	 */
	public final static String ipv4 = "(\\d+[.]\\d+[.]\\d+[.]\\d+)";
	/**
	 * 匹配所有除去A和SUb标签的html标签
	 */
	public final static String htmlLabel = "<(?!(a|sub|/a|/sub))[^>]+>";
	/**
	 * 匹配第一个非空位置
	 */
	public final static String firstTrimLabel = "((\\s)*)(.*)";

	public static String realContent(Set<String> set, Object object) {
		String content = object == null ? "" : object.toString();
		String regEx_html = "<(?!(a|sub|/a|/sub))[^>]+>"; // 定义HTML标签的正则表达式
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(content);
		content = m_html.replaceAll(""); // 过滤html标签
		content = content.trim();
		// content = StringUtils.getFontSize(content,160)+"...";
		StringBuffer keyRegex = new StringBuffer();
		keyRegex.append("(");
		for (String key : set) {
			if (!"".equals(key)) {
				keyRegex.append("(").append(escapeExprSpecialWord(key)).append(")").append("|");
			}
		}
		keyRegex.deleteCharAt(keyRegex.length() - 1);
		keyRegex.append(")");
		return replace(content, keyRegex.toString());
	}

	public static String replace(String str, String keyRegex) {
		StringBuffer htmlSB = new StringBuffer();
		htmlSB.append(">");
		htmlSB.append(str);
		htmlSB.append("<");
		String reg = ">[^<]*?<";
		String html = htmlSB.toString();
		Pattern p_html = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(html);
		StringBuffer sb = new StringBuffer();
		int t = 0;
		while (m_html.find()) {
			String group = m_html.group();
			int indexOf = htmlSB.indexOf(group);
			sb.append(htmlSB.substring(t, indexOf));
			htmlSB.delete(0, indexOf + group.length());
			StringBuffer sb1 = new StringBuffer();
			sb1.append(">");
			sb1.append(group.substring(1, group.length() - 1).replaceAll(keyRegex, "<font color='red'>"
					+ "$1" + "</font>"));
			sb1.append("<");
			sb.append(sb1);
		}
		sb.deleteCharAt(0);
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String escapeExprSpecialWord(String keyword) {
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?",
				"^", "{", "}", "|" };
		for (String key : fbsArr) {
			if (keyword.contains(key)) {
				keyword = keyword.replace(key, "\\" + key);
			}
		}
		return keyword;
	}

	/**
	 * 处理字符串,将查询到的内容,去掉html标签.只获取八十个字符.并把关键词中的内容替换成红色加粗.
	 * @param keyWord
	 * @param object
	 * @return
	 */
	public static String realContent(String keyWord, Object object) {
		String content = object == null ? "" : object.toString();
		String regEx_html = "<(?!(a|sub|/a|/sub))[^>]+>"; // 定义HTML标签的正则表达式
		// String regEx_html = "<[^(>|a|sub)]+>"; // 定义HTML标签的正则表达式
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(content);
		content = m_html.replaceAll(""); // 过滤html标签
		content = content.trim();
		// content = StringUtils.getFontSize(content,160)+"...";
		Set<String> keys = getKey(keyWord.split(" "));
		for (String key : keys) {
			if (!"".equals(key))
				content = content.replaceAll(key, "<B style='color:red;'>"
						+ key + "</B>");
		}
		return content;
	}

	/**
	 * 分词
	 * 
	 * @param split
	 * @return
	 */
	public static Set<String> getKey(String[] split) {
		Set<String> set = new HashSet<String>(0);
		for (String string : split) {
			if (!"".equals(string.trim())) {
				set.add(string);
			}
		}
		return set;
	}
}
