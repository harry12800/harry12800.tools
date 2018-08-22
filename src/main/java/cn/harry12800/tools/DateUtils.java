package cn.harry12800.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * 分段时间处理"yyyy-MM"
	 * @throws ParseException
	 */
	public static String[] processMonth(String month) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date date = dateFormat.parse(month);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String[] times = new String[2];
		times[0] = dateFormat.format(date);
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		times[1] = dateFormat.format(calendar.getTime());
		return times;
	}

	/**
	 * 分段时间处理"yyyy"
	 * @throws ParseException
	 */
	public static String[] processYear(String year) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date date = dateFormat.parse(year);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String[] times = new String[2];
		times[0] = dateFormat.format(date);
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		times[1] = dateFormat.format(calendar.getTime());
		return times;
	}

	/**
	 * "yyyy-MM-dd"
	 * @throws ParseException
	 */
	public static String[] processDiy(String startTime, String endTime) throws ParseException {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat1.parse(startTime);
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String[] times = new String[2];
		times[0] = dateFormat2.format(date);
		Calendar calendar = Calendar.getInstance();
		date = dateFormat1.parse(endTime);
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		times[1] = dateFormat2.format(calendar.getTime());
		return times;
	}

	public static String getCurrTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String format = df.format(new Date());
		return format;
	}

	public static String getCurrTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String format = df.format(date);
		return format;
	}
	
	public static String getwholeCurrTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = df.format(date);
		return format;
	}
	public static String getCurrTimeByFormat(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		String time = df.format(new Date());
		return time;
	}

	public static String getTimeByFormat(Date time, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		String time1 = df.format(time);
		return time1;
	}

	public static Date getDateByFormat(String triggerTime, String format) throws ParseException {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat(format);
		Date date = dateFormat1.parse(triggerTime);
		return date;
	}

	public static Date getInitTime() {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat1.parse("1970-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
