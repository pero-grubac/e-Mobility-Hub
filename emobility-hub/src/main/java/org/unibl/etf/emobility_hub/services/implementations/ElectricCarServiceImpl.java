package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.emobility_hub.base.services.impl.BaseVehicleCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricCarEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricCarRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricCarResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricCarResponse;
import org.unibl.etf.emobility_hub.repositories.ElectricCarEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.IElectricCarService;

import java.time.LocalDateTime;

@Service
@Transactional
public class ElectricCarServiceImpl
        extends BaseVehicleCRUDServiceImpl<ElectricCarEntityRepository, ElectricCarEntity, ElectricCarRequest, ElectricCarResponse, DetailedElectricCarResponse>
        implements IElectricCarService {
    @Autowired
    public ElectricCarServiceImpl(ModelMapper mapper, ElectricCarEntityRepository repository, ManufacturerEntityRepository manufacturerEntityRepository) {
        super(mapper, repository, ElectricCarEntity.class, ElectricCarRequest.class, ElectricCarResponse.class, DetailedElectricCarResponse.class, manufacturerEntityRepository);
    }

    @Override
    public ElectricCarResponse create(ElectricCarRequest request) {
        ManufacturerEntity manufacturerEntity = findManufacturerById(request.getManufacturerId());

        ElectricCarEntity entity = getMapper().map(request, getEntityClass());
        entity.setId(null);
        entity.setManufacturer(manufacturerEntity);
        entity.setPurchaseDate(LocalDateTime.parse(request.getPurchaseDate()));
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            MultipartFile imageFile = request.getImage();
            getRepository().saveAndFlush(entity);
            String imagePath = saveImageToFileSystem(imageFile, entity);
            entity.setImage(imagePath);
        }
        getRepository().saveAndFlush(entity);

        return getMapper().map(entity, getResponseClass());
    }

    @Override
    public ElectricCarResponse update(ElectricCarRequest request) {
        ManufacturerEntity manufacturerEntity = findManufacturerById(request.getManufacturerId());

        ElectricCarEntity entity = findById(request.getId());
        entity.setModel(request.getModel());
        entity.setPurchasePrice(request.getPurchasePrice());
        entity.setManufacturer(manufacturerEntity);
        entity.setPurchaseDate(LocalDateTime.parse(request.getPurchaseDate()));
        entity.setPurchasePrice(request.getPurchasePrice());
        entity.setDescription(request.getDescription());
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            MultipartFile imageFile = request.getImage();
            String imagePath = saveImageToFileSystem(imageFile, entity);
            entity.setImage(imagePath);
        }
        getRepository().saveAndFlush(entity);

        return getMapper().map(entity, getResponseClass());
    }

 /*   @Override
    public Page<ElectricCarResponse> getAllByModel(String model, Pageable pageable) {
        return ((ElectricCarEntityRepository) super.getRepository()).findAllByModelIgnoreCaseContaining(model, pageable)
                .map(ece -> getMapper().map(ece, getResponseClass()));

    }*/
}
