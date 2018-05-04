package cn.harry12800.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

public class NetworkUtils {
	private final static long PART1 = 0xff000000;
	private final static long PART2 = 0xff0000;
	private final static long PART3 = 0xff00;
	private final static long PART4 = 0xff;

	/**
	 * long 值转 字符串ip
	 * 
	 * @param ipv4
	 * @return
	 */
	public static String getIpStringByLongValue(long ipv4) {
		String result = String.valueOf((ipv4 & PART1) >> 24);
		result += "." + ((ipv4 & PART2) >> 16);
		result += "." + ((ipv4 & PART3) >> 8);
		result += "." + (ipv4 & PART4);
		return result;
	}

	/**
	 * 将字符串的ipv4地址转成long值
	 * 
	 * @param ipv4
	 * @return
	 */
	public static long getLongValueByIp(String ipv4) {
		String[] addr = ipv4.split("[.]");
		long a = Integer.valueOf(addr[0]);
		long b = Integer.valueOf(addr[1]);
		long c = Integer.valueOf(addr[2]);
		long d = Integer.valueOf(addr[3]);
		long result = d | c << 8 | b << 16 | a << 24;
		return result;
	}

	/**
	 * 得到外网Ip
	 * @return
	 * @throws Exception 
	 */
	public static String getOuterIp() throws Exception {
		String str = StringUtils.getHttpContentByUrl("https://ipip.yy.com/get_ip_info.php");
		Pattern p = Pattern.compile("(\\d+[.]\\d+[.]\\d+[.]\\d+)");
		Matcher m = p.matcher(str);
		if (m.find())
			return m.group(0);
		else {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		//	 StringUtils.errorln(getOuterIp());
		StringUtils.errorln(getMySQLServerIp());
	}

	/**
	 * 获取MySQL服务器的地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<String> getMySQLServerIp() throws Exception {
		List<String> ips = getIp();
		List<String> result = new ArrayList<String>(0);
		StringUtils.errorln(ips);
		for (int i = 0; i < ips.size(); i++) {
			if (portIsOpen(ips.get(i), 3306))
				result.add(ips.get(i));
		}
		return result;
	}

	/**
	 * 获取端口开发的所有局域网IP
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public static List<String> getPortServerIp(int port) throws Exception {
		List<String> ips = getIp();
		List<String> result = new ArrayList<String>(0);
		StringUtils.errorln(ips);
		for (int i = 0; i < ips.size(); i++) {
			if (portIsOpen(ips.get(i), port))
				result.add(ips.get(i));
		}
		return result;
	}

	/**
	 * 得到某局域网ip
	 * @param ipv4
	 * @return
	 * @throws Exception
	 */
	public static List<String> getSomeIntranetIp(String ipv4) throws Exception {
		List<String> section = getAllIp(ipv4);
		return section;
	}

	public static List<String> getSomeIntranetIp(String ipv4, int port) throws Exception {
		List<String> ips = getAllIp(ipv4);
		List<String> result = new ArrayList<String>(0);
		StringUtils.errorln(ips);
		for (int i = 0; i < ips.size(); i++) {
			if (portIsOpen(ips.get(i), port))
				result.add(ips.get(i));
		}
		return result;
	}

	/**
	 * 得到局域网的Ip
	 * @return
	 * @throws Exception
	 */
	public static List<String> getIp() throws Exception {
		List<String> ips = getPingIps();
		List<String> ip = new ArrayList<String>(0);
		for (int i = 0; i < ips.size(); i++) {
			List<String> section = getAllIp(ips.get(i));
			ip.addAll(section);
		}
		StringUtils.errorln("本机局域网IP:" + ip);
		return ip;
	}

	/**
	 * 查看IP主机的port端口是否开发
	 * @param ip
	 * @param port
	 * @return
	 */
	public static boolean portIsOpen(String ip, int port) {
		Socket client = null;
		try {
			client = new Socket(ip, port);
			StringUtils.errorln("端口已开");
			client.close();
			return true;
		} catch (Exception e) {
			StringUtils.errorln("端口未开");
			return false;
		}
	}

	/**
	 * 根据阻塞线程来获取每�?个能ping通的�?域网Ip
	 * 
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	private static List<String> getAllIp(String ip) throws Exception {
		final List<String> resultIp = new ArrayList<String>(0);
		if (ip.matches("\\d+.\\d+.\\d+.\\d+")) {
			if (ip.startsWith("127"))
				return resultIp;
			StringUtils.errorln("本机地址：" + ip);
			final String newip = ip.substring(0, ip.lastIndexOf(".") + 1);
			ExecutorService pool = Executors.newFixedThreadPool(255);
			List<Callable<String>> list = new ArrayList<Callable<String>>();

			for (int i = 0; i <= 255; i++) {
				final int x = i;
				Callable<String> c = new Callable<String>() {
					@Override
					public String call() throws Exception {
						String realIp = newip + x;
						// InetAddress inetAddress = null;
						if (ping(realIp, 1, 5000)) {
							resultIp.add(realIp);
							return realIp;
						}
						return null;
						// if (inetAddress.isReachable(5000)) {
						// System.out
						// .println( "HostName:"
						// + inetAddress.getHostName()
						// + " Ip:"
						// + inetAddress.getHostAddress() );
						// resultIp.add(inetAddress.getHostAddress());
						// return inetAddress.getHostAddress();
						// }

					}

				};
				list.add(c);
			}
			try {
				pool.invokeAll(list);
			} finally {
				pool.shutdown();
			}
			return resultIp;
		} else
			return resultIp;
	}

	/**
	 * 获取本机ipv4 的网卡网址
	 * 
	 * @return
	 * @throws SocketException
	 */
	private static List<String> getPingIps() throws SocketException {

		List<String> ip = new ArrayList<String>(0);
		try {
			Enumeration<NetworkInterface> interfaceList = NetworkInterface
					.getNetworkInterfaces();
			if (interfaceList == null) {
				StringUtils.errorln("--No interface found--");
			} else {
				while (interfaceList.hasMoreElements()) {
					NetworkInterface iface = interfaceList.nextElement();
					// StringUtils.errorln("Interface "+iface.getName()+":");
					Enumeration<InetAddress> addrList = iface
							.getInetAddresses();
					if (!addrList.hasMoreElements()) {
						// StringUtils.errorln("\t(No address for this address)");
					}
					while (addrList.hasMoreElements()) {
						InetAddress address = addrList.nextElement();
						// System.out.print("\tAddress "+((address instanceof
						// InetAddress? "v4":(address instanceof Inet6Address ?
						// "(v6)":"(?)"))));
						// StringUtils.errorln(":"+address.getHostAddress());
						if (address instanceof InetAddress)
							ip.add(address.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			StringUtils.errorln("Error getting network interfaces:"
					+ e.getMessage());
			e.printStackTrace();
		}
		StringUtils.errorln("本机ipv4 的网卡网址:" + ip);
		return ip;
	}

	/**
	 * 查询端口，时候能ping通
	 * @param ipAddress  地址
	 * @param pingTimes ping的次数
	 * @param timeOut 超时时间
	 * @return 是否ping通
	 */
	public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
		BufferedReader in = null;
		Runtime r = Runtime.getRuntime(); // 将要执行的ping命令,此命令是windows格式的命令
		String pingCommand = "";
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w "
					+ timeOut;
		} else {
			pingCommand = "ping " + ipAddress + " -c " + pingTimes + " -w "
					+ timeOut;
		}
		StringUtils.errorln(pingCommand);
		try { // 执行命令并获取输出
			StringUtils.errorln(pingCommand);
			Process p = r.exec(pingCommand);
			if (p == null) {
				return false;
			}
			in = new BufferedReader(new InputStreamReader(p.getInputStream())); // 逐行检查输出,计算类似出现=23ms
																				// TTL=62字样的次数
			int connectedCount = 0;
			String line = null;
			int x = 0;
			while ((line = in.readLine()) != null) {
				// StringUtils.errorln(line);
				connectedCount += getCheckResult(line);
				x++;
				if (x >= 20)
					return false;
			} // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
			return connectedCount == pingTimes;
		} catch (Exception ex) {
			ex.printStackTrace(); // 出现异常则返回假
			return false;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *  若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
	 * @param line
	 * @return
	 */
	private static int getCheckResult(String line) { // StringUtils.errorln("控制台输出的结果为:"+line);
		if (line != null && (line.contains("TTL") || line.contains("ttl")))
			return 1;
		Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			return 1;
		}
		return 0;
	}

	/**
	 * 
	 * @param urlPath
	 * @param val
	 * @return
	 */
	public static String getStringByUrl(String urlPath, Map<String, String> val) throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.connect();
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		StringBuilder content = new StringBuilder();
		for (Entry<String, String> map : val.entrySet()) {
			content.append("&" + map.getKey() + "=" + URLEncoder.encode(map.getValue(), "UTF-8"));
		}
		if (content.length() > 0)
			content.deleteCharAt(0);
		out.writeBytes(content.toString());
		out.flush();
		out.close();
		int responseCode = connection.getResponseCode();
		StringUtils.errorln(responseCode);
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static void saveToFile(String destUrl, String path) throws Exception {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		try {
			url = new URL(destUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.setConnectTimeout(5000);
			httpUrl.setReadTimeout(1000 * 5); //设置读取超时
			httpUrl.setRequestProperty("Accept", "*/*");
			httpUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; CIBA)"); //模拟ie浏览器
			httpUrl.setRequestProperty("Accept-Language", "zh-cn");
			httpUrl.connect();
			//httpUrl.setRequestProperty("Connection", "close"); //不进行持久化连接
			bis = new BufferedInputStream(httpUrl.getInputStream());
			File file = new File(path);
			file.createNewFile();
			fos = new FileOutputStream(file);
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			fos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				fos.close();
				bis.close();
				httpUrl.disconnect();
			} catch (Exception e) {
			}
		}
	}

	public static ImageIcon getImageByUrl(String destUrl) throws Exception {
		return getImageByUrl(new URL(destUrl));
	}

	public static ImageIcon getImageByUrl(URL url) throws Exception {
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		FileOutputStream fos = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		try {
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.setConnectTimeout(5000);
			httpUrl.setReadTimeout(1000 * 5); //设置读取超时
			httpUrl.setRequestProperty("Accept", "*/*");
			httpUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; CIBA)"); //模拟ie浏览器
			httpUrl.setRequestProperty("Accept-Language", "zh-cn");
			httpUrl.connect();
			//httpUrl.setRequestProperty("Connection", "close"); //不进行持久化连接
			bis = new BufferedInputStream(httpUrl.getInputStream());

			byte[] byteArray = FileUtils.toByteArray(bis);
			System.out.println(byteArray.length);
			ByteArrayInputStream in = new ByteArrayInputStream(byteArray); //将b作为输入流；
			//			BufferedImage image = ImageIO.read(  in);     //将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
			ImageIcon sdIcon = new ImageIcon(byteArray);
			return sdIcon;
			//			return image;
			//			File file = new File(path);
			//			file.createNewFile();
			//			fos = new FileOutputStream(file);
			//			while ((size = bis.read(buf)) != -1) {
			//				fos.write(buf, 0, size);
			//			}
			//			FileUtils.
			//			fos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				//				fos.close();
				bis.close();
				httpUrl.disconnect();
			} catch (Exception e) {
			}
		}
	}

	public static ImageIcon getIco(String protocol, String host) throws Exception {
		host = protocol + "://" + host + "/favicon.ico";
		return getImageByUrl(host);
	}

}
