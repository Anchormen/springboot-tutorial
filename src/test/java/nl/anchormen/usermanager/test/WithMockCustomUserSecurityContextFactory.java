package nl.anchormen.usermanager.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser>
{

    @Autowired
    PasswordEncoder PasswordEncoder;
    
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation)
    {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(annotation.username(), annotation.password());
        securityContext.setAuthentication(authentication);
        return securityContext;
    }

}
