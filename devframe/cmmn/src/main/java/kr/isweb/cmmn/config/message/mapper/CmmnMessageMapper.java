package kr.isweb.cmmn.config.message.mapper;

import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("cmmnMessageMapper")
public interface CmmnMessageMapper {
	public String getMessage(Map<String, Object> map);
}
