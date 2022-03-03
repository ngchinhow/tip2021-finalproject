package com.tfip2021.module4.configs;


import static com.tfip2021.module4.models.Constants.AUTHORIZATION_REQUEST_BASE_URI;

import com.tfip2021.module4.security.oauth2.SQLOAuth2AuthorizationRequestRepository;
import com.tfip2021.module4.security.oauth2.SocialOAuth2AuthorizationRequestResolver;
import com.tfip2021.module4.security.oauth2.jwt.JWTAuthenticationFilter;
import com.tfip2021.module4.security.oauth2.CustomOAuth2UserService;
import com.tfip2021.module4.security.oauth2.CustomOidcUserService;
import com.tfip2021.module4.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.tfip2021.module4.security.oauth2.OAuth2AuthenticationSuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
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
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/", "/login/**", "/error", "/oauth2/**").permitAll()
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

    // @Bean(BeanIds.AUTHENTICATION_MANAGER)
    // @Override
    // public AuthenticationManager authenticationManagerBean() throws Exception {
    //     return super.authenticationManagerBean();
    // }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    public SQLOAuth2AuthorizationRequestRepository mySqlAuthorizationRequestRepository() {
        return new SQLOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SocialOAuth2AuthorizationRequestResolver socialOAuth2AuthorizationRequestResolver() {
        return new SocialOAuth2AuthorizationRequestResolver(
            this.clientRegistrationRepository, AUTHORIZATION_REQUEST_BASE_URI
        );
    }
}
