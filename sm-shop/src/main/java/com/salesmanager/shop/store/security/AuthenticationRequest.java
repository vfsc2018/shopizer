package com.salesmanager.shop.store.security;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class AuthenticationRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Username and password must be used when using normal system authentication
	 * for a registered customer
	 */
	@NotEmpty(message="{NotEmpty.customer.userName}")
    private String username;
	@NotEmpty(message="{message.password.required}")
    private String password;

    private Long time = System.currentTimeMillis();
    private Integer counter = 0;
    private String otp;

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setTime(Long time) {
        this.otp = String.valueOf(time%10000);
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public AuthenticationRequest() {
        super();
    }

    public AuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
