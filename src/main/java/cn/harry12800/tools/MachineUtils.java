package cn.harry12800.tools;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JFrame;

public class MachineUtils {

	/**
	 * 取屏幕大小(去掉任务栏的高度部分)
	 * 
	 * @return
	 */
	public static Rectangle getScreenSize() {
		JFrame t = new JFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// 上面这种方式获取的是整个显示屏幕的大小，包含了任务栏的高度。
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(t.getGraphicsConfiguration());
		Rectangle desktopBounds = new Rectangle(screenInsets.left, screenInsets.top,
				screenSize.width - screenInsets.left - screenInsets.right,
				screenSize.height - screenInsets.top - screenInsets.bottom);
		return desktopBounds;
	}

	/**
	 * java(Swing)获取任务栏的高度
	 */
	public static int getTaskBarHeight() {
		JFrame t = new JFrame();
		// 获取屏幕边界
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(t.getGraphicsConfiguration());
		// 取得底部边界高度，即任务栏高度
		int taskHeight = screenInsets.bottom;
		return taskHeight;
	}

	public static void main2(String[] args) {
		File[] roots = File.listRoots();
		double constm = 1024 * 1024 * 1024;
		double total = 0d;
		for (File _file : roots) {
			StringUtils.errorln(_file.getPath());
			StringUtils.errorln("剩余空间 = " + doubleFormat(_file.getFreeSpace() / constm) + " G");
			StringUtils.errorln("已使用空间 = " + doubleFormat(_file.getUsableSpace() / constm) + " G");
			StringUtils.errorln(_file.getPath() + "盘总大小 = " + doubleFormat(_file.getTotalSpace() / constm) + " G");
			StringUtils.errorln();
			total += _file.getTotalSpace();
		}
		StringUtils.errorln("你的硬盘总大小 = " + doubleFormat(total / constm));
	}

	public static List<String> getPrintName() {
		List<String> list = new ArrayList<String>(0);
		HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		// 设置打印格式，因为未确定类型，所以选择autosense
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		for (PrintService printService2 : printService) {
			list.add(printService2.getName());

		}
		return list;
	}

	/**
	 * <p>
	 * 最大内存（maxMemory）是通过启动JAVA虚拟机时使用参数-Xmx100m指定的，而输出也确实是100m。
	 * 这表示JVM的堆内存最大可以使用104071168字节
	 * 。已分配内存（totalMemory）jvm使用的内存都是从本地系统获取的，但是通常jvm刚启动的时候
	 * ，并不会向系统申请全部的内存。而是根据所加载的Class和相关资源的容量来决定的
	 * 。在本例中，由于只在一个main()主方法中执行了上面的几行简单的代码。所以JVM只申请了5177344字节的内存。
	 * <p>
	 * 已分配内存中的剩余空间(freeMemory) 这是相对以分配内存（totalMemeory）计算的，相当于totalMemory -
	 * 已使用内存。当freeMemory 快要接近0时，以分配的内存即将耗尽，JVM会决定再次向系统申请更多的内存。
	 * <p>
	 * 最大可用内存 （usable）这是JVM真正还可以再继续使用的内存（不考虑之后垃圾回收再次得到的内存）。由【最大内存 - 已分配内存 +
	 * 已分配内存中的剩余空间】计算得到。
	 * 
	 * @param args
	 */

	public static String getWorkSpacePath() {
		String workspace = System.getProperty("user.dir");
		workspace = workspace.substring(0, workspace.lastIndexOf(File.separator));
		workspace = workspace.replaceAll("\\\\", "/");
		StringUtils.errorln("工作控件路径：" + workspace);
		return workspace;
	}

	public static String getProjectPath() {
		String workspace = System.getProperty("user.dir");
		workspace = workspace.replaceAll("\\\\", "/");
		return workspace;
	}

	/**
	 * 得到电脑最大剩余空间的磁盘路径
	 * 
	 * @return
	 */
	public static String getComputerMaxSpaceChkdsk() {
		File[] roots = File.listRoots();
		String chkdsk = "";
		long x = -1;
		for (File _file : roots) {
			if (_file.getFreeSpace() > x) {
				x = _file.getFreeSpace();
				chkdsk = _file.getPath();
			}
		}
		return chkdsk;
	}

	/**
	 * 得到电脑的中空间
	 * 
	 * @return
	 */
	public static long getComputerSpaceSize() {
		File[] roots = File.listRoots();
		long total = 0;
		for (File _file : roots) {
			total += _file.getTotalSpace();
		}
		return total;
	}

