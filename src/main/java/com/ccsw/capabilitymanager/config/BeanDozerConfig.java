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
	
	/**
	 * Configures a {@link DozerBeanMapper} bean for mapping between Java objects.
	 *
	 * <p>This configuration creates a {@link DozerBeanMapper} instance, which is used for mapping
	 * properties between different Java objects based on their field names and types.</p>
	 *
	 * @return A {@link DozerBeanMapper} instance used for object-to-object mapping.
	 */
	@Bean
    public DozerBeanMapper getDozerBeanMapper() {

        return new DozerBeanMapper();
    }
	
	

}
