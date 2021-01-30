package com.salesmanager.shop.admin.controller.vouchercode;

import java.io.Serializable;

/**
 * @author Ducdv83@gmail.com
 *
 */
public class VoucherCodeEditForm  implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -8958658972235612175L;
	private Long id;
	private int blocked;
	private String blockMessage;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	
}
