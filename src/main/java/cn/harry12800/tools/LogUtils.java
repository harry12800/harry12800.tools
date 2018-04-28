package cn.harry12800.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class LogUtils {
	static PrintStream printStream = null;
	static PrintStream sysout = System.out;
	static PrintStream syserr = System.err;
	static {
		File file = new File("d:\\cache.log");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void print(Object o) throws Exception {
		try {
			printStream = new PrintStream(new FileOutputStream(new File(
			"d:\\cache.log"), true));
			// set output to file instead of console
			System.setOut(printStream);
			System.setErr(printStream);
			StringUtils.errorln(o);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (printStream != null) {
				printStream.close();
			}
			System.setOut(sysout);
			System.setErr(syserr);
			StringUtils.errorln(o);
		}
	}
}
