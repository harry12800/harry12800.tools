package cn.harry12800.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

public class JarTool {

	private static final Integer BUFFER_SIZE = 512;

	/**
	 * Logger for this class
	 * 
	 * @throws IOException
	 */
	// private static final Logger logger = Logger.getLogger(JarTool.class);
	public static void main(String[] args) throws IOException {
		StringUtils.errorln("start run");
		JarTool.compressFolder("", ".jar");
	}

	/**
	 * 压缩文件夹及其子文件夹
	 * 
	 * @param source
	 *            String 源文件夹,如: d:/tmp
	 * @param dest
	 *            String 目标文件,如: e:/tmp.jar
	 * @throws IOException
	 */
	public static void compressFolder(String source, String dest)
			throws IOException {
		JarOutputStream jos = new JarOutputStream(new FileOutputStream(dest));
		jos.setLevel(Deflater.BEST_COMPRESSION);
		compressJarFolder(jos, new File(source), "");
		jos.close();
	}

	private static void compressJarFolder(JarOutputStream jos, File f,
			String base) throws IOException {
		System.out
				.println("compressJarFolder:" + f.getName() + " base:" + base);
		if (f.isFile()) {
			compressJarFile(jos, f, base);
		} else if (f.isDirectory()) {
			compressDirEntry(jos, f, base);
			String[] fileList = f.list();
			for (String file : fileList) {
				String newSource = f.getAbsolutePath() + File.separator + file;
				File newFile = new File(newSource);
				String newBase = base + "/" + f.getName() + "/"
						+ newFile.getName();
				if (base.equals("")) {
					newBase = newFile.getName();// f.getName()+"/"+newFile.getName();
				} else {
					newBase = base + "/" + newFile.getName();
				}

				// logger.info("正在压缩文件从 "+newSource+"    到 "+newBase);
				compressJarFolder(jos, newFile, newBase);

			} // for

		} // if
	}

	// 压缩单个文件
	private static void compressJarFile(JarOutputStream jos, File f, String base)
			throws IOException {
		StringUtils.errorln("compressJarFile:" + f.getName() + " base:" + base);

		jos.putNextEntry(new ZipEntry(base));

		BufferedInputStream bin = new BufferedInputStream(
				new FileInputStream(f));

		byte[] data = new byte[JarTool.BUFFER_SIZE];
		int iRead = 0;
		while ((iRead = bin.read(data)) != -1) {
			jos.write(data, 0, iRead);
		}
		bin.close();
		jos.closeEntry();
	}

	// 压缩单个文件到JAR文件中
	public static void compressFile(String sourceFile, String jarFile)
			throws IOException {
		File f = new File(sourceFile);
		String base = f.getName();
		JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
		jos.putNextEntry(new ZipEntry(base));

		BufferedInputStream bin = new BufferedInputStream(
				new FileInputStream(f));

		byte[] data = new byte[JarTool.BUFFER_SIZE];
		while ((bin.read(data)) != -1) {
			jos.write(data);
		}
		bin.close();
		jos.closeEntry();
		jos.close();
	}

	// 压缩空文件夹
	private static void compressDirEntry(JarOutputStream jos, File f,
			String base) throws IOException {
		StringUtils.errorln("1:" + base + "/");
		jos.putNextEntry(new ZipEntry(base + "/"));

		jos.closeEntry();
	}
}