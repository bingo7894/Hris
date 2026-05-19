package com.system.hris.meeting.controller;

import com.system.hris.meeting.dto.request.BookingRequest;
import com.system.hris.meeting.dto.response.BookingResponse;
import com.system.hris.meeting.service.RoomBookingService;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/room-bookings")
public class RoomBookingsController {

    @Autowired
    private RoomBookingService roomBookingService;

    @GetMapping("rooms/{id}/bookings")
    public ResponseEntity<List<BookingResponse>> getRoomBookings(
            @PathVariable Long id,
            @RequestParam Instant from,
            @RequestParam Instant to){
        return ResponseEntity.ok(roomBookingService.getBookingsByRoomAndDate(id,from,to));
    }

    @PostMapping("/rooms/{id}/bookings")
    public ResponseEntity<MessageResponse> bookRoom(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequest request,
            Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(roomBookingService.bookRoom(id,email,request));
    }

    @DeleteMapping("/bookings/{id}/cancel")
    public ResponseEntity<MessageResponse> cancelBooking(
            @PathVariable Long id,
            Authentication authentication
    ){
        String email = authentication.getName();
        return ResponseEntity.ok(roomBookingService.cancelBooking(id,email));
    }

    @GetMapping("my-bookings")
    public ResponseEntity<PageResponse<BookingResponse>> getMyBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ){
        String email =authentication.getName();
        return ResponseEntity.ok(roomBookingService.getMyBookings(page,size,email));
    }

}
