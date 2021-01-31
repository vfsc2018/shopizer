package com.salesmanager.core.business.modules.order.total;

import java.math.BigDecimal;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.configuration.DroolsBeanFactory;
import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.price.FinalPrice;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.OrderSummary;
import com.salesmanager.core.model.order.OrderTotal;
import com.salesmanager.core.model.order.OrderTotalType;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.core.modules.order.total.OrderTotalPostProcessorModule;

@Component
public class PromoCodeCalculatorModule implements OrderTotalPostProcessorModule {
	
	
	@Autowired
	private DroolsBeanFactory droolsBeanFactory;
	
	@Autowired
	private PricingService pricingService;

	private String name;
	private String code;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;

	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public OrderTotal caculateProductPiceVariation(OrderSummary summary, ShoppingCartItem shoppingCartItem,
			Product product, Customer customer, MerchantStore store) throws Exception {
		
		Validate.notNull(summary, "OrderTotalSummary must not be null");
		Validate.notNull(store, "MerchantStore must not be null");
		
		if(StringUtils.isBlank(summary.getPromoCode())) {
			return null;
		}
		String sku = null;
		if(summary.getVoucher()!=null && StringUtils.isNotBlank(summary.getVoucher().getProductSku())){
			sku = summary.getVoucher().getProductSku();
		}

		KieSession kieSession=droolsBeanFactory.getKieSession(ResourceFactory.newClassPathResource("com/salesmanager/drools/rules/PromoCoupon.drl"));
		
		OrderTotalResponse resp = new OrderTotalResponse();
		
		OrderTotalInputParameters inputParameters = new OrderTotalInputParameters();
		inputParameters.setPromoCode(summary.getPromoCode());

		if(summary.getVoucher().getPercent()!=null){
			inputParameters.setDiscount(summary.getVoucher().getPercent().intValue()*0.01);
		}
		if(sku!=null && summary.getVoucher().getDiscount()!=null){
			inputParameters.setMoneyoff(summary.getVoucher().getDiscount().intValue()*1.0);
		}
		
        kieSession.insert(inputParameters);
        kieSession.setGlobal("total",resp);
        kieSession.fireAllRules();
		Double discount = resp.getDiscount();
		Double moneyoff = resp.getMoneyoff();

		if((discount!=null && discount.doubleValue()>0) || (moneyoff!=null && moneyoff.doubleValue()>0)){
			OrderTotal orderTotal = new OrderTotal();
			orderTotal.setOrderTotalCode(Constants.OT_DISCOUNT_TITLE);
			orderTotal.setOrderTotalType(OrderTotalType.SUBTOTAL);
			orderTotal.setModule(Constants.OT_PROMOTION_MODULE_CODE);
			
			orderTotal.setTitle(summary.getVoucher().getCode() + ": " + summary.getVoucher().getPoint() + "/" + summary.getVoucher().getDiscount() + "/" + summary.getVoucher().getPercent() +  " #" + summary.getPromoCode());
			orderTotal.setText(product.getProductDescription().getName() + " #" + product.getSku());

			//calculate discount that will be added as a negative value
			FinalPrice productPrice = pricingService.calculateProductPrice(product);
			BigDecimal reduction = productPrice.getFinalPrice();
			if(discount!=null && discount.doubleValue()>0){
				reduction = reduction.multiply(BigDecimal.valueOf(discount));
			}
			if(moneyoff!=null && moneyoff.doubleValue()>0){
				reduction = reduction.add(BigDecimal.valueOf(moneyoff));
			}
			
			reduction = reduction.multiply(BigDecimal.valueOf(shoppingCartItem.getQuantity()));
			orderTotal.setValue(reduction);
			return orderTotal;
		}
		
		return null;
	}

}
