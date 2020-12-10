package com.salesmanager.core.model.catalog.product;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.generic.SalesManagerEntity;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "NOTIFICATIONS", schema=SchemaConstant.SALESMANAGER_SCHEMA)
@Cacheable
public class Notifications extends SalesManagerEntity<Long, Notifications>  implements Auditable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "NOTIFICATIONS_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@JsonIgnore
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID", nullable=true)
	private Customer customer;
	
	@Column (length=300)
	private String message;	
	
	
	@Column (length=300)
	private String topic;		
	
	private int read;	
	
	/***********************************************************************/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	



}
