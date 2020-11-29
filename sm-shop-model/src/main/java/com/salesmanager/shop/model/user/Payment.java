package com.salesmanager.shop.model.user;

public class Payment {

  private String transactionId;
  private String entity;
  private String callbackUrl;

  private String bankCode;
  private String paymentInfo;


  private int totalMoney = 0;
  private int orderType = 10000;

  public Payment() {

  }
  public int getOrderType() {
    return orderType;
  }
  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  public int getTotalMoney() {
    return totalMoney;
  }
  public void setTotalMoney(int totalMoney) {
    this.totalMoney = totalMoney;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }
  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public String getPaymentInfo() {
    return paymentInfo;
  }
  public void setPaymentInfo(String paymentInfo) {
    this.paymentInfo = paymentInfo;
  }

  public String getBankCode() {
    return bankCode;
  }
  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  public String getEntity() {
    return entity;
  }
  public void setEntity(String entity) {
    this.entity = entity;
  }
  public String getTransactionId() {
    return transactionId;
  }
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

}
