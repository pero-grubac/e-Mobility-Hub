package org.unibl.etf.emobility_hub.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unibl.etf.emobility_hub.EMobilityHubApplication;
import org.unibl.etf.emobility_hub.exception.BadRequestException;
import org.unibl.etf.emobility_hub.exception.ConflictException;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.UserEntity;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedUserRequest;
import org.unibl.etf.emobility_hub.models.dto.response.UserResponse;
import org.unibl.etf.emobility_hub.repositories.UserEntityRepository;
import org.unibl.etf.emobility_hub.services.implementations.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserEntityRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Spy
    ModelMapper mapper = new EMobilityHubApplication().modelMapper();

    @InjectMocks
    UserServiceImpl service;

    static DetailedUserRequest request(String username, String password, String role) {
        DetailedUserRequest request = new DetailedUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setFirstName("First");
        request.setLastName("Last");
        request.setRole(role);
        return request;
    }

    static UserEntity existing(long id, RoleEnum role) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername("old");
        user.setPassword("old-hash");
        user.setFirstName("Old");
        user.setLastName("Name");
        user.setRole(role);
        return user;
    }

    @Test
    void createHashesThePasswordAndParsesTheRole() {
        when(repository.existsByUsername("op2")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("hashed");

        UserResponse response = service.create(request("op2", "secret", "role_operator"));

        ArgumentCaptor<UserEntity> saved = ArgumentCaptor.forClass(UserEntity.class);
        verify(repository).saveAndFlush(saved.capture());
        assertThat(saved.getValue().getPassword()).isEqualTo("hashed");
        assertThat(saved.getValue().getRole()).isEqualTo(RoleEnum.ROLE_OPERATOR);
        assertThat(response.getUsername()).isEqualTo("op2");
    }

    @Test
    void createRejectsATakenUsername() {
        when(repository.existsByUsername("admin")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request("admin", "x", "ROLE_ADMIN")))
                .isInstanceOf(ConflictException.class);
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void createRejectsAnUnknownRole() {
        when(repository.existsByUsername("x")).thenReturn(false);

        assertThatThrownBy(() -> service.create(request("x", "x", "ROLE_SUPERUSER")))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid role: ROLE_SUPERUSER");
    }

    @Test
    void updateKeepsThePasswordWhenNoneIsGiven() {
        UserEntity user = existing(3L, RoleEnum.ROLE_MANAGER);
        when(repository.existsById(3L)).thenReturn(true);
        when(repository.findById(3L)).thenReturn(Optional.of(user));
        DetailedUserRequest request = request("manager2", "", "ROLE_MANAGER");
        request.setId(3L);

        service.update(request);

        assertThat(user.getPassword()).isEqualTo("old-hash");
        assertThat(user.getUsername()).isEqualTo("manager2");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateOfAMissingUserFails() {
        DetailedUserRequest request = request("x", "x", "ROLE_ADMIN");
        request.setId(42L);
        when(repository.existsById(42L)).thenReturn(false);

        assertThatThrownBy(() -> service.update(request)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteOfAMissingUserFails() {
        when(repository.existsById(9L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteById(9L)).isInstanceOf(EntityNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void employeeListExcludesClients() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAllByRoleIsNot(RoleEnum.ROLE_CLIENT, pageable)).thenReturn(Page.empty());

        service.getAll(pageable);

        verify(repository).findAllByRoleIsNot(RoleEnum.ROLE_CLIENT, pageable);
    }
}
