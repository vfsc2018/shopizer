package com.salesmanager.shop.model.catalog.category;

import java.util.ArrayList;
import java.util.List;
import com.salesmanager.shop.model.entity.ReadableList;

public class ReadableCategoryList extends ReadableList {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private List<ReadableCategory> categories = new ArrayList<>();
  public List<ReadableCategory> getCategories() {
    return categories;
  }
  public void setCategories(List<ReadableCategory> categories) {
    this.categories = categories;
    this.setRecordsFiltered(categories==null?0:categories.size());
  }

}
