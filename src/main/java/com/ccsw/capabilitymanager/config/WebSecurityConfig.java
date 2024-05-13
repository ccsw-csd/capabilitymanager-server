package com.ccsw.capabilitymanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ccsw.capabilitymanager.config.security.JsonWebTokenAuthenticationFilter;
import com.ccsw.capabilitymanager.config.security.JsonWebTokenAuthenticationProvider;

@Configuration
@EnableCaching
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JsonWebTokenAuthenticationProvider jwtAuthProvider;

    @Autowired
    private JsonWebTokenAuthenticationFilter jwtAuthFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String[] unsecuredResources = new String[] { "/", "/security/login", "/public/**" };

        http//
                .csrf().disable()//
                .cors().disable()//
                .httpBasic().disable()//
                .formLogin().disable()//
                .authorizeHttpRequests()//
                .requestMatchers(HttpMethod.OPTIONS).permitAll()//
                .requestMatchers(unsecuredResources).permitAll()//
                .anyRequest().authenticated()//
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                .and().authenticationProvider(jwtAuthProvider)//
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

}
