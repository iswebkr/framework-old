package kr.isweb.cmmn.util.cmmn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.isweb.cmmn.util.egov.EgovResourceCloseHelper;

public class CmmnExcelUtil {

	private Logger logger = LoggerFactory.getLogger(getClass());

	Workbook workbook;

	public String excelRead(File excelFile) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Map<String, Object> excelMap = new LinkedHashMap<>();

		FileInputStream fileInputStream = null;
		Workbook workbook = null;

		try {
			fileInputStream = new FileInputStream(excelFile);
			workbook = WorkbookFactory.create(fileInputStream);
			fileInputStream.close();

			int sheetsNum = workbook.getNumberOfSheets();
			int sheetcnt = 0;

			for (int sheetIdx = 0; sheetIdx < sheetsNum; sheetIdx++) {
				int rownum = 0;
				String sheetName = "NonameSheet-" + sheetcnt;
				Sheet currentSheet = workbook.getSheetAt(sheetIdx);
				List<Object> sheetList = new ArrayList<>();
				Map<String, Object> sheetMap = new LinkedHashMap<>();

				// When sheet is not null
				if (currentSheet != null) {
					// sheet name setting
					if (StringUtils.isNotBlank(currentSheet.getSheetName())) {
						sheetName = currentSheet.getSheetName();
					}

					// get physical row number
					int pnor = currentSheet.getPhysicalNumberOfRows();

					// Loop for row
					for (int rowIdx = 0; rowIdx <= pnor; rowIdx++) {
						Row currentRow = currentSheet.getRow(rowIdx);
						List<Object> rowList = new ArrayList<>();

						if (currentRow != null) {
							// get physical cell number by row
							int pnoc = currentRow.getPhysicalNumberOfCells();

							// Loop for cell
							for (int cellIdx = 0; cellIdx <= pnoc; cellIdx++) {
								Cell currentCell = currentRow.getCell(cellIdx);

								// When currentCell is not null
								if (currentCell != null) {
									// add cell value to rowList by cell type
									if (CellType.STRING == currentCell.getCellType()) {
										rowList.add(currentCell.getRichStringCellValue().getString());
									} else if (CellType.BOOLEAN == currentCell.getCellType()) {
										rowList.add(currentCell.getBooleanCellValue());
									} else if (CellType.NUMERIC == currentCell.getCellType()) {
										if (DateUtil.isCellDateFormatted(currentCell)) {
											rowList.add(DateFormatUtils.format(currentCell.getDateCellValue(), "yyyy-MM-dd"));
										} else {
											rowList.add(currentCell.getNumericCellValue());
										}
									} else if (CellType.BLANK == currentCell.getCellType()) {
										rowList.add("");
									} else if (CellType.FORMULA == currentCell.getCellType()) {
										rowList.add(currentCell.getCellFormula());
									} else if (CellType.ERROR == currentCell.getCellType()) {
										rowList.add(String.valueOf(currentCell.getErrorCellValue()));
									} else {
										rowList.add("");
									}
								}
							}
							sheetList.add(rowList);
							rownum++;
						}
					}
				}
				sheetMap.put("rowNum", rownum);
				sheetMap.put("rows", sheetList);
				excelMap.put(sheetName, sheetMap);
				sheetcnt++;
			}
		} catch (EncryptedDocumentException | IOException e) {
			if(logger.isErrorEnabled()) {
				logger.error(e.getLocalizedMessage());
			}
		} finally {
			EgovResourceCloseHelper.close(workbook, fileInputStream);
		}

		return gson.toJson(excelMap);
	}
}
