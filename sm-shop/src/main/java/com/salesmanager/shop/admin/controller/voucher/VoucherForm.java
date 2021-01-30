package com.salesmanager.shop.admin.controller.voucher;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Ducdv83@gmail.com
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherForm  implements Serializable{

	private static final long serialVersionUID = -5449520886006625559L;
	private Long id;
	private String code;
	private String description;
	private Integer  point = 0;
	private Integer  discount = 0;
	private Integer percent = 0;
	private Integer  status = 0;
	private Integer  blocked = 0;
	private String blockMessage;
	private String startDate;
	private String endDate;
	private String weekDays = "1,2,3,4,5,6,7";
	private String dayOfMonth;
	private Integer  startTime;
	private Integer  endTime;
	private String approved;
	private Long partnerId;
	private String expire;
	private String manager;
	/*********************************************/
	private String productSku;
	
	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	
	public Integer getPercent() {
		return percent;
	}


	public void setPercent(Integer percent) {
		this.percent = percent;
	}

	public String getManager() {
		return manager ;
	}


	public void setManager(String manager ) {
		this.manager  = manager ;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer  point) {
		this.point = point;
	}
	public Integer  getDiscount() {
		return discount;
	}
	public void setDiscount(Integer  discount) {
		this.discount = discount;
	}
	public Integer  getStatus() {
		return status;
	}
	public void setStatus(Integer  status) {
		this.status = status;
	}
	public Integer  getBlocked() {
		return blocked;
	}
	public void setBlocked(Integer  blocked) {
		this.blocked = blocked;
	}
	public String getBlockMessage() {
		return blockMessage;
	}
	public void setBlockMessage(String blockMessage) {
		this.blockMessage = blockMessage;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getWeekDays() {
		return weekDays;
	}
	public void setWeekDays(String weekDays) {
		this.weekDays = weekDays;
	}
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public Integer  getStartTime() {
		return startTime;
	}
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
	public Integer  getEndTime() {
		return endTime;
	}
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public Long getPartnerId() {
		return partnerId  ;
	}
	public void setPartnerId(Long partnerId) {
		this.partnerId   = partnerId  ;
	}
	public String getExpire() {
		return expire;
	}
	public void setExpire(String expire) {
		this.expire = expire;
	}
}
