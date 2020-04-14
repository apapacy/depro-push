/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.controller;


import com.deprosystem.push.bean.FireBaseConfig;
import com.deprosystem.push.model.FirebaseTokenRepository;
import com.deprosystem.push.model.FirebaseToken;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

    class inputFirebaseToken{
        public String id;
        public Long userId;
    }



@RestController
public class IndexController {

    @Autowired
    private final FirebaseTokenRepository firebaseTokenRepository;
    
    private final FireBaseConfig fireBaseConfig;

    //public IndexController(VisitsRepository visitsRepository) {
    //    this.visitsRepository = visitsRepository;
    //}

    public IndexController(FireBaseConfig fireBaseConfig, FirebaseTokenRepository firebaseTokenRepository) {
        this.fireBaseConfig = fireBaseConfig;
        this.firebaseTokenRepository = firebaseTokenRepository;
    }

    
    
    
    @PostMapping("/firebase-token")
    public FirebaseToken postToken(@RequestBody inputFirebaseToken token) {
        FirebaseToken firebaseToken = new FirebaseToken();
        firebaseToken.id = token.id;
        firebaseToken.userId = token.userId;
        this.firebaseTokenRepository.save(firebaseToken);
        return firebaseToken;
    }
    
    @GetMapping("/test")
    public String index() throws FirebaseMessagingException {
        Map<String, String> model = new HashMap<>();
        model.put("name", "Alexey++--");
        // This registration token comes from the client FCM SDKs.
String registrationToken = "fAKGIDRybgLMy-bc3WdV2C:APA91bF-ybDZU-ws3br72nVFO-oUDyLefVwfMkCXmwenUJjBR5KkQY4zUzI_5eOiEcqQgVJssf6vfkGePzGtIbEQmAktZRubeVeOQJm8NiFDSxyRd5IhS994wM1fYGedTIyT7JqFHAk3";

// See documentation on defining a message payload.
Message message = Message.builder()
      .setNotification(new Notification("title", "body"))
      .setAndroidConfig(AndroidConfig.builder()
          .setNotification(AndroidNotification.builder()
              //.setIcon(ANDROID_NEWS_ICON_RESOURCE)
              .build())
          .build())
      .setApnsConfig(ApnsConfig.builder()
          .setAps(Aps.builder()
              //.setBadge(APNS_NEWS_BADGE_RESOURCE)
              .build())
          .build())
      .setWebpushConfig(WebpushConfig.builder()
          //.setNotification(new WebpushNotification(null, null, WEBPUSH_NEWS_ICON_URL))
          .build())
      // .setTopic("auto-news")
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

        return "index";
    }
}