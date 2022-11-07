package com.knila.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Data Utils consists finding Row and Column count,Reading excel headers,
 * Retrieving testdata id and Fetching testdata values
 */
public class DataUtils {

	/**
	 * findRowColumnCount method to get total no of row and column count in a excel
	 * work sheet
	 *
	 * @param sheet          name
	 * @param rowColumnCount as Hashtable
	 * @return Hashtable (returns row count and column count)
	 */

	public static Hashtable<String, Integer> findRowColumnCount(HSSFSheet sheet,
			Hashtable<String, Integer> rowColumnCount) {

		HSSFRow row = null;
		int rows;
		rows = sheet.getPhysicalNumberOfRows();
		int cols = 0;
		int tmp = 0;
		int counter = 0;
		String temp = null;

		for (int i = 0; i < 10 || i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				temp = convertHSSFCellToString(row.getCell(0));
				if (!temp.isEmpty()) {
					counter++;
				}
				tmp = sheet.getRow(i).getPhysicalNumberOfCells();
				if (tmp > cols) {
					cols = tmp;
				}
			}
		}

		rowColumnCount.put("RowCount", counter);
		rowColumnCount.put("ColumnCount", cols);

		return rowColumnCount;
	}

	/**
	 * readExcelHeaders method read the excel headers column wise sheet
	 *
	 * @param sheet          name
	 * @param excelHeaders   (Hashtable)
	 * @param rowColumnCount (Hashtable)
	 * @return excelHeaders (returns Header column values)
	 */
	public static Hashtable<String, Integer> readExcelHeaders(HSSFSheet sheet, Hashtable<String, Integer> excelHeaders,
			Hashtable<String, Integer> rowColumnCount) {

		HSSFRow row = null;
		HSSFCell cell = null;
		for (int r = 0; r < rowColumnCount.get("RowCount"); r++) {
			row = sheet.getRow(r);

			if (row != null) {
				for (int c = 0; c < rowColumnCount.get("ColumnCount"); c++) {
					cell = row.getCell(c);
					if (cell != null) {
						excelHeaders.put(cell.toString(), c);
					}
				}
				break;
			}
		}
		return excelHeaders;
	}

	/**
	 * convertHSSFCellToString method to convert the HSSFCell value to its
	 * equivalent string value
	 *
	 * @param cell value
	 * @return String cellValue
	 */
	public static String convertHSSFCellToString(HSSFCell cell) {
		String cellValue = null;
		if (cell != null) {
			cellValue = cell.toString();
			cellValue = cellValue.trim();
		} else {
			cellValue = "";
		}
		return cellValue;
	}

	/**
	 * Fetch the test data for a test case based on test case ID
	 *
	 * @param filePath   test data xls location
	 * @param workBook   name
	 * @param sheetName  name
	 * @param testCaseId test id
	 * @return testData data
	 * @throws IOException
	 */
	public static HashMap<String, String> getTestData(String workBook, String sheetName, String testCaseId)
			throws IOException {
		HSSFRow row = null;
		HSSFCell cell = null;

		// Establish connection to work sheet
		String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + "\\testdata\\";
		File file = new File(basePath + workBook + ".xls");
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = wb.getSheet(sheetName);
		Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<String, Integer>();
		excelrRowColumnCount = findRowColumnCount(sheet, excelrRowColumnCount);

		// function call to find excel header fields
		Hashtable<String, Integer> excelHeaders = new Hashtable<String, Integer>();
		excelHeaders = readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);
		HashMap<String, String> data = null;
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<String> matcher = null;
		HashMap<String, String> matcherList = new HashMap<String, String>();
		int idcounter = 1;

		// Get all header
		row = sheet.getRow(0);
		if (row != null) {
			for (int c = 0; c < excelrRowColumnCount.get("ColumnCount"); c++) {
				cell = sheet.getRow(0).getCell(c);
				if (cell != null) {
					String temp = convertHSSFCellToString(row.getCell(c));
					header.add(temp);
				}
			}
		}

		// Get test data set
		for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
			row = sheet.getRow(r);
			if (row != null) {
				HSSFCell tempCell = sheet.getRow(r).getCell(0);
				if (tempCell != null) {
					String tcID = convertHSSFCellToString(row.getCell(0));
					if (tcID.equalsIgnoreCase(testCaseId)) {
						if (idcounter > 1) {
							Log.fail("Please check the '" + workBook + "' file's '" + sheetName
									+ "' sheet, its mapped with more than one id: " + testCaseId);
						}
						data = new HashMap<String, String>();
						matcher = new ArrayList<String>();
						matcher.add(tcID);
						for (int c = 1; c < excelrRowColumnCount.get("ColumnCount"); c++) {
							cell = sheet.getRow(r).getCell(c);
							String temp = convertHSSFCellToString(row.getCell(c));
							matcher.add(temp);
						}
						// Add all the test data to a Map
						for (int i = 0; i < matcher.size(); i++) {
							data.put(header.get(i), matcher.get(i));
						}
						matcherList.putAll(data);
						idcounter++;
					}
				}
			}
		}

		return matcherList;
	}

}
