package com.ccsw.capabilitymanager.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A pre-authenticated filter which purpose is only to extract the necessary
 * information on the principal from the incoming request, rather than to
 * authenticate them.
 *
 */
@Component
public class JsonWebTokenAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    public static final String ACCESS_HEADER_NAME = "Authorization";

    /**
     * SecurityContext
     */
    public static final ThreadLocal<SecurityContext> SecurityContext = new ThreadLocal<>();

    /**
     * The constructor.
     */
    public JsonWebTokenAuthenticationFilter() {

        // Don't throw exceptions if the header is missing
        setExceptionIfHeaderMissing(false);

        // Set the name of the request header that contains the username
        setPrincipalRequestHeader(ACCESS_HEADER_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        JsonWebTokenAuthenticationFilter.SecurityContext.set(SecurityContextHolder.getContext());

        super.doFilter(request, response, chain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {

        super.setAuthenticationManager(authenticationManager);
    }

}
