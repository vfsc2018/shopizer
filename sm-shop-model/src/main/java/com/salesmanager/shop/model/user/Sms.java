package com.salesmanager.shop.model.user;

import java.io.Serializable;

/**
 * Object containing password information
 * for change password request
 * @author carlsamson
 *
 */
public class Sms implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String text;
  private String phone;
  public Sms() {
	  
  }

  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

}
