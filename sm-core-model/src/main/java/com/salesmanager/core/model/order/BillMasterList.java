package com.salesmanager.core.model.order;

import java.util.ArrayList;
import java.util.List;
import com.salesmanager.core.model.common.EntityList;


public class BillMasterList extends EntityList {
	private static final long serialVersionUID = -6729541516948213748L;
	/**
	 * 
	 */

	private List<BillMaster> billMasters = new ArrayList<>();
	
	
	public void setBillMasters(List<BillMaster> billMasters) {
		this.billMasters = billMasters;
	}
	public List<BillMaster> getBillMasters() {
		return billMasters;
	}


}
