package com.system.hris.meeting.entity;

import com.system.hris.auth.entity.User;
import com.system.hris.meeting.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "room_bookings")
@Data
public class RoomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id",nullable = false)
    private MeetingRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booked_by",nullable = false)
    private User bookedBy;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @CreationTimestamp
    private Instant createAt;

    @UpdateTimestamp
    private Instant updateAt;

}
