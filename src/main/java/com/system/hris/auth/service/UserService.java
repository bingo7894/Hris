package com.system.hris.auth.service;

import com.system.hris.auth.dto.request.UserRequest;
import com.system.hris.share.dto.response.MessageResponse;
import com.system.hris.share.dto.response.PageResponse;
import com.system.hris.auth.dto.response.UserResponse;

public interface UserService {
    MessageResponse createUser(UserRequest userRequest);

    MessageResponse updateUser(Long id, UserRequest userRequest);

    PageResponse<UserResponse> getUsers(int page, int size, String search);

    MessageResponse deleteUser(Long id, String currentUserEmail);

    MessageResponse toggleUserStatus(Long id, String currentUserEmail);

    MessageResponse changeRole(Long id, UserRequest userRequest);
}
