package com.system.hris.meeting.dto.response;

import com.system.hris.meeting.entity.RoomBooking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private Long roomId;
    private String roomName;
    private String bookByEmail;
    private String bookByName;
    private String title;
    private String description;
    private Instant startTime;
    private Instant endTime;
    private String status;
    private Instant createdAt;

    public static BookingResponse fromEntity(RoomBooking booking){
        return new BookingResponse(
                booking.getId(),
                booking.getRoom().getId(),
                booking.getRoom().getName(),
                booking.getBookedBy().getEmail(),
                booking.getBookedBy().getFullName(),
                booking.getTitle(),
                booking.getDescription(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus().name(),
                booking.getCreateAt()
        );
    }
}
