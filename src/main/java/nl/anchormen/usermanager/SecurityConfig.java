package nl.anchormen.usermanager;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import nl.anchormen.usermanager.user.User;
import nl.anchormen.usermanager.user.UserService;
import nl.anchormen.usermanager.web.ErrorHandler;

// Enables method level authorization on the service layer. Will be discussed in the service layer section.
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable()
            // A REST API should not have any state.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // Authorize all requests
            .authorizeRequests()
            // except these paths, they contain public static content.
            .antMatchers("/", "/*.html", "/*.js", "/*.css",
                // Urls for static content for the h2 database.
                "/h2/**", "/console/**",
                // Urls for static content for swagger.
                "/v2/api-docs", "/v2/swagger-ui.html", "/swagger-resources/**", "/webjars/springfox-swagger-ui/**")
            .permitAll()
            // All other requests should be authenticated
            .anyRequest().authenticated()
            // using basic HTTP authentication.
            .and().httpBasic()
            // allow requests from frames of the same origin, required for the h2 tooling
            .and().headers().frameOptions().sameOrigin();
        
        // Write security error responses, the ErrorHandler will be explained in a later section
        http.exceptionHandling().authenticationEntryPoint(
            (request, response, exception) -> ErrorHandler.writeError(response, "Authentication Error", HttpStatus.UNAUTHORIZED))
            .accessDeniedHandler(
                (request, response, exception) -> ErrorHandler.writeError(response, "Forbidden", HttpStatus.FORBIDDEN));
    }

    /**
     * Register bcrypt as the default password encoder using the @Bean annotation 
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(6);
    }
    
    /**
     * Spring can use a UserDetailsService for authenticating users, we register one using the @Bean annotation.
     * 
     * This Spring Bean requires a UserService, spring will automatically search for it in the other beans.
     *  
     * @param userService
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(UserService userService) 
    {
        return email ->  {
            User user = userService.findByEmail(email);
            if (user == null)
            {
                throw new UsernameNotFoundException(email);
            }
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getUserRole().getName()));
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncodedPassword(),
                authorities);
        };
    }

    /**
     * Configure Spring Security to use our UserDetailsService and PasswordEncoder, the required objects will be auto-wired into this method.
     * 
     * @param auth
     * @param userDetailsService
     * @param passwordEncoder
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception
    {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

}