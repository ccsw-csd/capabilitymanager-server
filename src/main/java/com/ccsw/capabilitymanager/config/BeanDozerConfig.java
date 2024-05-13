package com.ccsw.capabilitymanager.config;

import org.dozer.DozerBeanMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ccsw
 * 
*/
@Configuration
@EnableCaching
public class BeanDozerConfig {
	
	@Bean
    public DozerBeanMapper getDozerBeanMapper() {

        return new DozerBeanMapper();
    }
	
	

}
