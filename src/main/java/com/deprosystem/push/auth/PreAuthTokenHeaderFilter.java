/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.auth;

import com.deprosystem.push.model.TokenUser;
import com.deprosystem.push.model.TokenUserRepository;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.security.web.authentication
            .preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

public class PreAuthTokenHeaderFilter 
        extends AbstractPreAuthenticatedProcessingFilter {
 
    
    //@Autowired
    private final TokenUserRepository tokenUserRepository;
  
    private String authHeaderName;
 
    public PreAuthTokenHeaderFilter(TokenUserRepository tokenUserRepository, String authHeaderName) {
        this.authHeaderName = authHeaderName;
        this.tokenUserRepository = tokenUserRepository;
    }
 
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String token = request.getHeader(authHeaderName);
        List<TokenUser> users = this.tokenUserRepository.findByToken(token);
        return users.isEmpty() ? null : users.get(0).userId;
    }
 
    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}