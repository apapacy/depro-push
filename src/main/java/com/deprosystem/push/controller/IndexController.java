/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.controller;

import org.springframework.core.task.TaskExecutor;
import com.deprosystem.push.bean.FireBaseConfig;
import com.deprosystem.push.firebase.FirebaseManager;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

    class InputFirebaseToken{
        public String push_token;
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
    
    private final TaskExecutor taskExecutor;

    //public IndexController(VisitsRepository visitsRepository) {
    //    this.visitsRepository = visitsRepository;
    //}

    public IndexController(
            FireBaseConfig fireBaseConfig,
            FirebaseTokenRepository firebaseTokenRepository,
            FirebaseTopicRepository firebaseTopicRepository,
            TaskExecutor taskExecutor) {
        this.fireBaseConfig = fireBaseConfig;
        this.firebaseTokenRepository = firebaseTokenRepository;
        this.firebaseTopicRepository = firebaseTopicRepository;
        this.taskExecutor = taskExecutor;
    }   
    
    @PostMapping("/subscribe")
    public Output subscribe(@RequestBody InputFirebaseToken input, Principal principal) {
        FirebaseToken firebaseToken = new FirebaseToken();
        firebaseToken.id = input.push_token;
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
                FirebaseManager.subscribeToTopic(input.push_token, input.topics[i]);
            } catch (FirebaseMessagingException ex) {
                Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new Output();
    }

    @PostMapping("/subscribe_news")
    public Output subscribeNews(@RequestBody InputFirebaseToken input, Principal principal) {
        String topic = "news";
        FirebaseToken firebaseToken = new FirebaseToken();
        firebaseToken.id = input.push_token;
        firebaseToken.userId = Long.parseLong(principal.getName());
        this.firebaseTokenRepository.save(firebaseToken);
        FirebaseTopic firebaseTopic = new FirebaseTopic();
        firebaseTopic.id = principal.getName() + ":" + topic;
        firebaseTopic.userId = Long.parseLong(principal.getName());
        firebaseTopic.topic = topic;
        firebaseTopic.enabled = true;
        this.firebaseTopicRepository.save(firebaseTopic);
        try {
            FirebaseManager.subscribeToTopic(input.push_token, topic);
        } catch (FirebaseMessagingException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return new Output(ex.getMessage());
        }
        return new Output();
    }


    @PostMapping("/subscribe_events")
    public Output subscribeEvents(@RequestBody InputFirebaseToken input) {
        String topic = "events";
        try {
            FirebaseManager.subscribeToTopic(input.push_token, topic);
        } catch (FirebaseMessagingException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return new Output(ex.getMessage());
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

    @PostMapping("/unsubscribe_news")
    public Output unsubscribeNews(Principal principal) {
        String topic = "news";
        List<FirebaseToken> tokens = this.firebaseTokenRepository.findByUserId(Long.parseLong(principal.getName()));
        FirebaseTopic firebaseTopic = new FirebaseTopic();
        firebaseTopic.id = principal.getName() + ":" + topic;
        firebaseTopic.userId = Long.parseLong(principal.getName());
        firebaseTopic.topic = topic;
        firebaseTopic.enabled = false;
        this.firebaseTopicRepository.save(firebaseTopic);
        if (!tokens.isEmpty()) {
            List<String> list = tokens.stream().map(item -> item.id).collect(Collectors.toList());
            try {
                FirebaseManager.unsubscribeFromTopic(list, topic);
            } catch (FirebaseMessagingException ex) {
                Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
                return new Output(ex.getMessage());
            }
        }
        return new Output();
    }

    @PostMapping("/unsubscribe_events")
    public Output unsubscribeEvents(@RequestBody InputFirebaseToken input) {
        String topic = "events";
        try {
            FirebaseManager.unsubscribeFromTopic(Arrays.asList(input.push_token), topic);
        } catch (FirebaseMessagingException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return new Output(ex.getMessage());
        }
        return new Output();
    }
    
    @PostMapping("/send-to-user")
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
    
    @GetMapping("/send_news")
    public Output sendNews() {
        Map<String, String> data = new HashMap<String, String>() {{
            put("push_type", "news");
            put("push_data", "168");
        }};
        try {
            FirebaseManager.sendMessageToTopic("news", "news", "News Message", data);
            return new Output();
        } catch (FirebaseMessagingException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return new Output(ex.getMessage());
        }
    }

    @GetMapping("/send_events")
    public Output sendEvents() {
        Map<String, String> data = new HashMap<String, String>() {{
            put("push_type", "events");
        }};
        try {
            FirebaseManager.sendMessageToTopic("events", "events", "Events Message", data);
            return new Output();
        } catch (FirebaseMessagingException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return new Output(ex.getMessage());
        }
    }
    
    
     private class MessagePrinterTask implements Runnable {

        private String message;

        public MessagePrinterTask(String message) {
            this.message = message;
        }

        public void run() {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(message);
        }

    }
     
    @GetMapping("/test")
    public Output test() {
        this.taskExecutor.execute(new MessagePrinterTask("798798798798"));
        return new Output();
    }
    
    
}