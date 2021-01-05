package com.salesmanager.shop.admin.controller.voucher;

import java.io.Serializable;

/**
 * @author Ducdv83@gmail.com
 *
 */
public class VoucherForm  implements Serializable{

	private static final long serialVersionUID = -5449520886006625559L;
	private Long id;
	private String code;
	private String description;
	private int point;
	private int discount;
	private int status;
	private int blocked;
	private String blockMessage;
	private String startDate;
	private String endDate;
	private String weekDays;
	private String dayOfMonth;
	private int startTime;
	private int endTime;
	private String approved;
	private long customerId;
	private String expire;
	private long creatorId;
	/*********************************************/
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
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getBlocked() {
		return blocked;
	}
	public void setBlocked(int blocked) {
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
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getExpire() {
		return expire;
	}
	public void setExpire(String expire) {
		this.expire = expire;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	
	/*********************************/
	
	
}
