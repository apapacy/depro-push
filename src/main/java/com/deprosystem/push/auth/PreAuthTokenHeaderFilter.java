/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.auth;

import javax.servlet.http.HttpServletRequest;
 
import org.springframework.security.web.authentication
            .preauth.AbstractPreAuthenticatedProcessingFilter;
 
public class PreAuthTokenHeaderFilter 
        extends AbstractPreAuthenticatedProcessingFilter {
 
    private String authHeaderName;
 
    public PreAuthTokenHeaderFilter(String authHeaderName) {
        this.authHeaderName = authHeaderName;
    }
 
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(authHeaderName);
    }
 
    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}