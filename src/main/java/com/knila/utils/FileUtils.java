package com.knila.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * FileUtils consists copy/move a file from source to destination location
 */
public class FileUtils {

	/**
	 * Copy a file from one location to another
	 *
	 * @param f1 - Source file
	 * @param f2 - Destination File
	 * @throws IOException
	 */
	public static void copyFile(File f1, File f2) throws IOException {
		InputStream in = new FileInputStream(f1);
		OutputStream out = new FileOutputStream(f2);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/***
	 * Method move the file from source location, rename it and place it in the
	 * destination location and deletes the old file from the destination location
	 * if any
	 *
	 * @param oldFile
	 * @param newFile
	 * @throws IOException
	 */
	public static void moveFile(String oldFile, String newFile) throws IOException {
		File oldfile = new File(oldFile);
		File newfile = new File(newFile);
		copyFile(oldfile, newfile);
		oldfile.delete();
	}

	public static void uploadFile(String fileNamewithFormat) throws AWTException {
		String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "testFilesToUpload" + File.separator;

		StringSelection stringselection = new StringSelection(basePath + fileNamewithFormat);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);

		Robot robot = new Robot();
		robot.setAutoDelay(5000);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.setAutoDelay(2000);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		robot.setAutoDelay(2000);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.setAutoDelay(2000);
	}

}
