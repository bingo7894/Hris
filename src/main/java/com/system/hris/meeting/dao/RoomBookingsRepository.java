package com.system.hris.meeting.dao;

import com.system.hris.meeting.dto.response.BookingResponse;
import com.system.hris.meeting.entity.RoomBooking;
import com.system.hris.meeting.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RoomBookingsRepository extends JpaRepository<RoomBooking,Long> {

    @Query("""
    SELECT b FROM RoomBooking b
    WHERE b.room.id = :roomId
    AND b.status = 'CONFIRMED'
    AND b.startTime >= :from
    AND b.endTime <= :to
    ORDER BY b.startTime ASC
""")
    List<RoomBooking> findBookingsByRoomAndDateRange(
            @Param("roomId") Long roomId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );


    @Query("""
        SELECT COUNT(b) > 0 FROM RoomBooking b
        WHERE b.room.id = :roomId
        AND b.status = 'CONFIRMED'
        AND b.startTime < :endTime
        AND b.endTime > :startTime
    """)
    boolean existsConflict(
            @Param("roomId") Long roomId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );

    @Query("""
        SELECT COUNT(b) > 0 FROM RoomBooking b
        WHERE b.room.id = :roomId
        AND b.status = 'CONFIRMED'
        AND b.id != :excludeId
        AND b.startTime < :endTime
        AND b.endTime > :startTime
    """)
    boolean existsConflictExcluding(
            @Param("roomId") Long roomId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime,
            @Param("excludeId") Long excludeId
    );

    Page<RoomBooking> findByBookedByEmailAndStatus(String email, BookingStatus bookingStatus, Pageable pageable);
}
