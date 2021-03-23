package com.salesmanager.shop.model.content;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Content implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @NotEmpty
  private String name;
  private String contentType;

  protected Content() {}

  protected Content(String name) {
    this.name = name;
  }

  protected Content(String name, String contentType) {
    this.name = name;
    this.contentType = contentType;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }


}
