package com.salesmanager.core.model.common;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.salesmanager.core.utils.CloneUtils;

@Embeddable
public class Loyalty {
	
	@Column(name = "tierId")
	private Long tierId;
	@Column(name = "expiredtierId")
	private Long expiredtierId;

	@Column(name = "lockPoint")
	private Integer lockPoint;
	@Column(name = "totalPoint")
	private Integer totalPoint;
	@Column(name = "vPoint")
	private Integer vPoint;
	@Column(name = "tierPoint")
	private Integer tierPoint;
	@Column(name = "totalVoucher")
	private Integer totalVoucher;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expireAt")
	private Date expireAt;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expireTime")
	private Date expireTime;

	public Integer getLockPoint() {
		return lockPoint;
	}

	public void setLockPoint(Integer lockPoint) {
		this.lockPoint = lockPoint;
	}

	public Integer getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(Integer totalPoint) {
		this.totalPoint = totalPoint;
	}

	public Integer getVPoint() {
		return vPoint;
	}

	public void setVPoint(Integer vPoint) {
		this.vPoint = vPoint;
	}

	public Integer getTierPoint() {
		return tierPoint;
	}

	public void setTierPoint(Integer tierPoint) {
		this.tierPoint = tierPoint;
	}

	public Integer getTotalVoucher() {
		return totalVoucher;
	}

	public void setTotalVoucher(Integer totalVoucher) {
		this.totalVoucher = totalVoucher;
	}

	public Date getExpireTime() {
		return CloneUtils.clone(expireTime);
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = CloneUtils.clone(expireTime);
	}

	public Date getExpireAt() {
		return CloneUtils.clone(expireAt);
	}

	public void setExpireAt(Date expireAt) {
		this.expireAt = CloneUtils.clone(expireAt);
	}

	public Long getTierId() {
		return tierId;
	}

	public void setTierId(Long tierId) {
		this.tierId = tierId;
	}

	public Long getExpiredtierId() {
		return expiredtierId;
	}

	public void setExpiredtierId(Long expiredtierId) {
		this.expiredtierId = expiredtierId;
	}


	
}
