package com.salesmanager.shop.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {

  public final static String STATUS_SUCCESS = "00";

  private String transactionId;
  private String entity;
  private String callbackUrl;

  private String bankCode;
  private String paymentInfo;


  private int totalMoney = 0;
  private int orderType = 10000;

  private Long walletId;
  private Long customerId;

  public boolean isComplexTransactionId(String id){
    return id!=null && id.split("-").length==3;
  }

  public String getComplexTransactionId(){
    return (transactionId==null?"0":transactionId) + "-" + (customerId==null?"0":customerId) + "-" + (walletId==null?"0":walletId);
  }

  public void setComplexTransactionId(String id){
    String[] i = id.split("-");
    this.transactionId = i[0];
    this.customerId = Long.parseLong(i[1]);
    this.walletId = Long.parseLong(i[2]);
  }

  public Long getWalletId() {
    return walletId;
  }
  public void setWalletId(Long walletId) {
    this.walletId = walletId;
  }

  public Long getCustomerId() {
    return customerId;
  }
  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }


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
    return transactionId==null?"0":transactionId;
  }
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

}
