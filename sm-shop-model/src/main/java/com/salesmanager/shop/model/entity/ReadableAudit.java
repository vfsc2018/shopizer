package com.salesmanager.shop.model.entity;

import java.io.Serializable;

public class ReadableAudit implements Serializable {

	private static final long serialVersionUID = 10990099L;

	private String created;
	private String modified;
	private String user;

	public ReadableAudit(){

	}

	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
}
