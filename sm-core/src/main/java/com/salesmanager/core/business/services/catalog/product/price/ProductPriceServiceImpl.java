package com.salesmanager.core.business.services.catalog.product.price;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.catalog.product.ProductRepository;
import com.salesmanager.core.business.repositories.catalog.product.price.ProductPriceRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPriceDescription;
import com.salesmanager.core.model.reference.language.Language;

@Service("productPrice")
public class ProductPriceServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductPrice> 
	implements ProductPriceService {

	@Inject
	ProductRepository productRepository;
	
	@Inject
	public ProductPriceServiceImpl(ProductPriceRepository productPriceRepository) {
		super(productPriceRepository);
	}

	
	
	
	
	@Override
	public ProductPrice getProductPriceByid(Long id) {
		return productRepository.getProductPriceByid(id);
	}
	
	
	
	@Override
	public void addDescription(ProductPrice price,
			ProductPriceDescription description) throws ServiceException {
		price.getDescriptions().add(description);
		update(price);
	}
	
	
	@Override
	public void saveOrUpdate(ProductPrice price) throws ServiceException {
		
		if(price.getId()!=null && price.getId().longValue() > 0) {
			this.update(price);
		} else {
			
			Set<ProductPriceDescription> descriptions = price.getDescriptions();
			price.setDescriptions(new HashSet<ProductPriceDescription>());
			this.create(price);
			for(ProductPriceDescription description : descriptions) {
				description.setProductPrice(price);
				this.addDescription(price, description);
			}
			
		}
		
		
		
	}
	
	@Override
	public void delete(ProductPrice price) throws ServiceException {
		
		//override method, this allows the error that we try to remove a detached instance
		price = this.getById(price.getId());
		super.delete(price);
		
	}
	


}
