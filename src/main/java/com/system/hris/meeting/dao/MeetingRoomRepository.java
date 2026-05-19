package com.system.hris.meeting.dao;

import com.system.hris.meeting.entity.MeetingRoom;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom,Long> {
    boolean existsByName(@NotBlank(message = "Room name is required") String name);

    Page<MeetingRoom> findByActiveTrue(Pageable pageable);

    @Query("SELECT r FROM MeetingRoom r WHERE r.active = true AND " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<MeetingRoom> searchActiveRooms(@Param("search") String search, Pageable pageable);
}
