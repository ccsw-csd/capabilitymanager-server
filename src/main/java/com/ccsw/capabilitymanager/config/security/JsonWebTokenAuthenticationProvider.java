package com.ccsw.capabilitymanager.config.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Can process a specific {@link Authentication} implementation.
 *
 */
@Component
public class JsonWebTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JsonWebTokenUtility jwtUtility;

    @Value("${app.code}")
    private String appCode;

    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Authentication authenticatedUser = null;

        String tokenHeader = (String) authentication.getPrincipal();

        tokenHeader = tokenHeader.substring(tokenHeader.indexOf(' ') + 1);

        UserInfoDto details = this.jwtUtility.createUserDetails(tokenHeader);
        if (details != null) {
            authenticatedUser = new JsonWebTokenAuthentication(details, createAuthorities(details), tokenHeader);
            authenticatedUser.setAuthenticated(true);
        }

        return authenticatedUser;
    }

    /**
     * @param details
     * @return
     */
    private List<GrantedAuthority> createAuthorities(UserInfoDto details) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : details.getAppRoles(appCode)) {
            authorities.add(new GrantedAuthority() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getAuthority() {

                    return role;
                }
            });
        }

        return authorities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class) || authentication.isAssignableFrom(JsonWebTokenAuthentication.class);
    }

}
