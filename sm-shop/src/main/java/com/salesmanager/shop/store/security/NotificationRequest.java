package com.salesmanager.shop.store.security;
import javax.validation.constraints.NotEmpty;

public class NotificationRequest {
	
	@NotEmpty(message="{NotEmpty.customer.token}")
	private String token;
	
	@NotEmpty(message="{NotEmpty.customer.token}")
	private String os;
	
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public NotificationRequest() {
        
    }



}
