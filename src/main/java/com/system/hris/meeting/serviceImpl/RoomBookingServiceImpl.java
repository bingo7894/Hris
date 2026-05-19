package com.system.hris.meeting.serviceImpl;

import com.system.hris.auth.entity.User;
import com.system.hris.meeting.dao.MeetingRoomRepository;
import com.system.hris.meeting.dao.RoomBookingsRepository;
import com.system.hris.meeting.dto.request.BookingRequest;
import com.system.hris.meeting.dto.response.BookingResponse;
import com.system.hris.meeting.entity.MeetingRoom;
import com.system.hris.meeting.entity.RoomBooking;
import com.system.hris.meeting.enums.BookingStatus;
import com.system.hris.meeting.service.RoomBookingService;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import com.system.hris.share.exception.ResourceNotFoundException;
import com.system.hris.share.util.PaginationUtils;
import com.system.hris.share.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RoomBookingServiceImpl implements RoomBookingService {

    @Autowired
    private RoomBookingsRepository roomBookingsRepository;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private ServiceUtils serviceUtils;


    @Override
    public List<BookingResponse> getBookingsByRoomAndDate(Long roomId, Instant from, Instant to) {
        return roomBookingsRepository
                .findBookingsByRoomAndDateRange(roomId, from, to)
                .stream()
                .map(BookingResponse::fromEntity)
                .toList();
    }

    @Override
    public MessageResponse bookRoom(Long roomId, String email, BookingRequest request) {

        //Validate Time
        if(!request.getEndTime().isAfter(request.getStartTime())){
            throw new IllegalArgumentException("End Time must be after Start Time");
        }

        if(request.getStartTime().isBefore(Instant.now())){
            throw new IllegalArgumentException("Now must be after Start Time");
        }

        //find room
        MeetingRoom room = meetingRoomRepository.findById(roomId)
                .orElseThrow(()->new ResourceNotFoundException("Room not found"));

        if(!room.isActive()){
            throw new IllegalArgumentException("Room is not available");
        }

        //conflict room
        boolean hasConflict = roomBookingsRepository.existsConflict(
                roomId,
                request.getStartTime(),
                request.getEndTime()
        );

        if(hasConflict){
            throw new IllegalArgumentException("Room is already booked for this time");
        }

        User user = serviceUtils.getUserByEmailOrThrow(email);

        RoomBooking booking = new RoomBooking();
        booking.setRoom(room);
        booking.setBookedBy(user);
        booking.setTitle(request.getTitle());
        booking.setDescription(request.getDescription());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus(BookingStatus.CONFIRMED);

        roomBookingsRepository.save(booking);

        return new MessageResponse("Room booked Successfully");
    }

    @Override
    public MessageResponse cancelBooking(Long id, String email) {
        RoomBooking booking = roomBookingsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found"));

        if(!booking.getBookedBy().getEmail().equals(email)){
            throw new IllegalArgumentException("You can only cancel own booking");
        }

        if(booking.getStatus().equals(BookingStatus.CANCELLED)){
            throw new IllegalArgumentException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        roomBookingsRepository.save(booking);

        return new MessageResponse("cancel Booking Successfully");
    }

    @Override
    public PageResponse<BookingResponse> getMyBookings(int page, int size, String email) {
        Pageable pageable = PaginationUtils.createPageRequest(page,size,"StartTime");

        Page<RoomBooking> bookingsPage = roomBookingsRepository
                .findByBookedByEmailAndStatus(email,BookingStatus.CONFIRMED,pageable);

        return PaginationUtils.toPageResponse(bookingsPage,BookingResponse::fromEntity);
    }


}
