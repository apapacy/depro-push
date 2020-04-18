/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.model;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TokenUserRepository  extends CrudRepository<TokenUser, String> {
    List<TokenUser> findByToken(String token);
}
