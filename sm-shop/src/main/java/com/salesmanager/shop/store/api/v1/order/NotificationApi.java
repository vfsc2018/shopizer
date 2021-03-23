package com.salesmanager.shop.store.api.v1.order;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

import com.salesmanager.core.business.modules.cms.impl.CacheNamesImpl;
import com.salesmanager.core.business.repositories.notifications.NotificationsRepositoryImpl;
import com.salesmanager.core.model.message.Notifications;
import com.salesmanager.shop.store.security.ResponseValue;
import com.salesmanager.shop.store.security.user.JWTUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = {"Notification Api"})
@SwaggerDefinition(tags = {
    @Tag(name = "Notification management resource", description = "notification gateway")
})
public class NotificationApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationApi.class);

	@Inject
	private NotificationsRepositoryImpl notificationsRepository;


	@GetMapping("/private/customer/notifications/count")
	@Cacheable(value=CacheNamesImpl.CACHE_CUSTOMER, key = "'noti_count_' + #request.userPrincipal.principal.id + '_' +#read")
	public ResponseValue count(@RequestParam String read, HttpServletRequest request, HttpServletResponse response) {
        final HttpHeaders httpHeaders= new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();
		JWTUser customer =  (JWTUser)principal.getPrincipal();

		Integer count = notificationsRepository.countByCustomerId(customer.getId(), read);
		return new ResponseValue(count);
	}

	@GetMapping("/private/customer/notifications")
	@ResponseBody
	@Cacheable(value=CacheNamesImpl.CACHE_CUSTOMER, key = "'noti_list_' + #request.userPrincipal.principal.id  + '_' + #page + '_' + #read")
	public List<?>  get(@RequestParam String read, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer count,  HttpServletRequest request, HttpServletResponse response) {
        
		UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();
		JWTUser customer =  (JWTUser)principal.getPrincipal();

		return notificationsRepository.findByCustomerId(customer.getId(), read, PageRequest.of(page,count));
	}
	
	@PatchMapping("/private/customer/notification/read/{id}")
	@Cacheable(value=CacheNamesImpl.CACHE_CUSTOMER, key = "'noti_read_' + #id")
	public ResponseValue read(@PathVariable final Long id,  HttpServletRequest request, HttpServletResponse response) {
		Notifications noti = notificationsRepository.findById(id);

		UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();
		JWTUser customer =  (JWTUser)principal.getPrincipal();

		if(noti==null || noti.getCustomer()==null || !noti.getCustomer().getId().equals(customer.getId())){
			return new ResponseValue(0L);
		}

		noti.setRead(1);
		notificationsRepository.save(noti);

		return new ResponseValue(id);
    }
  
}
