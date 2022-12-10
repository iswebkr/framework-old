package kr.isweb.cmmn.config.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_CMMN_MSG")
public class CmmnMessageEntity {

	@Id
	@Column(name="msgId", length=100, nullable=false, unique=true)
	public String msgId;

	@Column(name="msgText", length=200, nullable=false)
	public String msgText;

	@Column(name="msgDesc", length=200, nullable=false)
	public String msgDesc;

	@Column(name="msgLocale", length=10, nullable=false)
	public String msgLocale;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public String getMsgLocale() {
		return msgLocale;
	}

	public void setMsgLocale(String msgLocale) {
		this.msgLocale = msgLocale;
	}
}
