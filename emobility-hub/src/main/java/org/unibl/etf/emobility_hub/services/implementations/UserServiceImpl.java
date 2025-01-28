package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserEntityRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(te -> mapper.map(te, UserResponse.class));
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
        user.setRole(RoleEnum.ROLE_PENDING);
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
    public UserResponse changeRole(Long id, String role) {
        UserEntity user = findById(id);
        try {
            RoleEnum newRole = RoleEnum.valueOf(role.toUpperCase());
            user.setRole(newRole);
            repository.saveAndFlush(user);
            return mapper.map(user, UserResponse.class);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + role);
        }
    }

    @Override
    public Page<UserResponse> getAllByUsername(Pageable pageable, String search) {
        return repository.getAllByUsernameContainingIgnoreCase(search, pageable).map(te -> mapper.map(te, UserResponse.class));
    }
}
