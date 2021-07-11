package com.salesmanager.core.model.customer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "CUSTOMER_WALLET", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Wallet extends SalesManagerEntity<Long, Wallet> implements Auditable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WALLET_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "CUSTOMER_WALLET_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	@JsonIgnore
	private AuditSection auditSection = new AuditSection();
	@Column(name = "MONEY")
	private Integer money;

	@Column(name = "TOPUP")
	private Integer topup;
	
	@JsonIgnore
	@Column(name = "STATUS")
	private Integer status;

	@JsonIgnore
	@Column(name = "SHARE")
	private String share;

	@Transient
	private List<Long> friends;

	@Transient
	private String groupShare;

	@Transient
	private String amount;

	public String friendsToShare(){
		if(friends!=null && !friends.isEmpty()){
			String ids = "";
			for(Long f: friends){
				ids += "#" + f + "#";
			}
			return ids;
		}
		return null;
	}

	public List<Long> shareToFriends(){
		List<Long> fs = new ArrayList<>();
		if(share!=null){
			String [] ids = share.split("#");
			for(int i=0;i<ids.length;i++){
				if(ids[i].length()>0) fs.add(Long.parseLong(ids[i]));
			}
		}
		return fs;
	}

	public String getAmount(){
		return this.amount;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}
	
	public void setFriends(List<Long> friends) {
		this.friends = friends;
	}

	public List<Long> getFriends() {
		return friends;
	}

	public String getGroupShare() {
		return groupShare;
	}

	public void setGroupShare(String groupShare) {
		this.groupShare = groupShare;
	}

	// @JsonIgnore
	// @OneToOne
	// @JoinColumn(name="CUSTOMERS_ID")
	// private Customer customer;

	// @Transient
	// private String name;
	// @Transient
	// private Integer point;

	// public Integer getPoint() {
	// 	return point;
	// }

	// public void setPoint(Integer point) {
	// 	this.point = point;
	// }
	// public String getName() {
	// 	return name;
	// }

	// public void setName(String name) {
	// 	this.name = name;
	// }
	public String getShare() {
		this.friends = shareToFriends();
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}

	public Wallet() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTopup() {
		return topup;
	}

	public void setTopup(Integer topup) {
		this.topup = topup;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public void changeMoney(Integer money) {
		this.money += money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}
	
	@Override
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

}
