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
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
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
	public static void map2Properties(Map<String, String> map, String filePath)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> enty : map.entrySet()) {
			sb.append(enty.getKey() + "=" + enty.getValue() + "\r\n");
		}
		File file = new File(filePath);
		FileUtils.createFile(filePath);
		try (FileOutputStream fos = new FileOutputStream(file);
				Writer out = new OutputStreamWriter(fos, "UTF-8");) {
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
	public static void map2Properties(Map<String, String> map, File file)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> enty : map.entrySet()) {
			sb.append(enty.getKey() + "=" + enty.getValue() + "\r\n");
		}
		try (FileOutputStream fos = new FileOutputStream(file);
				Writer out = new OutputStreamWriter(fos, "UTF-8");) {
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
	public static Map<String, String> properties2Map(InputStream in)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>(0);
		Properties props = new Properties();
		props.load(in);
		for (Entry<Object, Object> entry : props.entrySet()) {
			String key = new String(
					(entry.getKey() + "").getBytes("ISO8859-1"), "utf-8")
							.trim();
			String value = new String(
					(entry.getValue() + "").getBytes("ISO8859-1"), "utf-8")
							.trim();
			// StringUtils.errorln(key+":"+value);
			map.put(key, value);// 配置文件必须设置成utf-8格式，并支持中文
		}
		return map;
	}

	/**
	 * 修改配置
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

	public static Map<String, String> file2Map(String filePath)
			throws IOException {
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

	public static Map<String, String> file2Map(String filePath, String charset)
			throws IOException {
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
	public static Map<String, String> properties2Map(String filePath)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>(0);
		Properties props = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filePath);
		System.out.println(in);
		props.load(in);
		for (Entry<Object, Object> entry : props.entrySet()) {
			String key = new String(
					(entry.getKey() + "").getBytes("ISO8859-1"), "utf-8")
							.trim();
			String value = new String(
					(entry.getValue() + "").getBytes("ISO8859-1"), "utf-8")
							.trim();
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
			//			StringUtils.outln("//目录存在");
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
			try (FileInputStream is = new FileInputStream(file);
					FileOutputStream ps = new FileOutputStream(file1);) {
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
		try (FileInputStream is = new FileInputStream(file);
				FileOutputStream ps = new FileOutputStream(descFile);) {
			is.read(b);
			ps.write(b);
		}
		boolean success = file.renameTo(new File(dir, file.getName()));
		return success;
	}

	/**
	 * 复制文件到目录下。
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
		try (FileInputStream is = new FileInputStream(src);
				FileOutputStream ps = new FileOutputStream(desc);) {
			is.read(b);
			ps.write(b);
		}
		return true;
	}

	/**
	 * 将文件夹移动到另一个位置
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
	 * @param file
	 * @param dirPath
	 */
	public static void moveFile(File file, String dirPath) {
		if (!file.exists()) {
			StringUtils.errorln("文件不存在！没法移走！");
		}
		createDirectory(dirPath);
		String path = new File(dirPath).getAbsolutePath() + "\\"
				+ file.getName();
		StringUtils.errorln(path);
		file.renameTo(new File(path));
	}

	/**
	 * 把文件<code>file</code>移到dirpath文件夹下
	 * @param file
	 * @param dirPath
	 */
	public static boolean moveFile(File file, String dirPath, boolean force) {
		if (!file.exists()) {
			StringUtils.errorln("文件不存在！没法移走！");
		}
		createDirectory(dirPath);
		String path = new File(dirPath).getAbsolutePath() + File.separator
				+ file.getName();
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
	 * @param file
	 * @param dirPath
	 */
	public static void moveFile(String file, String dirPath) {
		moveFile(new File(file), dirPath);
	}

	/**
	 * 把第一个路径下的移到dirpath文件夹下
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
	 * 获取文件中的文本  数据会在一行
	 * @param path
	 * @return
	 */
	public static String getStringByFilePath(String path) {
		String line;
		StringBuffer html = new StringBuffer("");
		try (InputStreamReader read = new InputStreamReader(
				new FileInputStream(path), "utf-8");
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
	public static String getStringByFilePath(String path, int row,
			String content) {
		String line;
		StringBuffer html = new StringBuffer("");
		int index = 0;
		if (row <= index) {
			html.append(content);
		}
		try (InputStreamReader read = new InputStreamReader(
				new FileInputStream(path), "utf-8");
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
		try (InputStreamReader read = new InputStreamReader(
				new FileInputStream(path), "gbk");
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
		try (InputStreamReader read = new InputStreamReader(
				new FileInputStream(path), charset);
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
		try (InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), charset);
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
		try (InputStreamReader read = new InputStreamReader(Thread
				.currentThread().getContextClassLoader()
				.getResourceAsStream(fileName), "utf-8");
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
		try (InputStreamReader read = new InputStreamReader(Thread
				.currentThread().getContextClassLoader()
				.getResourceAsStream(fileName), "utf-8");
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
		try (FileOutputStream fos = new FileOutputStream(path);
				Writer out = new OutputStreamWriter(fos, "UTF-8");) {
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
	public static String createFile(Class<?> clazz, String fileName)
			throws IOException {
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
	public static synchronized void appendContent(String filePath,
			String content) {
		try (FileWriter writer = new FileWriter(filePath, true);) {
			writer.write(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void appendContent(String filePath,
			String content, int row) {
		File file = new File(filePath);
		if (!file.exists()) {
			FileUtils.createFile(filePath);
		}
		String fileContent = FileUtils.getStringByFilePath(filePath, row,
				content);
		FileUtils.writeContent(filePath, fileContent);
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void writeContent(String filePath,
			String content) {
		try (FileWriter writer = new FileWriter(filePath, false);) {
			writer.write(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	 */
	public static synchronized void writeContent(String filePath,
			String content, String charset) {
		try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				filePath), charset); BufferedWriter bWriter = new BufferedWriter(osw)) {
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
	public static String modifyLineContent(String path, String prefix,
			String suffix, Set<Integer> row) {
		String line;
		StringBuffer html = new StringBuffer("");
		int index = 0;
		try (InputStreamReader read = new InputStreamReader(
				new FileInputStream(path), "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				index++;
				if (row.contains(index)) {
					html = html.append(prefix).append(line).append(suffix)
							.append("\r\n");
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
	public static String modifyLineContent(String path, String prefix,
			String suffix, int startRow, int endRow) {
		String line;
		StringBuffer html = new StringBuffer("");
		int index = 0;
		try (InputStreamReader read = new InputStreamReader(
				new FileInputStream(path), "utf-8");
				BufferedReader reader = new BufferedReader(read);) {
			while ((line = reader.readLine()) != null) {
				index++;
				if (index <= endRow && index >= startRow) {
					line = line.replaceFirst("((\\s)*)(.*)", "$1" + prefix
							+ "$3");
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
	 *  把byte数组写出到文件
	 * @param filename
	 * @param data
	 * @throws IOException
	 */
	static public void writeFile(String filename, byte data[])
			throws IOException {
		createFile(filename);
		try (FileOutputStream fout = new FileOutputStream(filename);) {
			fout.write(data);
		}
	}

	public static File inputStream2File(String fileName, InputStream is)
			throws Exception {
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
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
					zipFile));
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
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
					zipFile));
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; ++i) {
					input = new FileInputStream(files[i]);
					zipOut.putNextEntry(new ZipEntry(file.getName()
							+ File.separator + files[i].getName()));
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
	public static void ZipContraFile(String zippath, String outfilepath,
			String filename) {
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
	 * */
	public static void ZipContraMultiFile(String zippath, String outzippath) {
		File file = new File(zippath);
		File outFile = null;
		try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file);
				ZipInputStream zipInput = new ZipInputStream(new FileInputStream(
						file));) {
			ZipEntry entry = null;
			InputStream input = null;
			OutputStream output = null;
			while ((entry = zipInput.getNextEntry()) != null) {
				StringUtils.errorln("解压缩" + entry.getName() + "文件");
				outFile = new File(outzippath + File.separator
						+ entry.getName());
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
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] file2byte(String filePath) throws IOException {
		try (InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filePath);) {
			return toByteArray(in);
		}
	}

	/**
	 * 将文件读入至字节数组,文件路径是指服务器文件系统路径
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] file2byte1(String filePath) throws IOException {
		try (InputStream in = new FileInputStream(filePath);) {
			return toByteArray(in);
		}
	}

	//inputStream转outputStream
	public static ByteArrayOutputStream parse(InputStream in) throws Exception {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			swapStream.write(ch);
		}
		return swapStream;
	}

	//outputStream转inputStream
	public static ByteArrayInputStream parse(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream) out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
		return swapStream;
	}

	/**
	 * 字节到文件。
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

	// 把文件读入byte数组
	static public byte[] readFile(String filename) throws IOException {
		File file = new File(filename);
		long len = file.length();
		byte data[] = new byte[(int) len];
		try (FileInputStream fin = new FileInputStream(file);) {
			int r = fin.read(data);
			if (r != len)
				throw new IOException("Only read " + r + " of " + len + " for "
						+ file);
		}
		return data;
	}

	/**
	 * 将文件写入流中
	 * @param filePath
	 * @return
	 */
	public static InputStream file2inputStream(String filePath) {
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filePath);
		return in;
	}

	/**
	 * 流转字节数组
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream in) throws IOException {
		try (
				ByteArrayOutputStream out = new ByteArrayOutputStream();) {
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
	 * @param configPath 文件夹目录路径
	 * @param filter 过滤器
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
	public static void parse(InputStream inputStream,
			OutputStream outputStream) {
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
				//				DecimalFormat df = new DecimalFormat("#.00");
				return nf.format(length) + ch[i];
			}
		}
		return "";
	}

	public static String getMD5(String path) throws Exception {
		String strMD5 = null;
		File file = new File(path);
		FileInputStream in = new FileInputStream(file);
		MappedByteBuffer buffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		MessageDigest digest = MessageDigest.getInstance("md5");
		digest.update(buffer);
		in.close();

		byte[] byteArr = digest.digest();
		BigInteger bigInteger = new BigInteger(1, byteArr);
		strMD5 = bigInteger.toString(16);
		return strMD5;
	}

	static String getMD54ByteArray(String path) throws Exception {
		String strMD5 = null;
		MessageDigest digest = MessageDigest.getInstance("md5");
		InputStream in = new FileInputStream(path);
		byte[] buff = new byte[1024];
		int size = -1;
		while ((size = in.read(buff)) != -1) {
			digest.update(buff, 0, size);
		}
		in.close();
		BigInteger bigInteger = new BigInteger(1, digest.digest());
		strMD5 = bigInteger.toString(16);
		return strMD5;
	}

	/** 
	 * 创建固定大小的文件 
	 * @param file 
	 * @param length 
	 * @throws IOException  
	 */
	public static void createFixLengthFile1(File file, long length) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file);
				FileChannel output = fos.getChannel();) {
			output.write(ByteBuffer.allocate(1), length - 1);
		}
	}

	/** 
	 * 创建固定大小的文件 
	 * @param file 
	 * @param length 
	 * @throws IOException  
	 */
	public static void createFixLengthFile2(File file, long length) throws IOException {
		try (
				RandomAccessFile r = new RandomAccessFile(file, "rw");) {
			r.setLength(length);
		}
	}

	/**
	 * 字节写入到文件指定位置。
	 * @param file  文件
	 * @param position  位置下标
	 * @param b  字节
	 */
	public static void writeFile(File file, int position, byte[] b) {
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw");) {
			raf.seek(position);
			raf.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		String filePath = "C:\\Users\\harry12800\\Desktop";
		File f = new File(filePath, "aaaa.txt");
		createFixLengthFile2(f, 1L);
		String a = "abcd";
		String b = "efgh";
		String c = "ijkl";
		writeFile(f, 0, a.getBytes());
		writeFile(f, a.getBytes().length, b.getBytes());
		writeFile(f, a.getBytes().length + b.getBytes().length, c.getBytes());
	}
}