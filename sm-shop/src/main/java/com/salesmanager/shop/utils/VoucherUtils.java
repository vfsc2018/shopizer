package com.salesmanager.shop.utils;
import org.hashids.Hashids;

public class VoucherUtils {

	final static String alphabet = "78912346QAZWSXEDCRFVTGBYHNUJMKLP";
	final static int min = 12;

	public final static long TYPE_ORDER_PAYMENT = 3;

	private VoucherUtils(){

	}
	
	public static String encode(Long type, Long a, Long b) {
		Hashids hashids = new Hashids("o", min, alphabet);
		return hashids.encode(type, a, b);
	}

	public static long[] decode(String code) {
		Hashids hashids = new Hashids("o", min, alphabet);
		return hashids.decode(code);
	}

	

}
