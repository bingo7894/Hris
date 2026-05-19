package com.system.hris.meeting.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "meeting_rooms")
@Data
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    private String description;

    private Integer capacity;

    private String imageUuid;

    private boolean active  = true;

    @CreationTimestamp
    private Instant createdAt;

    @CreationTimestamp
    private Instant updateAt;

}
