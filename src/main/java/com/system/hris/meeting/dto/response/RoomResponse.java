package com.system.hris.meeting.dto.response;

import com.system.hris.meeting.entity.MeetingRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String name;
    private String description;
    private Integer capacity;
    private String imageUuid;
    private boolean active;
    private Instant createdAt;

    public static RoomResponse fromEntity(MeetingRoom room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getDescription(),
                room.getCapacity(),
                room.getImageUuid(),
                room.isActive(),
                room.getCreatedAt()
        );
    }
}
