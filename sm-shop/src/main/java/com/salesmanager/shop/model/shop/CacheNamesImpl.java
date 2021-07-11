package com.salesmanager.shop.model.shop;


public class CacheNamesImpl {

  CacheNamesImpl(){}

  public static final String KEY_LOGIN = "LOGIN_";
  public static final String KEY_RESET = "RESET_";
  public static final String KEY_DEVICE = "DEVICE_";
  public static final String KEY_CUSTOMER_INFOR = "CUSTOMER_INFOR_";
  public static final String KEY_CUSTOMER_REMIND_ORDER = "CUSTOMER_REMIND_ORDER_";

  public final static String CACHE_CUSTOMER_ORDER = "CACHE_CUSTOMER_ORDER";
  public final static String CACHE_PRODUCT = "CACHE_PRODUCT";
  public final static String CACHE_MERCHANT = "CACHE_MERCHANT";
  public final static String CACHE_CONTENT = "CACHE_CONTENT";
  public final static String CACHE_CUSTOMER = "CACHE_CUSTOMER";

  public static String[] caches = {
    CACHE_CUSTOMER_ORDER,
    CACHE_PRODUCT,
    CACHE_MERCHANT,
    CACHE_CONTENT,
    CACHE_CUSTOMER
  };

}

