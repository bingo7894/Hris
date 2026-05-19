package com.system.hris.meeting.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoomRequest {
    @NotBlank(message = "Room name is required")
    private String name;
    private String description;
    private Integer capacity;
    private String imageUuid;
}
