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

    /**
     * Configures the {@link AuthenticationManager} bean used for authentication.
     *
     * @param config The {@link AuthenticationConfiguration} used to obtain the {@link AuthenticationManager}.
     * @return An instance of {@link AuthenticationManager} configured according to the provided {@link AuthenticationConfiguration}.
     * @throws Exception If an error occurs while creating the {@link AuthenticationManager}.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
    
    /**
     * Configures the {@link SecurityFilterChain} for securing HTTP requests.
     *
     * <p>This configuration disables CSRF protection, CORS, HTTP basic authentication, and form login.
     * It permits all requests to specified unsecured resources and OPTIONS requests.
     * All other requests require authentication. Sessions are managed statelessly,
     * and a custom authentication provider and filter are added to handle JWT-based authentication.</p>
     *
     * @param http The {@link HttpSecurity} object used to configure the security settings.
     * @return The configured {@link SecurityFilterChain} instance.
     * @throws Exception If an error occurs while configuring the security settings.
     */
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

    /**
     * Configures CORS (Cross-Origin Resource Sharing) for the application.
     *
     * <p>This configuration allows cross-origin requests from any origin and permits the HTTP methods 
     * GET, POST, PUT, and DELETE for all endpoints.</p>
     *
     * @return A {@link WebMvcConfigurer} instance that provides CORS configuration.
     */
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
