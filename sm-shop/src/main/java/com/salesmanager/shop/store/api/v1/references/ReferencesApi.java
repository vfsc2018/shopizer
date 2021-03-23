package com.salesmanager.shop.store.api.v1.references;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.impl.CacheNamesImpl;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.currency.Currency;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.model.references.MeasureUnit;
import com.salesmanager.shop.model.references.ReadableCountry;
import com.salesmanager.shop.model.references.ReadableZone;
import com.salesmanager.shop.model.references.SizeReferences;
import com.salesmanager.shop.model.references.WeightUnit;
import com.salesmanager.shop.store.controller.country.facade.CountryFacade;
import com.salesmanager.shop.store.controller.currency.facade.CurrencyFacade;
import com.salesmanager.shop.store.controller.language.facade.LanguageFacade;
import com.salesmanager.shop.store.controller.store.facade.StoreFacade;
import com.salesmanager.shop.store.controller.zone.facade.ZoneFacade;
import com.salesmanager.shop.store.security.MerchantValue;
import com.salesmanager.shop.utils.LanguageUtils;

/**
 * Get system Language, Country and Currency objects
 *
 * @author c.samson
 */
@RestController
@RequestMapping("/api/v1")
public class ReferencesApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReferencesApi.class);


  @Inject private StoreFacade storeFacade;

  @Inject private LanguageUtils languageUtils;

  @Inject private LanguageFacade languageFacade;

  @Inject private CountryFacade countryFacade;

  @Inject private ZoneFacade zoneFacade;

  @Inject private CurrencyFacade currencyFacade;

	@Inject
	private MerchantStoreService merchantStoreService;

  /**
   * Search languages by language code private/languages returns everything
   *
   * @return
   */
  @GetMapping("/languages")
  public List<Language> getLanguages() {
    return languageFacade.getLanguages();
  }

  /**
   * Returns a country with zones (provinces, states) supports language set in parameter
   * ?lang=en|fr|ru...
   *
   * @param request
   * @return
   */
  @GetMapping("/country")
  public List<ReadableCountry> getCountry(HttpServletRequest request) {
    MerchantStore merchantStore = storeFacade.getByCode(request);
    Language lang = languageUtils.getRESTLanguage(request);
    return countryFacade.getListCountryZones(lang, merchantStore);
  }

  @GetMapping("/zones")
  public List<ReadableZone> getZones(
      @RequestParam("code") String code, HttpServletRequest request) {
    MerchantStore merchantStore = storeFacade.getByCode(request);
    Language lang = languageUtils.getRESTLanguage(request);
    return zoneFacade.getZones(code, lang, merchantStore);
  }

  /**
   * Currency
   *
   * @return
   */
  @GetMapping("/currency")
  @Cacheable(value=CacheNamesImpl.CACHE_MERCHANT, key = "DEFAULT_STORE_CURRENCY")
  public List<Currency> getCurrency() {
    return currencyFacade.getList();
  }

  @GetMapping("/measures")
  @Cacheable(value=CacheNamesImpl.CACHE_MERCHANT, key = "DEFAULT_STORE_MEASURE")
  public SizeReferences measures() {
    SizeReferences sizeReferences = new SizeReferences();
    sizeReferences.setMeasures(Arrays.asList(MeasureUnit.values()));
    sizeReferences.setWeights(Arrays.asList(WeightUnit.values()));
    return sizeReferences;
  }
  

	@GetMapping("/information")
  @Cacheable(value=CacheNamesImpl.CACHE_MERCHANT, key = "'DEFAULT_STORE_INFORMATION'")
	public MerchantValue information(HttpServletRequest request) throws ServiceException {
		
		MerchantStore merchantStore = merchantStoreService.getByCode(MerchantStore.DEFAULT_STORE);
    
    MerchantValue store = new MerchantValue();

    store.setStorename(merchantStore.getStorename());
    store.setStorebank(merchantStore.getStorebank());
    store.setStoreaddress(merchantStore.getStoreaddress());
    store.setStorephone(merchantStore.getStorephone());
    store.setStorecity(merchantStore.getStorecity());
    store.setStoreEmailAddress(merchantStore.getStoreEmailAddress());
    
		return store;
	}

}
