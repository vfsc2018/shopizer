package com.salesmanager.shop.model.catalog.manufacturer;

import java.util.ArrayList;
import java.util.List;

public class PersistableManufacturer extends ManufacturerEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ManufacturerDescription> descriptions = new ArrayList<>();
	public void setDescriptions(List<ManufacturerDescription> descriptions) {
		this.descriptions = descriptions;
	}
	public List<ManufacturerDescription> getDescriptions() {
		return descriptions;
	}

}
