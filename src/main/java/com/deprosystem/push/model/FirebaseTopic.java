/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deprosystem.push.model;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "firebase_topic")
public class FirebaseTopic {
   
    @Id
    @Column(length = 4096)
    public String id;

    @Column(unique = false)
    public long userId;
    
    @Column(length = 4096)
    public String topic;
    
    public boolean enabled;
    
    private OffsetDateTime created;

    private OffsetDateTime updated;
    
    @PrePersist
    protected void onCreate() {
        created = OffsetDateTime.now(ZoneId.of("UTC"));
        updated = OffsetDateTime.now(ZoneId.of("UTC"));
    }

    @PreUpdate
    protected void onUpdate() {
        updated = OffsetDateTime.now(ZoneId.of("UTC"));
    } 
}
