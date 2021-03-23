package com.salesmanager.shop.store.security;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseValue implements Serializable {

    private static final long serialVersionUID = 7156526077883281625L;

	private Integer value;

	private Long id;

	public ResponseValue(){

	}
	public ResponseValue(Long id){
		this.id = id;
	}
	public ResponseValue(Integer value){
		this.value = value;
	}

	public void setValue(Integer value){
		this.value = value;
	}

	public Integer getValue(){
		return value;
	}
	public void setId(Long id){
		this.id = id;
	}

	public Long getId(){
		return id;
	}
}
