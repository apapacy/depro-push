/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.model;


import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface FirebaseTokenRepository extends CrudRepository<FirebaseToken, String> {
        List<FirebaseToken> findByUserId(Long userId);
}

