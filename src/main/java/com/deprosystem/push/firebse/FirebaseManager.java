/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.firebse;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.Arrays;
import java.util.List;


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

}
