/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.controller;


import com.deprosystem.push.bean.FireBaseConfig;
import com.deprosystem.push.firebse.FirebaseManager;
import com.deprosystem.push.model.FirebaseTokenRepository;
import com.deprosystem.push.model.FirebaseToken;
import com.deprosystem.push.model.FirebaseTopic;
import com.deprosystem.push.model.FirebaseTopicRepository;
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
import java.security.Principal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

    class InputFirebaseToken{
        public String token;
        public String[] topics;
    }

    class Output {
        public String result = "OK";
        public String error;
        public Output(){
            super();
        }
        public Output(String error) {
            this.error = error;
        }
    }

    class InputMessage {
        public String topic;
        public Long userId;
        public String title;
        public String body;
        public Map<String, String> data;
    }
    

@RestController
public class IndexController {

    @Autowired
    private final FirebaseTokenRepository firebaseTokenRepository;
    
    @Autowired
    private final FirebaseTopicRepository firebaseTopicRepository;

    private final FireBaseConfig fireBaseConfig;

    //public IndexController(VisitsRepository visitsRepository) {
    //    this.visitsRepository = visitsRepository;
    //}

    public IndexController(
            FireBaseConfig fireBaseConfig,
            FirebaseTokenRepository firebaseTokenRepository,
            FirebaseTopicRepository firebaseTopicRepository) {
        this.fireBaseConfig = fireBaseConfig;
        this.firebaseTokenRepository = firebaseTokenRepository;
        this.firebaseTopicRepository = firebaseTopicRepository;
    }   
    
    @PostMapping("/subscribe")
    public Output subscribe(@RequestBody InputFirebaseToken input, Principal principal) {
        FirebaseToken firebaseToken = new FirebaseToken();
        firebaseToken.id = input.token;
        firebaseToken.userId = Long.parseLong(principal.getName());
        this.firebaseTokenRepository.save(firebaseToken);
        if (input.topics != null) for (int i = 0; i < input.topics.length; i++) {
            FirebaseTopic firebaseTopic = new FirebaseTopic();
            firebaseTopic.id = principal.getName() + ":" + input.topics[i];
            firebaseTopic.userId = Long.parseLong(principal.getName());
            firebaseTopic.topic = input.topics[i];
            firebaseTopic.enabled = true;
            this.firebaseTopicRepository.save(firebaseTopic);
            try {
                FirebaseManager.subscribeToTopic(input.token, input.topics[i]);
            } catch (FirebaseMessagingException ex) {
                Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new Output();
    }

    @PostMapping("/unsubscribe")
    public Output unsubscribe(@RequestBody InputFirebaseToken input, Principal principal) {
        List<FirebaseToken> tokens = this.firebaseTokenRepository.findByUserId(Long.parseLong(principal.getName()));
        for (int i = 0; i < input.topics.length; i++) {
            FirebaseTopic firebaseTopic = new FirebaseTopic();
            firebaseTopic.id = principal.getName() + ":" + input.topics[i];
            firebaseTopic.userId = Long.parseLong(principal.getName());
            firebaseTopic.topic = input.topics[i];
            firebaseTopic.enabled = false;
            this.firebaseTopicRepository.save(firebaseTopic);
            if (!tokens.isEmpty()) {
                List<String> list = tokens.stream().map(item  -> item.id).collect(Collectors.toList());
                try {
                    FirebaseManager.unsubscribeFromTopic(list, input.topics[i]);
                } catch (FirebaseMessagingException ex) {
                    Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return new Output();
    }

    @PostMapping("/send-to-usser")
    public Output sendToTopic(@RequestBody InputMessage input) {
        List<FirebaseToken> tokens = this.firebaseTokenRepository.findByUserId(input.userId);
        for (int i = 0; i < tokens.size(); i++)
            try {
                FirebaseManager.sendMessageToToken(tokens.get(i).id, input.title, input.body, input.data);
            } catch (FirebaseMessagingException ex) {
                Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            }
             return new Output();
    }


        @PostMapping("/send-to-topic")
    public Output sendToUser(@RequestBody InputMessage input) {
        try {
            FirebaseManager.sendMessageToTopic(input.topic, input.title, input.body, input.data);
            return new Output();
        } catch (FirebaseMessagingException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return new Output(ex.getMessage());
        }
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

        return "index++";
    }

    @GetMapping("/test1")
    public String test1() throws FirebaseMessagingException {
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
      .setTopic("a")
    .build();

// Send a message to the device corresponding to the provided
// registration token.
String response = FirebaseMessaging.getInstance().send(message);
// Response is a message ID string.
System.out.println("Successfully sent message: " + response);
        
        
        

        //Visit visit = new Visit();
        //visit.description = String.format("Visited at %s", LocalDateTime.now());
        //visitsRepository.save(visit);

        return "index++";
    }


}