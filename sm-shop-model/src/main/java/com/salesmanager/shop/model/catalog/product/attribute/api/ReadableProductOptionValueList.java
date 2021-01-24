package com.salesmanager.shop.model.catalog.product.attribute.api;

import java.util.ArrayList;
import java.util.List;

import com.salesmanager.shop.model.entity.ReadableList;

public class ReadableProductOptionValueList extends ReadableList {

  /**
   * 
   */
  // private static final long serialVersionUID = 1L;
  private List<ReadableProductOptionValueEntity> optionValues = new ArrayList<>();
  public List<ReadableProductOptionValueEntity> getOptionValues() {
    return optionValues;
  }
  public void setOptionValues(List<ReadableProductOptionValueEntity> optionValues) {
    this.optionValues = optionValues;
  }

}
