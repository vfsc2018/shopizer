package com.salesmanager.shop.model.references;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.shop.model.entity.Entity;

public class ReadableWallet extends Entity {

	private Long id;
	private Integer money;
	private String name;
	private Long customerId;

	private List<Long> friends;

	public void setFriends(List<Long> friends) {
		this.friends = friends;
	}

	@JsonIgnore
	public List<Long> getFriends() {
		return friends;
	}

	public ReadableWallet(Long id, Long customerId, Integer money, String name) {
		this.id = id;
		this.money = money;
		this.name = name;
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReadableWallet() {
	}
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomerId() {
		return this.customerId;
	}
	
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

}
