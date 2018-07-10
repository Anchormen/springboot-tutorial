package nl.anchormen.usermanager.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithCustomMockUser
{
    String username() default "admin@test.nl";
    String password() default "admin";
    String[] roles() default {"USER_ROLE"};
}
