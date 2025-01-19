package org.unibl.etf.emobility_hub.base.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.emobility_hub.base.services.IBaseVehicleCRUDService;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.TransportVehicleEntity;
import org.unibl.etf.emobility_hub.models.dto.request.TransportVehicleRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ManufacturerResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.IContainManufacturer;
import org.unibl.etf.emobility_hub.repositories.JpaTransportVehicleRepository;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
@Transactional
public class BaseVehicleCRUDServiceImpl<TRepository extends JpaTransportVehicleRepository<TEntity>, TEntity extends TransportVehicleEntity, TRequest extends TransportVehicleRequest, TResponse, TDetailedResponse extends IContainManufacturer>
        implements IBaseVehicleCRUDService<TRequest, TResponse, TDetailedResponse> {

    private final ModelMapper mapper;
    private final TRepository repository;
    private final Class<TEntity> entityClass;
    private final Class<TRequest> requestClass;
    private final Class<TResponse> responseClass;
    private final Class<TDetailedResponse> detailedResponseClass;
    private final ManufacturerEntityRepository manufacturerEntityRepository;

    @Value("${file.upload-dir}")
    private String baseDir;

    public BaseVehicleCRUDServiceImpl(ModelMapper mapper, TRepository repository, Class<TEntity> entityClass, Class<TRequest> requestClass, Class<TResponse> responseClass, Class<TDetailedResponse> detailedResponseClass, ManufacturerEntityRepository manufacturerEntityRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.entityClass = entityClass;
        this.requestClass = requestClass;
        this.responseClass = responseClass;
        this.detailedResponseClass = detailedResponseClass;
        this.manufacturerEntityRepository = manufacturerEntityRepository;
    }

    @Override
    public Page<TResponse> findAllBroken(Pageable pageable) {
        return getAll(pageable, true);
    }

    @Override
    public Boolean rent(Long id) {
        TEntity te = findById(id);
        if (te.isRented())
            return false;
        te.setRented(true);
        getRepository().save(te);
        return true;
    }

    @Override
    public void fix(Long id) {
        setIsBroken(id, false);
    }

    @Override
    public void brake(Long id) {
        setIsBroken(id, true);
    }

    @Override
    public Page<TResponse> findAll(Pageable pageable) {
        return getAll(pageable, false);
    }

    @Override
    public TDetailedResponse getById(Long id) {
        TEntity entity = findById(id);
        TDetailedResponse respone = mapper.map(entity, detailedResponseClass);
        respone.setManufacturer(mapper.map(entity.getManufacturer(), ManufacturerResponse.class));
        return respone;
    }


    @Override
    public TResponse create(TRequest request) {
        ManufacturerEntity manufacturerEntity = findManufacturerById(request.getManufacturerId());

        TEntity te = getMapper().map(request, getEntityClass());
        te.setId(null);
        te.setManufacturer(manufacturerEntity);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            MultipartFile imageFile = request.getImage();
            getRepository().saveAndFlush(te);
            String imagePath = saveImageToFileSystem(imageFile, te);
            te.setImage(imagePath);
        }
        getRepository().saveAndFlush(te);

        return getMapper().map(te, getResponseClass());
    }

    private String getBaseUrl() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request attributes found. Ensure this method is called in the context of an HTTP request.");
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getScheme() + "://" + // Protokol (http ili https)
                request.getServerName() + // Ime servera (localhost ili domen)
                ":" + request.getServerPort() + // Port (8081)
                request.getContextPath() + "/"; // Kontekst aplikacije (/eMobilityHubUser)
    }

    protected String saveImageToFileSystem(MultipartFile imageFile, TEntity te) {
        String uploadDir = baseDir + File.separator + TransportVehicleEntity.class.getSimpleName().toLowerCase()
                + File.separator + te.getClass().getSimpleName().toLowerCase() + File.separator
                + te.getId() + File.separator;
        Path uploadPath = Paths.get(uploadDir);
        System.out.println("uploadDir " + uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + uploadPath, e);
            }
        }

        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        System.out.println("filePath " + filePath);

        try {
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file to: " + filePath, e);
        }
        String baseUrl = getBaseUrl();

        return baseUrl + uploadDir + fileName;
    }

    @Override
    public TResponse update(TRequest request) {
        ManufacturerEntity manufacturerEntity = findManufacturerById(request.getManufacturerId());

        TEntity te = findById(request.getId());
        te.setModel(request.getModel());
        te.setPurchasePrice(request.getPurchasePrice());
        te.setManufacturer(manufacturerEntity);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            MultipartFile imageFile = request.getImage();
            String imagePath = saveImageToFileSystem(imageFile, te);
            te.setImage(imagePath);
        }
        getRepository().saveAndFlush(te);

        return getMapper().map(te, getResponseClass());
    }


    protected ManufacturerEntity findManufacturerById(Long id) {
        ManufacturerEntity entity = manufacturerEntityRepository.findById(id).orElse(null);
        if (entity == null)
            throw new EntityNotFoundException("Manufacturer with ID " + id + " not found");
        return entity;
    }

    @Override
    public void delete(Long id) {
        TEntity te = findById(id);
        deleteImageFromFileSystem(te.getImage());
        repository.deleteById(id);
        getRepository().flush();
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

    protected void setIsBroken(Long id, Boolean isBroken) {
        TEntity te = findById(id);
        te.setBroken(isBroken);
        getRepository().saveAndFlush(te);
    }

    protected Page<TResponse> getAll(Pageable pageable, Boolean isBroken) {
        return repository.findAllByIsBroken(pageable, isBroken).map(e -> getMapper().map(e, getResponseClass()));
    }

    protected void existsById(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException(entityClass.getSimpleName() + " with ID " + id + " not found");

    }

    protected TEntity findById(Long id) {
        TEntity te = repository.findById(id).orElse(null);
        if (te == null)
            throw new EntityNotFoundException(entityClass.getSimpleName() + " with ID " + id + " not found");
        return te;
    }
}
