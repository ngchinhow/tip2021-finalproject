package com.tfip2021.module4.configs;

import static com.tfip2021.module4.models.Constants.OAUTH2_BASE_URI;

import com.tfip2021.module4.security.oauth2.SQLOAuth2AuthorizationRequestRepository;
import com.tfip2021.module4.security.oauth2.SocialOAuth2AuthorizationRequestResolver;
import com.tfip2021.module4.services.CustomUserDetailsService;
import com.tfip2021.module4.security.exceptionHandling.RestAuthenticationEntryPoint;
import com.tfip2021.module4.security.jwt.JWTAuthenticationFilter;
import com.tfip2021.module4.security.oauth2.CustomOAuth2UserService;
import com.tfip2021.module4.security.oauth2.CustomOidcUserService;
import com.tfip2021.module4.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.tfip2021.module4.security.oauth2.OAuth2AuthenticationSuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Autowired
    private CustomOidcUserService oidcUserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler successHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler failureHandler;

    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(this.passwordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .formLogin().disable()
            .cors()
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint())
                .and()
            .authorizeRequests()
                .antMatchers("/", "/index.html", "/*.js", "/*.css", "/*.ico", "/assets/**", "/api/auth/**", "/error")
                .permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
                .authorizationEndpoint()
                    .authorizationRequestRepository(this.mySqlAuthorizationRequestRepository())
                    .authorizationRequestResolver(this.socialOAuth2AuthorizationRequestResolver())
                    .and()
                .userInfoEndpoint()
                    .oidcUserService(oidcUserService)
                    .userService(oauth2UserService)
                    .and()
            .successHandler(successHandler)
            .failureHandler(failureHandler);

        http.addFilterBefore(
            this.jwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class
        );
    }



    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        // JWT filter is not redirecting to the new 
        return new JWTAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public SQLOAuth2AuthorizationRequestRepository mySqlAuthorizationRequestRepository() {
        return new SQLOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SocialOAuth2AuthorizationRequestResolver socialOAuth2AuthorizationRequestResolver() {
        return new SocialOAuth2AuthorizationRequestResolver(
            this.clientRegistrationRepository,
            OAUTH2_BASE_URI
        );
    }
}