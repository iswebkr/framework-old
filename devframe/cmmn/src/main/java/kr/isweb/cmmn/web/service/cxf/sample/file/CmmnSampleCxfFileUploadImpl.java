package kr.isweb.cmmn.web.service.cxf.sample.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import kr.isweb.cmmn.util.egov.EgovResourceCloseHelper;
import kr.isweb.cmmn.web.service.cxf.sample.dto.CmmnCxfSampleFileUploader;

@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_MTOM_BINDING)
@WebService(endpointInterface = "kr.isweb.cmmn.web.service.cxf.sample.file.CmmnSampleCxfFileUpload")
public class CmmnSampleCxfFileUploadImpl implements CmmnSampleCxfFileUpload {
	@Override
	public void uploadFile(CmmnCxfSampleFileUploader file) {
		DataHandler handler = file.getFileData();
		InputStream is = null;
		OutputStream os = null;

		try {
			is = handler.getInputStream();
			os = new FileOutputStream(new File("/temp/" + file.getFileName()));
			byte[] b = new byte[1024];
			int byteRead = 0;
			while((byteRead = is.read(b)) != -1) {
				os.write(b, 0, byteRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			EgovResourceCloseHelper.close(os);
			EgovResourceCloseHelper.close(is);
		}
	}
}
