package com.system.hris.knowledge.service;

import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import com.system.hris.knowledge.dto.response.VideoResponse;

public interface WatchListService {
    MessageResponse addToWatchlist(String email, Long videoId);

    MessageResponse removeWatchList(String email, Long videoId);

    PageResponse<VideoResponse> getWatchListPaginated(String email, int page, int size, String search);
}
