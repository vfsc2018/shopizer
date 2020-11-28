package com.salesmanager.shop.model.user;

import java.io.Serializable;

/**
 * Object containing password information
 * for change password request
 * @author carlsamson
 *
 */
public class VnpayResponse {

  private String data;
  private int code = 0;

  public VnpayResponse() {

  }
  public int getCode() {
    return code;
  }
  public void setCode(int code) {
    this.code = code;
  }

  public String getData() {
    return data;
  }
  public void setData(String data) {
    this.data = data;
  }

}
