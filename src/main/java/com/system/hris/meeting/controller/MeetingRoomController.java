package com.system.hris.meeting.controller;

import com.system.hris.meeting.dto.request.RoomRequest;
import com.system.hris.meeting.dto.response.RoomResponse;
import com.system.hris.meeting.service.MeetingRoomService;
import com.system.hris.meeting.service.RoomBookingService;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("api/meeting-room")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private RoomBookingService roomBookingService;

    //Admin

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<MessageResponse> createRoom(@Valid @RequestBody RoomRequest request){
        return ResponseEntity.ok(meetingRoomService.createRoom(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/rooms/{id}")
    public ResponseEntity<MessageResponse> updateRoom(@PathVariable Long id , @Valid @RequestBody RoomRequest request){
        return ResponseEntity.ok(meetingRoomService.updateRoom(id,request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/rooms/{id}")
    public ResponseEntity<MessageResponse> deleteRoom(@PathVariable Long id){
        return ResponseEntity.ok(meetingRoomService.deleteRoom(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/rooms/{id}/toggle-status")
    public ResponseEntity<MessageResponse> toggleStatus(@PathVariable Long id){
        return ResponseEntity.ok(meetingRoomService.toggleStatus(id));
    }

    //User
    @GetMapping("rooms")
    public ResponseEntity<PageResponse<RoomResponse>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search){
        return ResponseEntity.ok(meetingRoomService.getAllActiveRooms(page,size,search));
    }

    @GetMapping("rooms/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id){
        return ResponseEntity.ok(meetingRoomService.getRoomById(id));
    }
}
