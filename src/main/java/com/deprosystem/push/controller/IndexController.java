/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.controller;


import com.deprosystem.push.bean.FireBaseConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    //private final VisitsRepository visitsRepository;
    private final FireBaseConfig fireBaseConfig;

    //public IndexController(VisitsRepository visitsRepository) {
    //    this.visitsRepository = visitsRepository;
    //}

    public IndexController(FireBaseConfig fireBaseConfig) {
        this.fireBaseConfig = fireBaseConfig;
    }

    
    @GetMapping("/test")
    public ModelAndView index() throws FirebaseMessagingException {
        Map<String, String> model = new HashMap<>();
        model.put("name", "Alexey++--");
        // This registration token comes from the client FCM SDKs.
String registrationToken = "fAKGIDRybgLMy-bc3WdV2C:APA91bF-ybDZU-ws3br72nVFO-oUDyLefVwfMkCXmwenUJjBR5KkQY4zUzI_5eOiEcqQgVJssf6vfkGePzGtIbEQmAktZRubeVeOQJm8NiFDSxyRd5IhS994wM1fYGedTIyT7JqFHAk3";

// See documentation on defining a message payload.
Message message = Message.builder()
    .putData("score", "850")
    .putData("time", "2:45")
    .setToken(registrationToken)
    .build();

// Send a message to the device corresponding to the provided
// registration token.
String response = FirebaseMessaging.getInstance().send(message);
// Response is a message ID string.
System.out.println("Successfully sent message: " + response);
        
        
        

        //Visit visit = new Visit();
        //visit.description = String.format("Visited at %s", LocalDateTime.now());
        //visitsRepository.save(visit);

        return new ModelAndView("index", model);
    }
}