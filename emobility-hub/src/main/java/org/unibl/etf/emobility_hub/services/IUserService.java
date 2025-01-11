package org.unibl.etf.emobility_hub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedUserRequest;
import org.unibl.etf.emobility_hub.models.dto.response.UserResponse;

public interface IUserService {
    Page<UserResponse> getAll(Pageable pageable);

    UserResponse getById(Long id);

    UserResponse create(DetailedUserRequest request);

    UserResponse update(DetailedUserRequest request);

    void deleteById(Long id);

    UserResponse changeRole(Long id, String role);
}
