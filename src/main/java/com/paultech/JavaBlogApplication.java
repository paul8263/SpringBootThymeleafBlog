package com.paultech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import java.util.Locale;

@SpringBootApplication
public class JavaBlogApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(JavaBlogApplication.class, args);
	}

//	Implemented in application.properties
//	@Bean
//	public ReloadableResourceBundleMessageSource messageSource() {
//		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//		messageSource.setBasename("classpath:i18n/messages");
//		messageSource.setCacheSeconds(3600);
//		return messageSource;
//	}

	@Bean
	public LocaleResolver localeResolver() {
//		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setIgnoreInvalidLocale(true);
		interceptor.setParamName("lang");
		return interceptor;
	}

	@Bean
	public SpringSecurityDialect securityDialect() {
		return new SpringSecurityDialect();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}
