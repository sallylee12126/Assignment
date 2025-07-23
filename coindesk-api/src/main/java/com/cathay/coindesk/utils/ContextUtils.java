package com.cathay.coindesk.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;


@Component
public class ContextUtils implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ContextUtils.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 取得SPRING BEAN
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz, Object... params) {
		return applicationContext.getBean(clazz, params);
	}
	
	/**
	 * 取得當前的請求
	 * 
	 * @return Optional<HttpServletRequest>
	 */
	public static Optional<HttpServletRequest> getCurrentHttpRequest() {
	    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
	        .filter(requestAttributes -> ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
	        .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
	        .map(ServletRequestAttributes::getRequest);
	}
	
	/**
	 * 取得訊息
	 * @param key message key
	 * @param params message params
	 * @return String message
	 */
	public static String getBundleMessage(String key, Object... params) {
		Locale locale = getLocale();
		return applicationContext.getMessage(key, params, locale);
	}
	
	public static Locale getLocale() {
		return Locale.TRADITIONAL_CHINESE;
	}
	
	public static <T> T autowired(T obj) {
		applicationContext.getAutowireCapableBeanFactory().autowireBean(obj);
		return obj;		
	}
}