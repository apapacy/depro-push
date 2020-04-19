/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.firebase;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andrey
 */
public class FirebaseManager {

    public static void subscribeToTopic(String token, String topic) throws FirebaseMessagingException {
        FirebaseMessaging.getInstance().subscribeToTopic(Arrays.asList(token), topic);
    }

    public static void unsubscribeFromTopic(List<String> tokens, String topic) throws FirebaseMessagingException {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(tokens, topic);
    }

    public static void sendMessageToTopic(String topic, String title, String body, Map<String, String> data) throws FirebaseMessagingException {
        Builder messageBuilder;
        if (data == null) {
            messageBuilder = Message.builder();
        } else {
            messageBuilder = Message.builder().putAllData(data);
        }
        Message message = messageBuilder
                .setNotification(new Notification(title, body))
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
                .setTopic(topic)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);
    }

        public static void sendMessageToToken(String token, String title, String body, Map<String, String> data) throws FirebaseMessagingException {
        Builder messageBuilder;
        if (data == null) {
            messageBuilder = Message.builder();
        } else {
            messageBuilder = Message.builder().putAllData(data);
        }
        Message message = messageBuilder
                .setNotification(new Notification(title, body))
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
                .setToken(token)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);
    }

    
}
