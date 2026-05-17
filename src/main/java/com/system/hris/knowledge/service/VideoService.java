package com.system.hris.knowledge.service;

import com.system.hris.knowledge.dto.request.VideoRequest;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import com.system.hris.knowledge.dto.response.VideoResponse;
import com.system.hris.knowledge.dto.response.VideoStatsResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface VideoService {
    MessageResponse createVideoByAdmin(@Valid VideoRequest videoRequest);

    PageResponse<VideoResponse> getAllAdminVideos(int page, int size, String search);

    MessageResponse updateVideoByAdmin(Long id, @Valid VideoRequest videoRequest);

    MessageResponse deleteVideoByAdmin(Long id);

    MessageResponse toggleVideoPublishStatusByAdmin(Long id, boolean value);

    VideoStatsResponse getAdminStats();

    PageResponse<VideoResponse> getPublishedVideo(int page, int size, String search, String email);

    List<VideoResponse> getFeaturedVideos();
}
