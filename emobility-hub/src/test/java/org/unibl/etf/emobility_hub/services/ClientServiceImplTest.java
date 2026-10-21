package org.unibl.etf.emobility_hub.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unibl.etf.emobility_hub.EMobilityHubApplication;
import org.unibl.etf.emobility_hub.exception.ConflictException;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.ClientEntity;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedClientRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;
import org.unibl.etf.emobility_hub.repositories.ClientEntityRepository;
import org.unibl.etf.emobility_hub.services.implementations.ClientServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    ClientEntityRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Spy
    ModelMapper mapper = new EMobilityHubApplication().modelMapper();

    @InjectMocks
    ClientServiceImpl service;

    static ClientEntity client(long id) {
        ClientEntity client = new ClientEntity();
        client.setId(id);
        client.setUsername("client" + id);
        client.setPassword("hash");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john@example.com");
        client.setPhoneNumber("+387-65-111-111");
        client.setIdCardNumber("ID-001");
        client.setRole(RoleEnum.ROLE_CLIENT);
        return client;
    }

    @Test
    void blockAndUnblock() {
        ClientEntity client = client(4);
        when(repository.findById(4L)).thenReturn(Optional.of(client));

        service.block(4L, true);
        assertThat(client.isBlocked()).isTrue();

        service.block(4L, false);
        assertThat(client.isBlocked()).isFalse();
    }

    @Test
    void deletingAClientWithoutAvatarWorks() {
        when(repository.findById(4L)).thenReturn(Optional.of(client(4)));

        service.deleteById(4L);

        verify(repository).deleteById(4L);
    }

    @Test
    void deletingAnUnknownClientFails() {
        when(repository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteById(9L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void registrationAlwaysCreatesAClient() {
        when(repository.existsByUsername("newbie")).thenReturn(false);
        when(passwordEncoder.encode("pw")).thenReturn("hashed");
        DetailedClientRequest request = new DetailedClientRequest();
        request.setUsername("newbie");
        request.setPassword("pw");
        request.setFirstName("New");
        request.setLastName("Bie");
        request.setEmail("new@example.com");
        request.setPhoneNumber("+387-65-000-000");
        request.setIdCardNumber("ID-999");

        ClientResponse response = service.create(request);

        verify(repository).saveAndFlush(argThat(c -> c.getRole() == RoleEnum.ROLE_CLIENT && c.getPassword().equals("hashed")));
        assertThat(response.getUsername()).isEqualTo("newbie");
    }

    @Test
    void registrationRejectsATakenUsername() {
        when(repository.existsByUsername("client4")).thenReturn(true);
        DetailedClientRequest request = new DetailedClientRequest();
        request.setUsername("client4");

        assertThatThrownBy(() -> service.create(request)).isInstanceOf(ConflictException.class);
        verify(repository, never()).saveAndFlush(any());
    }
}
