package com.system.hris.knowledge.serviceImpl;

import com.system.hris.auth.dao.UserRepository;
import com.system.hris.knowledge.dao.VideoRepository;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import com.system.hris.knowledge.dto.response.VideoResponse;
import com.system.hris.auth.entity.User;
import com.system.hris.knowledge.entity.Video;
import com.system.hris.knowledge.service.WatchListService;
import com.system.hris.share.util.PaginationUtils;
import com.system.hris.share.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ServiceUtils serviceUtils;

    @Override
    public MessageResponse addToWatchlist(String email, Long videoId) {

        User user = serviceUtils.getUserByEmailOrThrow(email);

        Video video = serviceUtils.getVideoByIdOrThrow(videoId);

        user.addToWatchList(video);
        userRepository.save(user);
        return new MessageResponse("Video added to watchlist successfully");

    }

    @Override
    public MessageResponse removeWatchList(String email, Long videoId) {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        Video video = serviceUtils.getVideoByIdOrThrow(videoId);

        user.removeFromWatchList(video);
        userRepository.save(user);
        return  new MessageResponse("Video removed from watchlist successfully. ");
    }

    @Override
    public PageResponse<VideoResponse> getWatchListPaginated(String email, int page, int size, String search) {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        Pageable pageable = PaginationUtils.createPageRequest(page,size);
        Page<Video> videoPage;

        if(search != null && !search.trim().isEmpty()){
            videoPage = userRepository.searchWatchListByUserId(user.getId(),search.trim(),pageable);
        }else {
            videoPage = userRepository.findWatchVideoUserId(user.getId(),pageable);
        }

        return  PaginationUtils.toPageResponse(videoPage,VideoResponse::fromEntity);

    }
}
