package org.unibl.etf.emobility_hub.services.implementations;

import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.emobility_hub.exception.BadRequestException;
import org.unibl.etf.emobility_hub.exception.ConflictException;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.UserEntity;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedUserRequest;
import org.unibl.etf.emobility_hub.models.dto.response.UserResponse;
import org.unibl.etf.emobility_hub.repositories.UserEntityRepository;
import org.unibl.etf.emobility_hub.services.IUserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserEntityRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return repository.findAllByRoleIsNot(RoleEnum.ROLE_CLIENT, pageable).map(te -> mapper.map(te, UserResponse.class));
    }

    @Override
    public UserResponse getById(Long id) {
        return mapper.map(findById(id), UserResponse.class);
    }

    private UserEntity findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public UserResponse create(DetailedUserRequest request) {
        if (repository.existsByUsername(request.getUsername()))
            throw new ConflictException("Username already used");
        UserEntity user = mapper.map(request, UserEntity.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        try {
            RoleEnum newRole = RoleEnum.valueOf(request.getRole().toUpperCase());
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + request.getRole().toUpperCase());
        }
        repository.saveAndFlush(user);
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse update(DetailedUserRequest request) {
        if (!repository.existsById(request.getId()))
            throw new EntityNotFoundException("User not found");
        UserEntity user = findById(request.getId());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        try {
            RoleEnum newRole = RoleEnum.valueOf(request.getRole().toUpperCase());
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + request.getRole().toUpperCase());
        }
        repository.saveAndFlush(user);
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("User not found");
        repository.deleteById(id);
        repository.flush();
    }

    @Override
    public Page<UserResponse> getAllByUsername(Pageable pageable, String search) {
        return repository.getAllByUsernameContainingIgnoreCaseAndRoleIsNot(search, RoleEnum.ROLE_CLIENT, pageable)
                .map(te -> mapper.map(te, UserResponse.class));
    }
}
