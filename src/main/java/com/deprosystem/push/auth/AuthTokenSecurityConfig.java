/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.auth;

import com.deprosystem.push.model.TokenUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
 
@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
@Order(1)
public class AuthTokenSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Value("${howtodoinjava.http.auth.tokenName}")
    private String authHeaderName;
 
    //TODO: retrieve this token value from data source
    @Value("${howtodoinjava.http.auth.tokenValue}")
    private String authHeaderValue;
    
    @Autowired
    private final TokenUserRepository tokenUserRepository;

    public AuthTokenSecurityConfig(TokenUserRepository tokenUserRepository){
        this.tokenUserRepository = tokenUserRepository;
    }
 
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception 
    {
        PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter(this.tokenUserRepository, authHeaderName);
         
        filter.setAuthenticationManager(new AuthenticationManager() 
        {
            @Override
            public Authentication authenticate(Authentication authentication) 
                                                throws AuthenticationException 
            {
                String principal = (String) authentication.getPrincipal();
                 
                if (principal == null)
                {
                    throw new BadCredentialsException("The API key was not found "
                                                + "or not the expected value.");
                }
                authentication.setAuthenticated(true);
                return authentication;
            }
        });
         
        httpSecurity.
            antMatcher("/**")
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilter(filter)
                .addFilterBefore(new ExceptionTranslationFilter(
                    new Http403ForbiddenEntryPoint()), 
                        filter.getClass()
                )
                .authorizeRequests()
                    .anyRequest()
                    .authenticated();
    }
 
}