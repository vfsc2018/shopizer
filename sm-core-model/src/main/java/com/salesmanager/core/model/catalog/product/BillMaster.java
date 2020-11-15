package com.salesmanager.core.model.catalog.product;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.product.relationship.BillItem;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.order.Order;


@Entity
@Table(name = "BILL_MASTER", schema=SchemaConstant.SALESMANAGER_SCHEMA)
@Cacheable
public class BillMaster extends SalesManagerEntity<Integer, BillMaster> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BILL_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "BILL_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Integer id;

	@NotEmpty	@Pattern(regexp="^[a-zA-Z0-9_]*$")	@Column(name = "SKU") String sku;
	
	
	@Column (name="PRODUCT_NAME" , length=64 , nullable=false)
	private String productName;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateExported;
	
	
	
	public Date getDateExported() {
		return dateExported;
	}



	public void setDateExported(Date dateExported) {
		this.dateExported = dateExported;
	}

	@Column (name="STATUS" , length=100)
	private String status;	

	@Column (name="DESCRIPTION" , length=500)
	private String description;		

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "billMaster")
	private Set<BillItem> items = new HashSet<BillItem>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt = new Date();

	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID", nullable=false)
	private Order order;


	public Order getOrder() {
		return order;
	}



	public void setOrder(Order order) {
		this.order = order;
	}



	public BillMaster() {
	}



	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Set<BillItem> getItems() {
		return items;
	}

	public void setItems(Set<BillItem> items) {
		this.items = items;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}


}
