package kr.isweb.cmmn.config.view;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import egovframework.rte.fdl.cmmn.exception.FdlException;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.idgnr.impl.EgovUUIdGnrServiceImpl;
import kr.isweb.cmmn.util.egov.EgovResourceCloseHelper;

@Component("xssfDownloadView")
public class CmmnXSSFExcelDownloadView extends AbstractView {

	@Autowired
	EgovUUIdGnrServiceImpl egovUUIdGenService;

	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	XSSFWorkbook xssfWorkbook;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		xssfWorkbook = new XSSFWorkbook();
		BufferedOutputStream outputStream = null;

		Sheet sheet = xssfWorkbook.createSheet();
		sheet.setFitToPage(Boolean.TRUE);
		sheet.setAutobreaks(Boolean.TRUE);

		try {
			String downloadFileName = "excel_xssf_" + egovUUIdGenService.getNextStringId() + ".xlsx";

			List<?> dataList = (List<?>) model.get("dataList");
			String jsonStr = gson.toJson(dataList);
			JsonElement jsonElement = JsonParser.parseString(jsonStr);

			if(jsonElement.isJsonArray()) {
				JsonArray jsonArray = jsonElement.getAsJsonArray();
				Iterator<JsonElement> iterator = jsonArray.iterator();
				int countRow = 1;

				while(iterator.hasNext()) {
					JsonElement jsElement = iterator.next();
					Row row = sheet.createRow(countRow);
					int countCell = 0;

					for(Entry<String, JsonElement> item : jsElement.getAsJsonObject().entrySet()) {
						Cell cell = row.createCell(countCell);
						cell.setCellStyle(this.getCellStyle());
						cell.setCellValue(item.getValue().getAsString());
						countCell++;
					}
					countRow++;
				}
			}

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires",  "0");

			outputStream = new BufferedOutputStream(response.getOutputStream());
			xssfWorkbook.write(outputStream);
			outputStream.close();

			xssfWorkbook.close();
		} catch (JsonSyntaxException | FdlException | IOException e) {
			leaveaTrace.trace("error.file.download", getClass());
		} finally {
			EgovResourceCloseHelper.close(outputStream);
			EgovResourceCloseHelper.close(xssfWorkbook);
		}
	}

	private CellStyle getCellStyle() {
		CellStyle cellStyle = xssfWorkbook.createCellStyle();
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setFont(this.getFont());
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setWrapText(Boolean.TRUE);
		return cellStyle;
	}

	private Font getFont() {
		Font font = xssfWorkbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setBold(Boolean.FALSE);
		font.setFontName("NanumGothic");
		return font;
	}
}
