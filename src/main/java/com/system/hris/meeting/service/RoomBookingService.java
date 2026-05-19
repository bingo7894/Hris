package com.system.hris.meeting.service;

import com.system.hris.meeting.dto.request.BookingRequest;
import com.system.hris.meeting.dto.response.BookingResponse;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import jakarta.validation.Valid;

import java.time.Instant;
import java.util.List;

public interface RoomBookingService {
    List<BookingResponse> getBookingsByRoomAndDate(Long id, Instant from, Instant to);

    MessageResponse bookRoom(Long id, String email, @Valid BookingRequest request);

    MessageResponse cancelBooking(Long id, String email);

    PageResponse<BookingResponse> getMyBookings(int page, int size, String email);
}
