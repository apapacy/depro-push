/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.bean;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class FireBaseConfig{
    @Bean
    FirebaseApp createFireBaseApp() throws IOException {
        Resource resource = new ClassPathResource("firebase.json");

        InputStream serviceAccount = resource.getInputStream();
        System.out.println("------------------------------------------");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        

      return  FirebaseApp.initializeApp(options);
    }

}