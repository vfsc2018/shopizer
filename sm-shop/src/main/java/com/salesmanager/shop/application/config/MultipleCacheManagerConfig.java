package com.salesmanager.shop.application.config;

import com.salesmanager.core.business.modules.cms.impl.CacheNamesImpl;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class MultipleCacheManagerConfig extends CachingConfigurerSupport {
	

	// @Bean
	// public net.sf.ehcache.CacheManager ehCacheManager() {
	// 	CacheConfiguration productGroupCache = new CacheConfiguration();
	// 	productGroupCache.setName(ProductGroupCacheManagerImpl.NAMED_CACHE);
	// 	productGroupCache.setMemoryStoreEvictionPolicy("LRU");
	// 	productGroupCache.setMaxEntriesLocalHeap(1000);
	// 	productGroupCache.setTimeToLiveSeconds(3600);

	// 	CacheConfiguration productDetailCache = new CacheConfiguration();
	// 	productDetailCache.setName(CacheNamesImpl.NAMED_CACHE);
	// 	productDetailCache.setMemoryStoreEvictionPolicy("LRU");
	// 	productDetailCache.setMaxEntriesLocalHeap(100);
	// 	productDetailCache.setTimeToLiveSeconds(3600);

	// 	CacheConfiguration customerOrderCache = new CacheConfiguration();
	// 	customerOrderCache.setName(CacheNamesImpl.NAMED_CACHE);
	// 	customerOrderCache.setMemoryStoreEvictionPolicy("LRU");
	// 	customerOrderCache.setMaxEntriesLocalHeap(1000);
	// 	customerOrderCache.setTimeToLiveSeconds(3600);

	// 	net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
	// 	return net.sf.ehcache.CacheManager.getInstance();
	// 	config.addCache(customerOrderCache);
	// 	config.addCache(productDetailCache);
	// 	config.addCache(productGroupCache);
	// 	return net.sf.ehcache.CacheManager.newInstance(config);
	// }
	
	@Bean
	@Override
	public CacheManager cacheManager() {
		net.sf.ehcache.CacheManager caches = net.sf.ehcache.CacheManager.getInstance();
		for(int i=0;i<CacheNamesImpl.caches.length;i++){
			caches.addCache(CacheNamesImpl.caches[i]);
		}
		return new EhCacheCacheManager(caches);//net.sf.ehcache.CacheManager.getCacheManager("com.shopizer.OBJECT_CACHE"));
		// return new ConcurrentMapCacheManager(ProductGroupCacheManagerImpl.NAMED_CACHE);


		// CacheManager cache = net.sf.ehcache.CacheManager.getInstance();
		// return null; //net.sf.ehcache.CacheManager.getInstance();
		// return cache.getCacheManager();
		// return new EhCacheCacheManager(ehCacheManager());
	}

    @Bean
    public CacheManager alternateCacheManager() {
		// return new EhCacheCacheManager(net.sf.ehcache.CacheManager.getCacheManager("com.shopizer.OBJECT_CACHE"));
        return new ConcurrentMapCacheManager();//CacheNamesImpl.NAMED_CACHE,CacheNamesImpl.NAMED_CACHE);
    }
}
