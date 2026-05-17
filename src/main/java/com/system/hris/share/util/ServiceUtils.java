package com.system.hris.share.util;

import com.system.hris.auth.dao.UserRepository;
import com.system.hris.knowledge.dao.VideoRepository;
import com.system.hris.auth.entity.User;
import com.system.hris.knowledge.entity.Video;
import com.system.hris.share.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtils {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    public User getUserByEmailOrThrow(String email){
        return  userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found with email:" +email));
    }

    public User getUserByIdOrThrow(Long id){
        return userRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found with id:"+id));
    }

    public Video getVideoByIdOrThrow(Long id){
        return  videoRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Video not found with id:"+id));

    }

}
