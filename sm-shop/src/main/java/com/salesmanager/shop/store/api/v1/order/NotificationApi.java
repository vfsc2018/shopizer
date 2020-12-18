package com.salesmanager.shop.store.api.v1.order;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.MediaType;

import com.salesmanager.core.business.repositories.notifications.NotificationsRepository;
import com.salesmanager.core.business.repositories.notifications.NotificationsRepositoryImpl;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.model.message.Notifications;
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


	@GetMapping("/private/notifications/count")
	@ResponseBody
	public ResponseEntity<?>  count(@RequestParam String read, HttpServletRequest request, HttpServletResponse response) {
        final HttpHeaders httpHeaders= new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();
		JWTUser customer =  (JWTUser)principal.getPrincipal();

		Integer count = notificationsRepository.countByCustomerId(customer.getId(), read);
		return new ResponseEntity<>("{\"value\":" + count + "}", httpHeaders, HttpStatus.OK);
	}

	@GetMapping("/private/notifications")
	@ResponseBody
	public List<?>  get(@RequestParam String read, @RequestParam Integer page, @RequestParam Integer count,  HttpServletRequest request, HttpServletResponse response) {
        final HttpHeaders httpHeaders= new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		if (page == null) {
			page = 0;
		}
		if (count == null) {
			count = 20;
		}

		UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();
		JWTUser customer =  (JWTUser)principal.getPrincipal();

		return notificationsRepository.findByCustomerId(customer.getId(), read, PageRequest.of(page,count));
    }
  
}
