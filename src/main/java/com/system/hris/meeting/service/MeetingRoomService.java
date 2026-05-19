package com.system.hris.meeting.service;

import com.system.hris.meeting.dto.request.RoomRequest;
import com.system.hris.meeting.dto.response.RoomResponse;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import jakarta.validation.Valid;

public interface MeetingRoomService {
    MessageResponse createRoom(@Valid RoomRequest request);

    MessageResponse updateRoom(Long id, @Valid RoomRequest request);

    MessageResponse deleteRoom(Long id);

    MessageResponse toggleStatus(Long id);

    PageResponse<RoomResponse> getAllActiveRooms(int page, int size, String search);

    RoomResponse getRoomById(Long id);

}
