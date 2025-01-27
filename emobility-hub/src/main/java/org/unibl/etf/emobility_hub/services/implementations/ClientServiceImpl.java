package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.emobility_hub.exception.ConflictException;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.ClientEntity;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;
import org.unibl.etf.emobility_hub.models.dto.request.detailed.DetailedClientRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ClientResponse;
import org.unibl.etf.emobility_hub.repositories.ClientEntityRepository;
import org.unibl.etf.emobility_hub.services.IClientService;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
public class ClientServiceImpl implements IClientService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ClientEntityRepository repository;

    @Value("${file.upload-dir}")
    private String baseDir;



    @Override
    public Page<ClientResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(te -> mapper.map(te, ClientResponse.class));
    }

    @Override
    public ClientResponse getById(Long id) {
        return mapper.map(findById(id), ClientResponse.class);
    }

    private ClientEntity findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public ClientResponse create(DetailedClientRequest request) {
        if (repository.existsByUsername(request.getUsername()))
            throw new ConflictException("Username already used");
        ClientEntity user = mapper.map(request, ClientEntity.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleEnum.ROLE_CLIENT);
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            MultipartFile image = request.getImage();
            String path = saveImageToFileSystem(image, user);
            user.setAvatarImage(path);
        }
        repository.saveAndFlush(user);
        return mapper.map(user, ClientResponse.class);
    }

    private String saveImageToFileSystem(MultipartFile file, ClientEntity client) {
        try {
            String dir = baseDir + File.separator;
            String uploadDir = new ClassPathResource(dir).getFile().getAbsolutePath() +
                    File.separator + ClientEntity.class.getSimpleName().toLowerCase() +
                    File.separator + client.getUsername() + File.separator;
            Path path = Paths.get(uploadDir);

            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to create directory: " + path, e);
                }
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = path.resolve(fileName);
            try {
                Files.copy(file.getInputStream(), filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to copy file: " + filePath, e);
            }
            return baseDir+ File.separator + ClientEntity.class.getSimpleName().toLowerCase() +
                    File.separator + client.getUsername() + File.separator
                    + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file to resources/uploads: " + e.getMessage(), e);
        }
    }

    @Override
    public ClientResponse update(DetailedClientRequest request) {
        if (!repository.existsById(request.getId()))
            throw new EntityNotFoundException("User not found");
        ClientEntity user = findById(request.getId());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            MultipartFile image = request.getImage();
            String path = saveImageToFileSystem(image, user);
            user.setAvatarImage(path);
        }

        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        repository.saveAndFlush(user);
        return mapper.map(user, ClientResponse.class);
    }

    @Override
    public void deleteById(Long id) {
        ClientEntity user = findById(id);
        deleteImageFromFileSystem(user.getAvatarImage());
        repository.deleteById(id);
        repository.flush();
    }

    @Override
    public void block(Long id, boolean isBlocked) {
        ClientEntity user = findById(id);
        user.setBlocked(isBlocked);
        repository.saveAndFlush(user);
    }

    private void deleteImageFromFileSystem(String imagePath) {
        Path filePath = Paths.get(imagePath);
        Path folderPath = filePath.getParent();

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("File deleted successfully: " + imagePath);
            } else {
                System.out.println("File does not exist: " + imagePath);
            }
            if (folderPath != null && Files.exists(folderPath)) {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
                    if (!directoryStream.iterator().hasNext()) {
                        Files.delete(folderPath);
                        System.out.println("Folder deleted successfully: " + folderPath);
                    } else {
                        System.out.println("Folder is not empty, cannot delete: " + folderPath);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + imagePath, e);
        }
    }
}
