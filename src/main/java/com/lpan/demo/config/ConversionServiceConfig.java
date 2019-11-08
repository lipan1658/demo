package com.lpan.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ConversionServiceConfig extends WebMvcConfigurationSupport{

	@Override
	protected void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToDateConverter());
	}
	

}
