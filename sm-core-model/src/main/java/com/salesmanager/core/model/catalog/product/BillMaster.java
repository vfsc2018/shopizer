package com.salesmanager.core.model.catalog.product;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.hibernate.annotations.OrderBy;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.product.relationship.BillItem;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.order.Order;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "BILL_MASTER", schema=SchemaConstant.SALESMANAGER_SCHEMA)
@Cacheable
public class BillMaster extends SalesManagerEntity<Integer, BillMaster>  implements Auditable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BILL_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "BILL_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Integer id;
	
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
	
	
	@Column (length=20)
	private String phone;		
	
	
	@Column (length=500)
	private String address;		

	@Column (name="DESCRIPTION" , length=500)
	private String description;		

	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



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

	@OneToMany(mappedBy = "billMaster")
	@OrderBy(clause = "ID asc")
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
