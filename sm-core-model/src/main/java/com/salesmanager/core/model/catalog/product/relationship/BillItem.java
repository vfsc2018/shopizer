package com.salesmanager.core.model.catalog.product.relationship;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.product.BillMaster;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "BILL_ITEM", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class BillItem  extends SalesManagerEntity<Long, BillItem>  implements Auditable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	@Id
	@Column(name="ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "BILL_ITEM_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	

    @Embedded
	private AuditSection auditSection = new AuditSection();
	
    @Override
    public AuditSection getAuditSection() {
        return auditSection;
    }
    
    @Override
    public void setAuditSection(AuditSection auditSection) {
        this.auditSection = auditSection;
    }

	@ManyToOne(targetEntity = BillMaster.class)
	@JoinColumn(name="BILL_ID",updatable=false,nullable=true) 
	private BillMaster billMaster = null;
	
	
	@Column(name="code")
	private String code;
	
	@Column(name="NAME")
	private String name;	
	
	@Column(name="QUANTITY")
	private Double quantity;	

	@Column(name="QUANTITY_OF_PARENT")
	private Double quantityOfParent;	
	
	public Double getQuantityOfParent() {
		return quantityOfParent;
	}

	public void setQuantityOfParent(Double quantityOfParent) {
		this.quantityOfParent = quantityOfParent;
	}

	@Column(name="PRICE")
	private BigDecimal price;	
	

	@Column(name="UNIT")
	private String unit;	
	
	@Column(name="PARENT_ID")
	private Long parentId;	
	
	

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}




	public BillMaster getBillMaster() {
		return billMaster;
	}


	public void setBillMaster(BillMaster billMaster) {
		this.billMaster = billMaster;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Double getQuantity() {
		return quantity;
	}


	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}



	public BigDecimal getPrice() {
		return price;
	}

	
	

	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}

}
