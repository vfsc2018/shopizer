package com.salesmanager.shop.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.CategoryDescription;
import com.salesmanager.core.model.reference.language.Language;

import org.apache.commons.collections.CollectionUtils;

public class CategoryUtils {

  private CategoryUtils(){}
  
  
  public static com.salesmanager.shop.admin.model.catalog.Category readableCategoryConverter(Category category, Language language) {
    com.salesmanager.shop.admin.model.catalog.Category readableCategory = new com.salesmanager.shop.admin.model.catalog.Category();
    readableCategory.setCategory(category);
    
    List<CategoryDescription> descriptions = new ArrayList<>(category.getDescriptions());
    
    if(CollectionUtils.isNotEmpty(descriptions) && descriptions.size()>1){
      descriptions = descriptions.stream().filter(desc -> desc.getLanguage().getCode().equals(language.getCode())).collect(Collectors.toList());
    }
    
    
    readableCategory.setDescriptions(descriptions);
    return readableCategory;
  }
  
  public static List<com.salesmanager.shop.admin.model.catalog.Category> readableCategoryListConverter(List<Category> categories, Language language) {
    
    return categories.stream().map(cat -> readableCategoryConverter(cat, language)).collect(Collectors.toList());
  }

}