	/**
	 * 得到电脑的中空间
	 * 
	 * @return
	 */
	public static long getUsedMemery() {
		Runtime run = Runtime.getRuntime();
		long total = run.totalMemory();
		return total / 1024 / 1024;
	}

	public static String doubleFormat(double d) {
		DecimalFormat df = new DecimalFormat("0.###");
		return df.format(d);
	}

	public static void printSystemProperties() {
		Properties props = System.getProperties(); // 系统属性
		StringUtils.errorln("Java的运行环境版本：" + props.getProperty("java.version"));
		StringUtils.errorln("Java的运行环境供应商：" + props.getProperty("java.vendor"));
		StringUtils.errorln("Java供应商的URL：" + props.getProperty("java.vendor.url"));
		StringUtils.errorln("Java的安装路径：" + props.getProperty("java.home"));
		StringUtils.errorln("Java的虚拟机规范版本：" + props.getProperty("java.vm.specification.version"));
		StringUtils.errorln("Java的虚拟机规范供应商：" + props.getProperty("java.vm.specification.vendor"));
		StringUtils.errorln("Java的虚拟机规范名称：" + props.getProperty("java.vm.specification.name"));
		StringUtils.errorln("Java的虚拟机实现版本：" + props.getProperty("java.vm.version"));
		StringUtils.errorln("Java的虚拟机实现供应商：" + props.getProperty("java.vm.vendor"));
		StringUtils.errorln("Java的虚拟机实现名称：" + props.getProperty("java.vm.name"));
		StringUtils.errorln("Java运行时环境规范版本：" + props.getProperty("java.specification.version"));
		StringUtils.errorln("Java运行时环境规范供应商：" + props.getProperty("java.specification.vender"));
		StringUtils.errorln("Java运行时环境规范名称：" + props.getProperty("java.specification.name"));
		StringUtils.errorln("Java的类格式版本号：" + props.getProperty("java.class.version"));
		StringUtils.errorln("Java的类路径：" + props.getProperty("java.class.path"));
		StringUtils.errorln("加载库时搜索的路径列表：" + props.getProperty("java.library.path"));
		StringUtils.errorln("默认的临时文件路径：" + props.getProperty("java.io.tmpdir"));
		StringUtils.errorln("一个或多个扩展目录的路径：" + props.getProperty("java.ext.dirs"));
		StringUtils.errorln("操作系统的名称：" + props.getProperty("os.name"));
		StringUtils.errorln("操作系统的构架：" + props.getProperty("os.arch"));
		StringUtils.errorln("操作系统的版本：" + props.getProperty("os.version"));
		StringUtils.errorln("文件分隔符：" + props.getProperty("file.separator")); // 在
																				// 系统中是”／”
		StringUtils.errorln("路径分隔符：" + props.getProperty("path.separator")); // 在
																				// 系统中是”:”
		StringUtils.errorln("行分隔符：" + props.getProperty("line.separator")); // 在
		StringUtils.errorln("用户的账户名称：" + props.getProperty("user.name"));
		StringUtils.errorln("用户的主目录：" + props.getProperty("user.home"));
		StringUtils.errorln("用户的当前工作目录：" + props.getProperty("user.dir"));
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			StringUtils.errorln(entry.getKey() + ":" + entry.getValue());
		}

	}

	public static String getDefaultPrintName() {
		PrintService lookupDefaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
		if (lookupDefaultPrintService != null)
			return lookupDefaultPrintService.getName();
		else {
			return null;
		}
	}

	public static long getRestMemery() {
		Runtime run = Runtime.getRuntime();
		long free = run.freeMemory();
		return free / 1024 / 1024;
	}

	/**
	 * 根据类名获取当前的类 是在jar包中，还是在文件目录中。
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean getByClass(Class<?> clazz) {
		String simpleName = clazz.getPackage().getName();
		return getByClass(simpleName.replace(".", "/"));
	}

	/**
	 * 根据包名获取当前的类 是在jar包中，还是在文件目录中。
	 * 
	 * @param packageDirName
	 * @return
	 */
	public static boolean getByClass(String packageDirName) {
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					return false;
				}
				// System.err.println("file类型的扫描");
				// 获取包的物理路径
				// String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				// 以文件的方式扫描整个包下的文件 并添加到集合中
				// findAndAddClassesInPackageByFile(packageName, filePath,
				// recursive, classes);
				else if ("jar".equals(protocol)) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static String runtimeErr(String cmd) throws IOException {
		Process exec = Runtime.getRuntime().exec(cmd);
		BufferedReader in = new BufferedReader(new InputStreamReader(exec.getErrorStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		return sb.toString();
	}

	public static String runtimeOut(String cmd) throws IOException {
		Process exec = Runtime.getRuntime().exec(cmd);
		BufferedReader in = new BufferedReader(new InputStreamReader(exec.getInputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		return sb.toString();
	}

	public static String[] runtimeOutErr(String cmd) throws IOException {
		Process exec = Runtime.getRuntime().exec(cmd);
		BufferedReader in = new BufferedReader(new InputStreamReader(exec.getInputStream(), "GBK"));
		StringBuffer sb1 = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			sb1.append(line).append("\r\n");
		}
		in = new BufferedReader(new InputStreamReader(exec.getErrorStream(), "GBK"));
		StringBuffer sb2 = new StringBuffer();
		while ((line = in.readLine()) != null) {
			sb2.append(line).append("\r\n");
		}
		return new String[] { sb1.toString(), sb2.toString() };
	}

	public static void main(String[] args) {
		try {
			runtimeCmd("ipconfig /all");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String[] runtimeCmd(String cmd) throws IOException {
		ProcessBuilder pb = new ProcessBuilder();
		Process start = pb.command(cmd.split(" ")).start();
		BufferedReader in = new BufferedReader(new InputStreamReader(start.getInputStream(), "GBK"));
		StringBuffer sb1 = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			sb1.append(line).append("\r\n");
		}
		in = new BufferedReader(new InputStreamReader(start.getErrorStream(), "GBK"));
		StringBuffer sb2 = new StringBuffer();
		while ((line = in.readLine()) != null) {
			sb2.append(line).append("\r\n");
		}
		return new String[] { sb1.toString(), sb2.toString() };
	}

	public static String runtimeOut(String cmd[]) throws IOException {
		Process exec = Runtime.getRuntime().exec(cmd);
		System.out.println(Runtime.getRuntime());
		BufferedReader in = new BufferedReader(new InputStreamReader(exec.getInputStream(), "GBK"));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		return sb.toString();
	}

	public static void runtime(String cmd) throws IOException {
		Runtime.getRuntime().exec(cmd);
	}

	public static void reStart() {
		try {
			String clazz = System.getProperty("sun.java.command");
			if (new File(clazz).exists()) {
				String file = System.getProperty("java.class.path");
				System.out.println("打开的File:" + file);
				String cmd = "java -jar " + file;
				System.out.println(cmd);
				runtimeCmd(cmd);
				return;
			}
			Class<?> forName = null;
			try {
				forName = Thread.currentThread().getContextClassLoader().loadClass(clazz);
				// forName = Class.forName(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			boolean byClass = MachineUtils.getByClass(forName);
			if (byClass) {
				String file = System.getProperty("java.class.path");
				System.out.println("打开的File:" + file);
				String cmd = "java -jar " + file;
				System.out.println(cmd);
				runtimeCmd(cmd);
			} else {
				// MachineUtils.printSystemProperties();
				String file = System.getProperty("java.class.path");
				String file1 = System.getProperty("java.library.path");
				// System.out.println("打开的File:"+file);
				// System.out.println("classpath:"+clazz);
				file1 = file1.substring(0, file1.length() - 1);
				String cmd2 = "cmd /c java -Dfile.encoding=utf-8 -classpath %classpath% " + clazz;
				String string2 = "cmd /c set classpath=" + file + " && cmd /c echo %classpath% &&" + cmd2;
				System.out.println("cmd:" + string2);
				String[] runtimeOutErr = MachineUtils.runtimeCmd(string2);
				System.out.println(runtimeOutErr[0]);
				System.err.println(runtimeOutErr[1]);
				// string = MachineUtils.runtimeErr(string2);
				// System.out.println(string);
				// System.out.println(string);
				// MachineUtils.runtime("cmd /c set path=%cd%");
				// MachineUtils.runtime("cmd /c cd c:");
				// string = MachineUtils.runtimeOut("cmd /c echo %cd%");
				// System.out.println("cd:"+string);
				// string = MachineUtils.runtimeOut("cmd /c echo %CLASSPATH%");
				// System.out.println("1:"+string);
				// Process exec2 = Runtime.getRuntime().exec(cmd2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}