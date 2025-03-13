package com.itc.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.itc.filter.JwtFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**").allowedOrigins("*")
//				.allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*").allowCredentials(true);
//	}

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/bookmark/*"); // Apply filter to specific URL patterns
		return registrationBean;
	}
}