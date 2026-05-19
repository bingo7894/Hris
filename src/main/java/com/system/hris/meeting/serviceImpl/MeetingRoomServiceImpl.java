package com.system.hris.meeting.serviceImpl;

import com.system.hris.meeting.dao.MeetingRoomRepository;
import com.system.hris.meeting.dto.request.RoomRequest;
import com.system.hris.meeting.dto.response.RoomResponse;
import com.system.hris.meeting.entity.MeetingRoom;
import com.system.hris.meeting.service.MeetingRoomService;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import com.system.hris.share.exception.ResourceNotFoundException;
import com.system.hris.share.util.PaginationUtils;
import com.system.hris.share.util.ServiceUtils;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private ServiceUtils serviceUtils;

    @Override
    public MessageResponse createRoom(RoomRequest request) {
        if(meetingRoomRepository.existsByName(request.getName())){
            throw new ResourceNotFoundException("Room name already exists");
        }

        MeetingRoom room = new MeetingRoom();
        room.setName(request.getName());
        room.setDescription(request.getDescription());
        room.setCapacity(request.getCapacity());
        room.setImageUuid(request.getImageUuid());
        room.setActive(true);

        meetingRoomRepository.save(room);

        return new MessageResponse("Room created successfully");
    }

    @Override
    public MessageResponse updateRoom(Long id, RoomRequest request) {
        MeetingRoom room = meetingRoomRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found"));

        if(!room.getName().equals(request.getName()) && meetingRoomRepository.existsByName(request.getName())){
            throw new ResourceNotFoundException("Room name already exists");
        }

        if(request.getName() != null) room.setName(request.getName());
        if(request.getDescription() != null) room.setDescription(request.getDescription());
        if (request.getCapacity() != null) room.setCapacity(request.getCapacity());
        if(request.getImageUuid() != null) room.setImageUuid(request.getImageUuid());

        meetingRoomRepository.save(room);

        return new MessageResponse("Room Update Successfully");
    }

    @Override
    public MessageResponse deleteRoom(Long id) {
        if(!meetingRoomRepository.existsById(id)){
            throw new ResourceNotFoundException("Room not found");
        }
        meetingRoomRepository.deleteById(id);
        return new MessageResponse("Room delete Successfully");
    }

    @Override
    public MessageResponse toggleStatus(Long id) {
        MeetingRoom room = meetingRoomRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found"));
        room.setActive(!room.isActive());
        meetingRoomRepository.save(room);
        return new MessageResponse("Change status Room Successfully");
    }

    @Override
    public PageResponse<RoomResponse> getAllActiveRooms(int page, int size, String search) {
        Pageable pageable = PaginationUtils.createPageRequest(page,size,"id");
        Page<MeetingRoom> roomPage;

        if(search != null && !search.trim().isEmpty()){
            roomPage = meetingRoomRepository.searchActiveRooms(search.trim(),pageable);
        }else {
            roomPage = meetingRoomRepository.findByActiveTrue(pageable);
        }
        return PaginationUtils.toPageResponse(roomPage, RoomResponse::fromEntity);
    }

    @Override
    public RoomResponse getRoomById(Long id) {
        MeetingRoom room = meetingRoomRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Room not found"));
        return RoomResponse.fromEntity(room);
    }


}
