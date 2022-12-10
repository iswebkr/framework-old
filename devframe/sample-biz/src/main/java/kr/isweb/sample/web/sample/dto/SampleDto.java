package kr.isweb.sample.web.sample.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;

import kr.isweb.cmmn.config.validate.annotation.ValidBizNumber;
import kr.isweb.cmmn.config.validate.annotation.ValidCorpNumber;
import kr.isweb.cmmn.config.validate.annotation.ValidFgnrNumber;
import kr.isweb.cmmn.config.validate.annotation.ValidJuminNumber;
import kr.isweb.cmmn.config.validate.annotation.ValidMobilePhone;
import kr.isweb.cmmn.web.dto.CmmnDefaultDto;

public class SampleDto extends CmmnDefaultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ValidMobilePhone
	private String mobilePhone;

	@ValidJuminNumber
	private String juminNo;

	@ValidCorpNumber
	private String corpNo;

	@ValidFgnrNumber
	private String fgnrNo;

	@ValidBizNumber
	private String bizNo;

	@Email
	private String email;

	private String text;

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getJuminNo() {
		return juminNo;
	}

	public void setJuminNo(String juminNo) {
		this.juminNo = juminNo;
	}

	public String getCorpNo() {
		return corpNo;
	}

	public void setCorpNo(String corpNo) {
		this.corpNo = corpNo;
	}

	public String getFgnrNo() {
		return fgnrNo;
	}

	public void setFgnrNo(String fgnrNo) {
		this.fgnrNo = fgnrNo;
	}

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
