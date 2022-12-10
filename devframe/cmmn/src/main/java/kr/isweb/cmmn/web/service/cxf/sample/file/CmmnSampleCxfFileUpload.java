package kr.isweb.cmmn.web.service.cxf.sample.file;

import javax.jws.WebParam;
import javax.jws.WebService;

import kr.isweb.cmmn.web.service.cxf.sample.dto.CmmnCxfSampleFileUploader;

@WebService
public interface CmmnSampleCxfFileUpload {
	public void uploadFile(@WebParam(name="file") CmmnCxfSampleFileUploader file);
}
