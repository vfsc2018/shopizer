/**
 *
 */
package com.salesmanager.shop.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

/**
 * @author Umesh Awasthi
 *
 */
public class SessionUtil
{
    private SessionUtil(){

    }

	public static HttpHeaders getBasicHeader(final String token) {
        final HttpHeaders headers = new HttpHeaders(); 
        headers.setContentType(new MediaType("application", "json"));
        headers.add("Authorization", "Basic " + token);
        return headers;
	}
    
    @SuppressWarnings("unchecked")
	public static <T> T getSessionAttribute(final String key, HttpServletRequest request) {
        return (T) request.getSession().getAttribute( key );
    }
    
	public static void removeSessionAttribute(final String key, HttpServletRequest request) {
        request.getSession().removeAttribute( key );
    }

    public static void setSessionAttribute(final String key, final Object value, HttpServletRequest request) {
    	request.getSession().setAttribute( key, value );
    }


}
