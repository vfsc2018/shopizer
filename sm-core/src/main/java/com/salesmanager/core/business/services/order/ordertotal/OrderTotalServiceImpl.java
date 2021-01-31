package com.salesmanager.core.business.services.order.ordertotal;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.OrderSummary;
import com.salesmanager.core.model.order.OrderTotal;
import com.salesmanager.core.model.order.OrderTotalType;
import com.salesmanager.core.model.order.OrderTotalVariation;
import com.salesmanager.core.model.order.RebatesOrderTotalVariation;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.core.modules.order.total.OrderTotalPostProcessorModule;

import com.salesmanager.core.business.constants.Constants;

@Service("OrderTotalService")
public class OrderTotalServiceImpl implements OrderTotalService {
	
	@Autowired
	@Resource(name="orderTotalsPostProcessors")
	List<OrderTotalPostProcessorModule> orderTotalPostProcessors;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private LanguageService languageService;

	@Override
	public OrderTotalVariation findOrderTotalVariation(OrderSummary summary, Customer customer, MerchantStore store, Language language)
			throws Exception {
	
		RebatesOrderTotalVariation variation = new RebatesOrderTotalVariation();
		
		String sku = null;
		String code = summary.getPromoCode();

		if(summary.getVoucher()!=null && StringUtils.isNotBlank(summary.getVoucher().getProductSku())){
			sku = summary.getVoucher().getProductSku();
		}
		
		if(code!=null && orderTotalPostProcessors != null) {
			for(OrderTotalPostProcessorModule module : orderTotalPostProcessors) {
				
				List<ShoppingCartItem> items = summary.getProducts();
				for(ShoppingCartItem item : items) {
					
					Long productId = item.getProductId();
					Product product = productService.getProductForLocale(productId, language, languageService.toLocale(language, store));
					if(sku==null || sku.equals(product.getSku())){
						OrderTotal orderTotal = module.caculateProductPiceVariation(summary, item, product, customer, store);
						if(orderTotal==null) {
							continue;
						}
						//if product is null it will be catched when invoking the module
						// orderTotal.setText(product.getProductDescription().getName());
						variation.getVariations().add(orderTotal);	
					}
				}
			}
		}
		if(summary.getVoucher()!=null && sku==null){
			if(summary.getVoucher().getDiscount()!=null){
				OrderTotal orderTotal = new OrderTotal();
				orderTotal.setOrderTotalCode(Constants.OT_DISCOUNT_TITLE);
				orderTotal.setOrderTotalType(OrderTotalType.SUBTOTAL);
				orderTotal.setModule(Constants.OT_PROMOTION_MODULE_CODE);
				orderTotal.setTitle(summary.getVoucher().getCode() + ": " + summary.getVoucher().getPoint() + "/" + summary.getVoucher().getDiscount() + "/" + summary.getVoucher().getPercent() +  " #" + summary.getPromoCode());
				orderTotal.setText(summary.getVoucher().getDescription() + " #" + summary.getVoucher().getCode());
				orderTotal.setValue(new BigDecimal(summary.getVoucher().getDiscount()));
				variation.getVariations().add(orderTotal);	
			}
			if(summary.getVoucher().getPercent()!=null){
				// processing in caculateProductPiceVariation
			}
		}
		
		return variation;
	}

}
