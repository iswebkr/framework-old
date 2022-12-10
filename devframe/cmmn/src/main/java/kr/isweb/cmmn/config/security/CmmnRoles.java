package kr.isweb.cmmn.config.security;

public enum CmmnRoles {
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private String value;

	private CmmnRoles(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
