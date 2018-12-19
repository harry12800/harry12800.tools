package cn.harry12800.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class FileUtils {
	/**
	 * 
	 * @param map
	 * @param filePath
	 * @throws IOException
	 */
	public static void map2Properties(Map<String, String> map, String filePath) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> enty : map.entrySet()) {
			sb.append(enty.getKey() + "=" + enty.getValue() + "\r\n");
		}
		File file = new File(filePath);
		FileUtils.createFile(filePath);
		try (FileOutputStream fos = new FileOutputStream(file); Writer out = new OutputStreamWriter(fos, "UTF-8");) {
			out.write(sb.toString());
		}
	}

	/**
	 * 将map转化成文件写入
	 * 
	 * @param map
	 * @param file
	 * @throws IOException
	 */
	public static void map2Properties(Map<String, String> map, File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> enty : map.entrySet()) {
			sb.append(enty.getKey() + "=" + enty.getValue() + "\r\n");
		}
		try (FileOutputStream fos = new FileOutputStream(file); Writer out = new OutputStreamWriter(fos, "UTF-8");) {
			if (!file.exists()) {
				file.createNewFile();
			}
			out.write(sb.toString());
		}
	}

	/**
	 * 将文件中的键值对转化成map
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> properties2Map(InputStream in) throws IOException {
		Map<String, String> map = new HashMap<String, String>(0);
		Properties props = new Properties();
		props.load(in);
		for (Entry<Object, Object> entry : props.entrySet()) {
			String key = new String((entry.getKey() + "").getBytes("ISO8859-1"), "utf-8").trim();
			String value = new String((entry.getValue() + "").getBytes("ISO8859-1"), "utf-8").trim();
			// StringUtils.errorln(key+":"+value);
			map.put(key, value);// 配置文件必须设置成utf-8格式，并支持中文
		}
		return map;
	}

	/**
	 * 修改配置
	 * 
	 * @param file
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void modifyProperties(File file, String key, String value) throws Exception {
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		prop.setProperty(key, value);
		FileOutputStream outputFile = new FileOutputStream(file);
		prop.store(outputFile, "modify");
	}

	public static Map<String, String> file2Map(String filePath) throws IOException {
		Map<String, String> map = new LinkedHashMap<String, String>(0);
		List<String> rowByFile = getRowByFile(new File(filePath));
		for (String str : rowByFile) {
			String[] split = str.split("=");
			if (split.length != 2)
				continue;
			map.put(split[0].trim(), split[1].trim());
		}
		return map;
	}

	public static Map<String, String> file2Map(String filePath, String charset) throws IOException {
		Map<String, String> map = new LinkedHashMap<String, String>(0);
		List<String> rowByFile = getRowByFile(new File(filePath), charset);
		for (String str : rowByFile) {
			String[] split = str.split("=");
			if (split.length != 2)
				continue;
			map.put(split[0].trim(), split[1].trim());
		}
		return map;
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> properties2Map(String filePath) throws IOException {
		Map<String, String> map = new HashMap<String, String>(0);
		Properties props = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		System.out.println(in);
		props.load(in);
		for (Entry<Object, Object> entry : props.entrySet()) {
			String key = new String((entry.getKey() + "").getBytes("ISO8859-1"), "utf-8").trim();
			String value = new String((entry.getValue() + "").getBytes("ISO8859-1"), "utf-8").trim();
			StringUtils.errorln(key + ":" + value);
			map.put(key, value);// 配置文件必须设置成utf-8格式，并支持中文
		}
		return map;

	}

	/**
	 * 方法说明：创建目录
	 * 
	 * @param path
	 */
	public static void createDirectory(String path) {
		File file = new File(path); // 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			StringUtils.errorln("//不存在");
			boolean b = file.mkdirs();
			// boolean b = file .mkdir();
			StringUtils.errorln("b===" + b);
			StringUtils.errorln("创建目录啦" + path);
		} else {
			// StringUtils.outln("//目录存在");
		}
	}

	/**
	 * 方法说明：创建文件
	 * 
	 * @param pathFile
	 */
	public static void createFile(String pathFile) {
		File file = new File(pathFile);
		createFile(file);
	}

	/**
	 * 方法说明：创建文件
	 * 
	 * @param file
	 */
	public static void createFile(File file) {
		createDirectory(file.getParentFile().getAbsolutePath());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}

		}
		// 目录此时为空，可以删除, is file, directly delete.
		return dir.delete();
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(String dirPath) {
		File dir = new File(dirPath);
		return deleteDir(dir);
	}

	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			return file.delete();
		return true;
	}

	/**
	 * 得到文件的每一行内容存入list
	 * 
	 * @param file
	 * @return
	 */
	public static List<String> getRowByFile(File file, String charset) {
		List<String> list = new ArrayList<String>(0);
		String line;
		try (FileInputStream in = new FileInputStream(file);
				InputStreamReader read = new InputStreamReader(in, charset);
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 方法说明：递归删除目录及子目录
	 * 
	 * @param filepath
	 */
	public static boolean delPath(String filepath) {
		boolean mark = true;
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {
			// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				mark = mark && f.delete();
			} else {
				// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						mark = mark && delPath(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
				mark = mark && f.delete();
			}
		}
		return mark;
	}

	/**
	 * 将本目录的文件全部复制到des目录下
	 * 
	 * @param src
	 * @param des
	 * @throws Exception
	 */
	public static void copyDirectory(String src, String des) throws Exception {
		File file = new File(src);
		File file1 = new File(des);
		if (!file.exists()) {
			StringUtils.errorln(file.getName() + "文件不存在");
		}
		byte[] b = new byte[(int) file.length()];
		if (file.isFile()) {

			if (!file1.exists()) {
				file1.getParentFile().mkdirs();
				file1.createNewFile();
			}
			try (FileInputStream is = new FileInputStream(file); FileOutputStream ps = new FileOutputStream(file1);) {
				is.read(b);
				ps.write(b);
			}
		} else if (file.isDirectory()) {
			if (!file.exists())
				file.mkdir();
			String[] list = file.list();
			for (int i = 0; i < list.length; i++) {
				copyDirectory(src + "/" + list[i], des + "/" + list[i]);
			}
		}
	}

	/**
	 * 复制文件到目录下。
	 * 
	 * @param srcFilePath
	 * @param destDirPath
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String srcFilePath, String destDirPath) throws IOException {
		// 被移动的文件夹
		File file = new File(srcFilePath);
		return copyFile(srcFilePath, destDirPath, file.getName());
	}

	/**
	 * 复制文件到目录下。
	 * 
	 * @param srcFilePath
	 * @param destDirPath
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String srcFilePath, String destDirPath, String name) throws IOException {
		// 被移动的文件夹
		File file = new File(srcFilePath);
		// 目标文件夹
		File dir = new File(destDirPath);
		if (!dir.exists())
			dir.mkdir();
		// 将文件移动到另一个文件目录下
		File descFile = new File(dir.getAbsolutePath() + File.separator + name);
		if (!descFile.exists())
			descFile.createNewFile();
		byte[] b = new byte[(int) file.length()];
		try (FileInputStream is = new FileInputStream(file); FileOutputStream ps = new FileOutputStream(descFile);) {
			is.read(b);
			ps.write(b);
		}
		boolean success = file.renameTo(new File(dir, file.getName()));
		return success;
	}

	/**
	 * 复制文件到目录下。
	 * 
	 * @param srcFilePath
	 * @param destDirPath
	 * @return
	 * @throws IOException
	 */
	public static boolean copyIOFile(File src, File desc) throws IOException {
		// 将文件移动到另一个文件目录下
		if (!desc.exists())
			desc.createNewFile();
		byte[] b = new byte[(int) src.length()];
		try (FileInputStream is = new FileInputStream(src); FileOutputStream ps = new FileOutputStream(desc);) {
			is.read(b);
			ps.write(b);
		}
		return true;
	}

	/**
	 * 将文件夹移动到另一个位置
	 * 
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public static boolean moveFiles(String srcPath, String destPath) {
		// 被移动的文件夹
		File file = new File(srcPath);
		// 目标文件夹
		File dir = new File(destPath);
		// 将文件移动到另一个文件目录下
		boolean success = file.renameTo(new File(dir, file.getName()));
		StringUtils.errorln(success);
		return success;
	}

	/**
	 * 把文件<code>file</code>移到dirpath文件夹下
	 * 
	 * @param file
	 * @param dirPath
	 */
	public static void moveFile(File file, String dirPath) {
		if (!file.exists()) {
			StringUtils.errorln("文件不存在！没法移走！");
		}
		createDirectory(dirPath);
		String path = new File(dirPath).getAbsolutePath() + "\\" + file.getName();
		StringUtils.errorln(path);
		file.renameTo(new File(path));
	}

	/**
	 * 把文件<code>file</code>移到dirpath文件夹下
	 * 
	 * @param file
	 * @param dirPath
	 */
	public static boolean moveFile(File file, String dirPath, boolean force) {
		if (!file.exists()) {
			StringUtils.errorln("文件不存在！没法移走！");
		}
		createDirectory(dirPath);
		String path = new File(dirPath).getAbsolutePath() + File.separator + file.getName();
		StringUtils.errorln(path);
		if (force) {
			File desfile = new File(path);
			if (desfile.exists() && desfile.isFile()) {
				desfile.delete();
				StringUtils.errorln("删除原有文件");
			}
		}
		boolean renameTo = file.renameTo(new File(path));
		System.out.println("renameto失败！@");
		if (!renameTo) {
			try {
				FileUtils.copyIOFile(file, new File(path));
				file.delete();
			} catch (IOException e) {
				System.out.println("IO失败lew！@");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * 把第一个路径下的移到dirpath文件夹下
	 * 
	 * @param file
	 * @param dirPath
	 */
	public static void moveFile(String file, String dirPath) {
		moveFile(new File(file), dirPath);
	}

	/**
	 * 把第一个路径下的移到dirpath文件夹下
	 * 
	 * @param file
	 * @param dirPath
	 */
	public static void moveFile(String file, String dirPath, boolean force) {
		moveFile(new File(file), dirPath, force);
	}

	/**
	 * 得到文件的每一行内容存入list
	 * 
	 * @param file
	 * @return
	 */
	public static List<String> getRowByFile(File file) {
		List<String> list = new ArrayList<String>(0);
		String line;
		try (FileInputStream in = new FileInputStream(file);
				InputStreamReader read = new InputStreamReader(in, "gbk");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 得到文件的每一行内容存入list
	 * 
	 * @param file
	 * @return
	 */
	public static List<String> getRowByStream(InputStream in) {
		List<String> list = new ArrayList<String>(0);
		String line;
		try (InputStreamReader read = new InputStreamReader(in, "gbk");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 得到文件的第从start行到end行内容存入sb
	 * 
	 * @param file
	 * @return
	 */
	public static StringBuilder getRowByFile(File file, int start, int end) {
		StringBuilder sb = new StringBuilder();
		String line;
		try (FileInputStream in = new FileInputStream(file);
				InputStreamReader read = new InputStreamReader(in, "gbk");
				BufferedReader reader = new BufferedReader(read);) {
			int index = 0;
			while ((line = reader.readLine()) != null) {
				index++;
				if (start <= index && index <= end)
					sb.append(line).append("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	/**
	 * 得到文件的第从start行到end行内容存入sb
	 * 
	 * @param file
	 * @return
	 */
	public static StringBuilder getRowByFile(File file, int start) {
		StringBuilder sb = new StringBuilder();
		String line;
		try (FileInputStream in = new FileInputStream(file);
				InputStreamReader read = new InputStreamReader(in, "gbk");
				BufferedReader reader = new BufferedReader(read);) {
			int index = 0;
			while ((line = reader.readLine()) != null) {
				index++;
				if (start <= index)
					sb.append(line).append("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

	/**
	 * 获取文件中的文本 数据会在一行
	 * 
	 * @param path
	 * @return
	 */
	public static String getStringByFilePath(String path) {
		String line;
		StringBuffer html = new StringBuffer("");
		try (InputStreamReader read = new InputStreamReader(new FileInputStream(path), "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				html = html.append(line);
			}
			// if (html.indexOf("gb2312") > 0) {
			// StringUtils.errorln("2");
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}

	/**
	 * 在文件中第row行添加content内容。
	 * 
	 * @param path
	 * @param row
	 * @param content
	 * @return
	 */
	public static String getStringByFilePath(String path, int row, String content) {
		String line;
		StringBuffer html = new StringBuffer("");
		int index = 0;
		if (row <= index) {
			html.append(content);
		}
		try (InputStreamReader read = new InputStreamReader(new FileInputStream(path), "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				index++;
				if (row == index) {
					html.append(content);
				}
				html = html.append(line).append("\r\n");
			}
			if (row > index) {
				html.append(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}

	/**
	 * 获取文件中的文本
	 * 
	 * @param path
	 * @return
	 */
	public static String getSrcByFilePath(String path) {
		String line;
		StringBuffer html = new StringBuffer("");
		try (InputStreamReader read = new InputStreamReader(new FileInputStream(path), "gbk");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				html = html.append(line + "\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}

	/**
	 * 获取文件中的文本
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getSrcByFilePath(String path, String charset) throws Exception {
		String line;
		StringBuffer html = new StringBuffer("");
		try (InputStreamReader read = new InputStreamReader(new FileInputStream(path), charset);
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				html = html.append(line + "\r\n");
			}
		} catch (Exception e) {
			throw e;
		}
		return html.toString();
	}

	/**
	 * 获取文件中的文本
	 * 
	 * @param path
	 * @return
	 */
	public static String getSrcByFilePath(File file, String charset) {
		String line;
		StringBuffer html = new StringBuffer("");
		try (InputStreamReader read = new InputStreamReader(new FileInputStream(file), charset);
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				html = html.append(line + "\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}

	/**
	 * 获取文件中的文本
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getStringByFileName(String fileName) {
		String line;
		StringBuffer html = new StringBuffer("");
		try (InputStreamReader read = new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				html = html.append(line + "\r\n");
			}
			if (html.indexOf("gb2312") > 0) {
				StringUtils.errorln("2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}

	/**
	 * 文件名，相对于classes目录下的路径
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getSqlStringByFileName(String fileName) {
		String line;
		StringBuffer html = new StringBuffer("");
		try (InputStreamReader read = new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				html = html.append(line + " ");
			}
			if (html.indexOf("gb2312") > 0) {
				StringUtils.errorln("2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}

	/**
	 * 写入内容
	 * 
	 * @param path
	 * @param content
	 */
	public static void realContent(String path, String content) {
		createFile(path);
		try (FileOutputStream fos = new FileOutputStream(path); Writer out = new OutputStreamWriter(fos, "UTF-8");) {
			out.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在类路径的src源路径中创建文件,并在运行环境中创建jar
	 * 
	 * @param clazz
	 *            类
	 * @param fileName
	 *            文件名
	 * @throws IOException
	 */
	public static String createFile(Class<?> clazz, String fileName) throws IOException {
		String path = clazz.getName();
		System.out.println(clazz.getName() + "   " + fileName);
		path = path.replaceAll("[.]", "\\\\");
		path = "src\\" + path;
		File file = new File(path);
		System.out.println(file.getParentFile().getAbsolutePath());
		path = file.getParentFile().getAbsoluteFile() + "\\" + fileName;
		file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		String str = ExceptionUtils.class.getResource("").getPath() + fileName;
		file = new File(str);
		if (!file.exists()) {
			file.createNewFile();
		}
		return path;
	}

	public static String createFile(Class<?> clazz) throws IOException {
		return createFile(clazz, clazz.getSimpleName());
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void appendContent(String filePath, String content) {
		try (FileWriter writer = new FileWriter(filePath, true);) {
			writer.write(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void appendContent(String filePath, String content, int row) {
		File file = new File(filePath);
		if (!file.exists()) {
			FileUtils.createFile(filePath);
		}
		String fileContent = FileUtils.getStringByFilePath(filePath, row, content);
		FileUtils.writeContent(filePath, fileContent);
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void writeContent(String filePath, String content) {
		try (FileWriter writer = new FileWriter(filePath, false);) {
			writer.write(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void writeContent(String filePath, String content, String charset) {
		try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath), charset);
				BufferedWriter bWriter = new BufferedWriter(osw)) {
			osw.write(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 将文件 所有row集合中的行位置 首尾添加prefix，suffix
	 * 
	 * @param path
	 *            文件路径
	 * @param prefix
	 *            需要添加的前缀
	 * @param suffix
	 *            需要添加的后缀
	 * @param row
	 *            集合-》所有行位置
	 * @return 新的文件内容
	 */
	public static String modifyLineContent(String path, String prefix, String suffix, Set<Integer> row) {
		String line;
		StringBuffer html = new StringBuffer("");
		int index = 0;
		try (InputStreamReader read = new InputStreamReader(new FileInputStream(path), "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				index++;
				if (row.contains(index)) {
					html = html.append(prefix).append(line).append(suffix).append("\r\n");
				} else {
					html = html.append(line).append("\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileUtils.writeContent(path, html.toString());
		return html.toString();

	}

	/**
	 * 将文件 所有从startRow行到endRow中的行位置 首尾添加prefix，suffix
	 * 
	 * @param path
	 *            文件路径
	 * @param prefix
	 *            需要添加的前缀
	 * @param suffix
	 *            需要添加的后缀
	 * @param startRow
	 * @param endRow
	 * @return 新的文件内容
	 */
	public static String modifyLineContent(String path, String prefix, String suffix, int startRow, int endRow) {
		String line;
		StringBuffer html = new StringBuffer("");
		int index = 0;
		try (InputStreamReader read = new InputStreamReader(new FileInputStream(path), "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				index++;
				if (index <= endRow && index >= startRow) {
					line = line.replaceFirst("((\\s)*)(.*)", "$1" + prefix + "$3");
					html = html.append(line).append(suffix).append("\r\n");
				} else {
					html = html.append(line).append("\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileUtils.writeContent(path, html.toString());
		return html.toString();

	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void addContent(String filePath, Map<?, ?> map) {
		StringBuilder sbBuilder = new StringBuilder();
		for (Entry<?, ?> entry : map.entrySet()) {
			sbBuilder.append(entry.getKey() + "=" + entry.getValue() + "\r\n");
		}
		try (FileWriter writer = new FileWriter(filePath, true);) {
			writer.write(sbBuilder.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void addContent(String filePath, String content) {

		try (FileWriter writer = new FileWriter(filePath, true);) {
			writer.write(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void addContentByTrue(String filePath, String content) {
		if (!new File(filePath).exists()) {
			createFile(new File(filePath));
		}
		try (FileWriter writer = new FileWriter(filePath, true);) {
			writer.write(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void writeText(String filePath, String content) {
		try (FileWriter writer = new FileWriter(filePath, false);) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把byte数组写出到文件
	 * 
	 * @param filename
	 * @param data
	 * @throws IOException
	 */
	static public void writeFile(String filename, byte data[]) throws IOException {
		createFile(filename);
		try (FileOutputStream fout = new FileOutputStream(filename);) {
			fout.write(data);
		}
	}

	public static File inputStream2File(String fileName, InputStream is) throws Exception {
		File file = new File(fileName);
		try (OutputStream os = new FileOutputStream(file);) {
			byte buffer[] = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) > 0) {
				os.write(buffer, 0, len);
				os.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("文件没发现！" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("IO失败！" + e.getMessage());
		}
		return file;
	}

	/**
	 * 应用：压缩单个文件 ZipFile("d:/hello.txt", "d:/hello.zip");
	 */
	public static void ZipFile(String filepath, String zippath) {
		try {
			File file = new File(filepath);
			File zipFile = new File(zippath);
			InputStream input = new FileInputStream(file);
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			zipOut.putNextEntry(new ZipEntry(file.getName()));
			int temp = 0;
			while ((temp = input.read()) != -1) {
				zipOut.write(temp);
			}
			input.close();
			zipOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 应用：压缩多个文件（文件夹） ZipMultiFile("f:/uu", "f:/zippath.zip");
	 * 一次性压缩多个文件，文件存放至一个文件夹中
	 */
	public static void ZipMultiFile(String filepath, String zippath) {
		try {
			File file = new File(filepath);// 要被压缩的文件夹
			File zipFile = new File(zippath);
			InputStream input = null;
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; ++i) {
					input = new FileInputStream(files[i]);
					zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + files[i].getName()));
					int temp = 0;
					while ((temp = input.read()) != -1) {
						zipOut.write(temp);
					}
					input.close();
				}
			}
			zipOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 应用： ZipContraFile("d:/hello.zip","d:/eee.txt", "hello.txt"); 解压缩（解压缩单个文件）
	 */
	public static void ZipContraFile(String zippath, String outfilepath, String filename) {
		File file = new File(zippath);// 压缩文件路径和文件名
		File outFile = new File(outfilepath);// 解压后路径和文件名
		try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file);) {
			java.util.zip.ZipEntry entry = zipFile.getEntry(filename);// 所解压的文件名
			try (InputStream input = zipFile.getInputStream(entry);
					OutputStream output = new FileOutputStream(outFile);) {
				int temp = 0;
				while ((temp = input.read()) != -1) {
					output.write(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压缩多个文件 解压缩（压缩文件中包含多个文件）可代替上面的方法使用。 ZipInputStream类
	 * 当我们需要解压缩多个文件的时候，ZipEntry就无法使用了， 如果想操作更加复杂的压缩文件，我们就必须使用ZipInputStream类
	 * ZipContraMultiFile("f:/zippath.zip", "d:/");
	 * ZipContraMultiFile("d:/hello.zip", "d:/");
	 */
	public static void ZipContraMultiFile(String zippath, String outzippath) {
		File file = new File(zippath);
		File outFile = null;
		try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file);
				ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));) {
			ZipEntry entry = null;
			InputStream input = null;
			OutputStream output = null;
			while ((entry = zipInput.getNextEntry()) != null) {
				StringUtils.errorln("解压缩" + entry.getName() + "文件");
				outFile = new File(outzippath + File.separator + entry.getName());
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdir();
				}
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				input = zipFile.getInputStream(entry);
				output = new FileOutputStream(outFile);
				int temp = 0;
				while ((temp = input.read()) != -1) {
					output.write(temp);
				}
				input.close();
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将文件读入至字节数组,文件路径是指项目资源路径
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] file2byte(String filePath) throws IOException {
		try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);) {
			return toByteArray(in);
		}
	}

	/**
	 * 将文件读入至字节数组,文件路径是指服务器文件系统路径
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] file2byte1(String filePath) throws IOException {
		try (InputStream in = new FileInputStream(filePath);) {
			return toByteArray(in);
		}
	}

	// inputStream转outputStream
	public static ByteArrayOutputStream parse(InputStream in) throws Exception {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			swapStream.write(ch);
		}
		return swapStream;
	}

	// outputStream转inputStream
	public static ByteArrayInputStream parse(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream) out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
		return swapStream;
	}

	/**
	 * 字节到文件。
	 * 
	 * @param buf
	 * @param filePath
	 * @param fileName
	 */
	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 字节到文件。
	 * 
	 * @param buf
	 * @param filePath
	 * @param fileName
	 */
	public static void byte2File(byte[] buf, String filePath) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filePath));
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 把文件读入byte数组
	static public byte[] readFile(String filename) throws IOException {
		File file = new File(filename);
		long len = file.length();
		byte data[] = new byte[(int) len];
		try (FileInputStream fin = new FileInputStream(file);) {
			int r = fin.read(data);
			if (r != len)
				throw new IOException("Only read " + r + " of " + len + " for " + file);
		}
		return data;
	}

	/**
	 * 将文件写入流中
	 * 
	 * @param filePath
	 * @return
	 */
	public static InputStream file2inputStream(String filePath) {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		return in;
	}

	/**
	 * 流转字节数组
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream in) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			byte[] buffer = new byte[1024 * 4];
			int n = 0;
			while ((n = in.read(buffer)) != -1) {
				out.write(buffer, 0, n);
			}
			return out.toByteArray();
		}
	}

	/**
	 * 从流中获取
	 * 
	 * @param in
	 * @return
	 */
	public static String getStringByStream(InputStream in) {
		String line;
		StringBuffer html = new StringBuffer("");
		try (InputStreamReader read = new InputStreamReader(in, "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				html = html.append(line + "\r\n");
			}
			if (html.indexOf("gb2312") > 0) {
				StringUtils.errorln("2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}

	/**
	 * 遍历一个目录下的文件，深层次
	 * 
	 * @param configPath
	 *            文件夹目录路径
	 * @param filter
	 *            过滤器
	 * @return
	 */
	public static List<File> traverseDir(String configPath, FilenameFilter filter) {
		File file = new File(configPath);
		List<File> lists = Lists.newArrayList();
		if (file == null || file.isFile()) {
			return lists;
		} else {

			File[] listFiles = file.listFiles();
			if (listFiles != null)
				for (File file2 : listFiles) {
					if (filter.accept(file2, file2.getName())) {
						lists.add(file2);
					}
					if (file2.isDirectory()) {
						lists.addAll(traverseDir(file2.getAbsolutePath(), filter));
					}
				}
			return lists;
		}
	}

	/**
	 * 
	 * @param inputStream
	 * @param outputStream
	 */
	public static void parse(InputStream inputStream, OutputStream outputStream) {
		byte buffer[] = new byte[10240];
		int len = 0;
		try {
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
				outputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取文件的大小 ，非文件夹，如果是文件夹 或者文件不存在 将返回 "";
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileSize(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				return "";
			} else {
				char[] ch = new char[] { 'B', 'K', 'M', 'G', 'T', 'P', 'Z', 'E' };
				double length = file.length();
				int i = 0;
				while (length >= 1024) {
					length = length / 1024;
					i++;
				}
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setMaximumFractionDigits(2);
				// DecimalFormat df = new DecimalFormat("#.00");
				return nf.format(length) + ch[i];
			}
		}
		return "";
	}

	/**
	 * 获取文件的Md5值
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getMD5(String path) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            File f = new File(path);
            FileInputStream fis = new FileInputStream(f);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi.toString(16);
    }
	/**
	 * 获取文件的Md5值
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static String getMD5(File f) throws Exception {
		BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(f);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi.toString(16);
	}

	static String getMD54ByteArray(String path) throws Exception {
		String strMD5 = null;
		MessageDigest digest = MessageDigest.getInstance("md5");
		try (InputStream in = new FileInputStream(path);) {
			byte[] buff = new byte[1024];
			int size = -1;
			while ((size = in.read(buff)) != -1) {
				digest.update(buff, 0, size);
			}
			BigInteger bigInteger = new BigInteger(1, digest.digest());
			strMD5 = bigInteger.toString(16);
		}
		return strMD5;
	}

	/**
	 * 创建固定大小的文件
	 * 
	 * @param file
	 * @param length
	 * @throws IOException
	 */
	public static void createFixLengthFile1(File file, long length) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file); FileChannel output = fos.getChannel();) {
			output.write(ByteBuffer.allocate(1), length - 1);
		}
	}

	/**
	 * 创建固定大小的文件
	 * 
	 * @param file
	 * @param length
	 * @throws IOException
	 */
	public static void createFixLengthFile2(File file, long length) throws IOException {
		try (RandomAccessFile r = new RandomAccessFile(file, "rw");) {
			r.setLength(length);
		}
	}

	/**
	 * 字节写入到文件指定位置。
	 * 
	 * @param file
	 *            文件
	 * @param position
	 *            位置下标
	 * @param b
	 *            字节
	 */
	public static void writeFile(File file, long position, byte[] b) {
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw");) {
			raf.seek(position);
			raf.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			// encode("C:/Workspaces/harry12800.develper.rar",
			// "C:/Workspaces/harry12800.develper.txt");
			// encode("C:/Workspaces/harry12800.tools.rar",
			// "C:/Workspaces/harry12800.tools.txt");
			// encode("C:/Workspaces/dev-docs.zip",
			// "C:/Workspaces/dev-docs.zip.txt");
			decode("C:/Users/harry12800/Desktop/docs.txt", "C:/Users/harry12800/Desktop/docs.rar");
			// decode("C:/Workspaces/harry12800.tools.txt",
			// "C:/Workspaces/b.rar");
			// decode("C:/Workspaces/dev-docs.zip.txt", "C:/Workspaces/c.zip");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件加密
	 * 
	 * @param path
	 * @param outPath
	 * @throws IOException
	 */
	public static void encode(String path, String outPath) throws IOException {
		byte[] bytes = file2byte1(path);
		int i = 0;
		for (byte b : bytes) {
			// printByte(b);
			bytes[i++] = exchange((byte) ~b);
			// printByte((byte) ~b);
			// printByte( exchange((byte) ~b));
			// System.out.println();
		}
		byte2File(bytes, outPath);

	}

	/**
	 * 文件解密
	 * 
	 * @param path
	 * @param outPath
	 * @throws IOException
	 */
	public static void decode(String path, String outPath) throws IOException {
		byte[] bytes = file2byte1(path);
		int i = 0;
		for (byte b : bytes) {
			bytes[i++] = (byte) ~exchange(b);
		}
		byte2File(bytes, outPath);
	}

	/**
	 * Aj45
	 * 
	 * @param i
	 * @return
	 */
	private static byte exchange(byte i) {
		int bit1 = (i >> 7) & 1;
		int bit2 = (i >> 6) & 1;
		int bit3 = (i >> 5) & 1;
		int bit6 = (i >> 2) & 1;
		// System.out.println(bit1);
		// System.out.println(bit2);
		// System.out.println(bit3);
		// System.out.println(bit6);
		if (bit1 == 1)
			i = (byte) (i | 0x20);
		else
			i = (byte) (i & 0xDF);// 11011111
		if (bit2 == 1)
			i = (byte) (i | 0x04);
		else
			i = (byte) (i & 0xFB);// 11111011
		if (bit3 == 1)
			i = (byte) (i | 0x80);
		else
			i = (byte) (i & 0x7F);// 01111111
		if (bit6 == 1)
			i = (byte) (i | 0x40);
		else
			i = (byte) (i & 0xBF);// 10111111
		return i;
	}

	public static void printByte(byte a) {
		int y = a;
		int x = 8;
		while (x-- > 0) {
			y = (byte) (a >> x);
			System.out.print((y & 1));
		}
		System.out.println();
	}
	/**
	 * 将字节数组写入文件
	 */
	public static void writeBytes2File(byte[] bytes, File file) {
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			fos.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				fos = null;
				e.printStackTrace();
			}
		}
	}
	/**
	 * 批量重命名
	 * 重命名格式，xxx %s xxx，其中%s为原有名称
	 * @param formatter
	 */
	public static void batchRename(String dirPath, String formatter) {
		for (File file : getAllFiles(dirPath)) {
			File newFile = new File(file.getParentFile(), String.format(formatter, file.getName()));
			copyFileByChannel(file, newFile);
		}
	}

	/**
	 * 递归获取的文件列表集合
	 */
	private static List<File> allFiles = new ArrayList<>();

	/**
	 * 获取指定目录下全部文件
	 * 
	 * @param dir
	 *            根目录路径
	 * @return 获取到的文件列表
	 */
	public static List<File> getAllFiles(String dir) {
		return getAllFiles(new File(dir));
	}

	/**
	 * 获取指定目录下全部文件
	 * 
	 * @param rootFile
	 *            根目录文件
	 * @return 获取到的文件列表
	 */
	public static List<File> getAllFiles(File rootFile) {
		allFiles = new ArrayList<File>();
		try {
			getFiles(rootFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allFiles;
	}

	/**
	 * 递归dir下全部文件并保存至allFiles
	 * 
	 * @param dir
	 *            发起递归的根目录
	 */
	public static void getFiles(File dir) throws Exception {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File file = fs[i];
			if (fs[i].isDirectory()) {
				try {
					getFiles(fs[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				allFiles.add(file);
			}
		}
	}

	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param tarFile
	 *            复制到的新文件
	 */
	public static void copyFileByChannel(File srcFile, File tarFile) {
		try (FileInputStream fi = new FileInputStream(srcFile);
				FileOutputStream fo = new FileOutputStream(tarFile);
				FileChannel in = fi.getChannel(); // 得到对应的文件通道
				FileChannel out = fo.getChannel()// 得到对应的文件通道
		) {
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 替换指定目录下全部java文件内符合自定义条件的字符串
	 * 
	 * @param rootPath
	 *            根目录的绝对路径
	 * @param replaceString
	 *            key-原文字 value-需要替换的文字
	 */
	public static void replaceStringOfJava(String rootPath, Map<String, String> replaceString) {
		// 获取全部文件
		List<File> files = FileUtils.getAllFiles(rootPath);

		for (File file : files) {
			// 如果不是java后缀的文件,则跳过
			if (!file.getName().endsWith(".java")) {
				continue;
			}

			// 将文件读取为一整个字符串
			String fileContent = readToString(file);

			// 是否有替换操作
			boolean hasReplace = false;
			// 遍历替换map,依次替换全部字符串
			for (Map.Entry<String, String> entry : replaceString.entrySet()) {
				if (fileContent.contains(entry.getKey())) {
					fileContent = fileContent.replace(entry.getKey(), entry.getValue());
					hasReplace = true;
				}
			}

			// 如果有替换操作,则将替换后的新文件内容字符串写入回文件中去
			if (hasReplace) {
				writeString2File(fileContent, file);
			}
		}
	}

	/**
	 * 替换指定目录下全部java文件内符合自定义条件的字符串,支持正则
	 * 
	 * @param rootPath
	 *            根目录的绝对路径
	 * @param replaceString
	 *            key-原文字 value-需要替换的文字
	 */
	public static void replaceAllStringOfJava(String rootPath, Map<String, String> replaceString, String charSet) {
		// 获取全部文件
		List<File> files = FileUtils.getAllFiles(rootPath);

		for (File file : files) {
			// 如果不是java后缀的文件,则跳过
			if (!file.getName().endsWith(".java")) {
				continue;
			}

			// 将文件读取为一整个字符串
			String fileContent = readToString(file, charSet);

			// 是否有替换操作
			boolean hasReplace = false;
			// 遍历替换map,依次替换全部字符串
			for (Map.Entry<String, String> entry : replaceString.entrySet()) {
				if (fileContent.contains(entry.getKey())) {
					fileContent = fileContent.replaceAll(entry.getKey(), entry.getValue());
					hasReplace = true;
				}
			}

			// 如果有替换操作,则将替换后的新文件内容字符串写入回文件中去
			if (hasReplace) {
				writeString2File(fileContent, file, charSet);
			}
		}
	}

	/**
	 * 删除无用java文件
	 * 
	 * @param rootPath
	 *            根目录的绝对路径
	 */
	public static void delNoUseJavaFile(String rootPath) {
		List<File> files = getAllFiles(rootPath);
		out: for (File file : files) {
			if (!file.getName().endsWith(".java")) {
				continue;
			}

			for (File compareFile : files) {
				// 如果包含文件名,则视为有使用
				String fileContent = readToString(compareFile);
				if (fileContent.contains(getName(file))) {
					continue out;
				}
			}

			String absname = file.getAbsoluteFile().getName();
			boolean delete = file.delete();
			System.out.println(absname + " ... delete=" + delete);
		}
	}

	/**
	 * 获取代码行数详情,包括总/空行/注释/有效代码各个行数
	 * 
	 * @param rootPath
	 *            根目录的绝对路径
	 */
	public static void getCodeLinesDetail(String rootPath) {
		// 全部文件中行数
		int allLines = 0;
		// 全部文件中空行数
		int allEmptyLines = 0;
		// 全部文件中代码行数
		int allCodeLines = 0;
		// 全部文件中注释行数
		int allAnnoLines = 0;

		List<File> files = FileUtils.getAllFiles(rootPath);
		for (File file : files) {
			// TODO 只统计java和xml代码
			if (file.getName().endsWith(".java") || file.getName().endsWith(".xml")) {
				FileReader fr;
				try {
					fr = new FileReader(file);
					BufferedReader bufferedreader = new BufferedReader(fr);

					String line;
					// 是否属于多行注释
					boolean multiLineAnno = false;
					while ((line = bufferedreader.readLine()) != null) {
						allLines++;

						// 空行
						if (line.trim().equals("")) {
							allEmptyLines++;
							continue;
						}

						// 单行注释
						if (line.contains("//")) {
							allAnnoLines++;
							continue;
						}

						// 如果还是在多行注释中
						if (multiLineAnno) {
							allAnnoLines++;
							// 如果本行包含多行注释结束符,结束
							if (line.contains("*/")) {
								multiLineAnno = false;
							}
							continue;
						}

						// 多行注释开始(包括/*和/**)
						if (line.contains("/*")) {
							allAnnoLines++;
							multiLineAnno = true;
							continue;
						}

						// 有效代码
						allCodeLines++;
					}
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("文件总行数为：" + allLines);
		System.out.println("文件空行数为：" + allEmptyLines);
		System.out.println("文件注释行数为：" + allAnnoLines);
		System.out.println("文件有效代码行数为：" + allCodeLines);
		System.out.println("--------------------");
		// TODO 计算比例规则为 注释行数/有效代码数
		float percent = (float) allAnnoLines / allCodeLines * 100;
		// 格式化百分比,保留2位小数 %50.00
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("注释比例(注释行数/有效代码数): %" + df.format(percent));
	}

	/**
	 * 获取代码行数,只统计java/xml后缀的文件
	 * 
	 * @param rootPath
	 *            根目录的绝对路径
	 */
	public static void getCodeLines(String rootPath) {
		int allLines = 0;
		List<File> files = getAllFiles(rootPath);
		for (File file : files) {
			if (file.getName().endsWith(".java") || file.getName().endsWith(".xml")) {
				int lines = getLines(file);
				allLines += lines;
			}
		}
		System.out.println(allLines);
	}

	/**
	 * 获取文件内文本的行数
	 */
	public static int getLines(File file) {
		int lines = 0;
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader bufferedreader = new BufferedReader(fr);
			while ((bufferedreader.readLine()) != null) {
				lines++;
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static File getFileByName(String proPath, String filename) {
		File tarFile = null;

		List<File> files = FileUtils.getAllFiles(proPath);

		for (File file : files) {
			String fileName = file.getName();
			if (fileName.equals(filename)) {
				tarFile = file;
				break;
			}
		}

		return tarFile;
	}

	/**
	 * 获取文件名,去除后缀部分
	 */
	public static String getName(File file) {
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf("."));
		// 如果是.9.png结尾的,则在去除.png后缀之后还需要去除.9的后缀
		if (file.getName().endsWith(".9.png")) {
			name = name.substring(0, name.lastIndexOf("."));
		}
		return name;
	}

	/**
	 * 获取文件名,去除后缀部分
	 */
	public static String getName(String fileAbsPath) {
		File file = new File(fileAbsPath);
		return getName(file);
	}

	/**
	 * 文件名和后缀分开
	 */
	public static String[] getNameMap(File file) {
		String[] nameMap = new String[2];

		String name = file.getName();
		name = name.substring(0, name.lastIndexOf("."));
		// 如果是.9.png结尾的,则在去除.png后缀之后还需要去除.9的后缀
		if (file.getName().endsWith(".9.png")) {
			name = name.substring(0, name.lastIndexOf("."));
		}

		nameMap[0] = name;
		nameMap[1] = file.getName().replaceFirst(name, "");

		return nameMap;
	}

	/**
	 * 将文件读取为字符串
	 */
	public static String readToString(File file) {
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// 获取文件的编码格式,再根据编码格式生成字符串
			String charSet = getCharSet(file);
			return new String(filecontent, charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String parseCharset(String oldString, String oldCharset, String newCharset) {
		byte[] bytes;
		try {
			bytes = oldString.getBytes(oldCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("UnsupportedEncodingException - oldCharset is wrong");
		}
		try {
			return new String(bytes, newCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("UnsupportedEncodingException - newCharset is wrong");
		}
	}

	/**
	 * 根据指定编码格式将文件读取为字符串
	 */
	public static String readToString(File file, String charSet) {
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将文件内容以行为单位读取
	 */
	public static ArrayList<String> readToStringLines(File file) {
		ArrayList<String> strs = new ArrayList<String>();

		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				strs.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ignored) {
				}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException ignored) {
				}
			}
		}

		return strs;
	}

	/**
	 * 搜索某目录下所有文件的文本中,是否包含某个字段,如果包含打印改文件路径
	 * 
	 * @param path
	 *            搜索目录
	 * @param key
	 *            包含字段
	 */
	public static void searchFileContent(String path, String key) {
		List<File> allFiles = FileUtils.getAllFiles(path);
		for (File file : allFiles) {
			String string = FileUtils.readToString(file);
			if (string.contains(key)) {
				System.out.println(file.getAbsoluteFile());
			}
		}
	}

	/**
	 * 获取文件编码格式,暂只判断gbk/utf-8
	 */
	public static String getCharSet(File file) {
		String chatSet = null;
		try {
			InputStream in = new java.io.FileInputStream(file);
			byte[] b = new byte[3];
			in.read(b);
			in.close();
			if (b[0] == -17 && b[1] == -69 && b[2] == -65)
				chatSet = "UTF-8";
			else
				chatSet = "GBK";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return chatSet;
	}

	/**
	 * 将字符串写入文件
	 */
	public static void writeString2File(String str, File file, String encoding) {
		BufferedWriter writer = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
			writer.write(str);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				writer = null;
				e.printStackTrace();
			}
		}
	}

	public static void writeString2File(String str, File file) {
		writeString2File(str, file, getCharSet(file));
	}
}
