package kr.isweb.cmmn.config.view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import kr.isweb.cmmn.util.egov.EgovResourceCloseHelper;

@Component("fileDownloadView")
public class CmmnFileDownloadView extends AbstractView {

	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		FileInputStream fileInputStream = null;

		try {
			String sourceFilePath = model.get("sourceFilePath").toString();
			String sourceFileName = model.get("sourceFileName").toString();
			String downloadFileName = model.get("downloadFileName").toString();
			String downloadFileContentType = "application/octet-stream";

			Path srcFilePath = Paths.get(sourceFilePath).resolve(sourceFileName);

			if(model.get("downloadFileContentType") != null) {
				downloadFileContentType = model.get("downloadFileContentType").toString();
			} else {
				downloadFileContentType = Files.probeContentType(srcFilePath);
			}

			if(srcFilePath.isAbsolute() && Files.exists(srcFilePath)) {
				downloadFileName = java.net.URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");

				response.setContentType(downloadFileContentType);
				response.setCharacterEncoding(StandardCharsets.UTF_8.name());
				response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\";");
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Expires",  "0");
				response.setContentLengthLong(Files.size(srcFilePath));

				fileInputStream = new FileInputStream(srcFilePath.toFile());
				inputStream = new BufferedInputStream(fileInputStream);
				outputStream = new BufferedOutputStream(response.getOutputStream());

				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();
				fileInputStream.close();
			}
		} catch (IOException e) {
			leaveaTrace.trace("error.file.download", getClass());
		} finally {
			EgovResourceCloseHelper.close(fileInputStream);
			EgovResourceCloseHelper.close(outputStream);
			EgovResourceCloseHelper.close(inputStream);
		}
	}
}
