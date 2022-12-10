package kr.isweb.cmmn.config.websocket;

import java.security.Principal;

public class CmmnWebsocketUser implements Principal {

	private String sessionId;

	public CmmnWebsocketUser(String name) {
		this.sessionId = name;
	}

	@Override
	public String getName() {
		return this.sessionId;
	}
}
