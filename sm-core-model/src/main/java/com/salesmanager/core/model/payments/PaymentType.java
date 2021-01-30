package com.salesmanager.core.model.payments;

public enum PaymentType {
	
	
	
	CREDITCARD("creditcard"), FREE("free"), COD("cod"), MONEYORDER("moneyorder"), PAYPAL("paypal");
	private String type;
	PaymentType(String type) {
		this.type = type;
	}

	public String getPaymentType() {
		return type;
	}

    public static PaymentType fromString(String text) {
		    if (text != null) {
		      for (PaymentType b : PaymentType.values()) {
		    	String payemntType = text.toUpperCase(); 
		        if (payemntType.equalsIgnoreCase(b.name())) {
		          return b;
		        }
		      }
		    }
		    return null;
	}
}
