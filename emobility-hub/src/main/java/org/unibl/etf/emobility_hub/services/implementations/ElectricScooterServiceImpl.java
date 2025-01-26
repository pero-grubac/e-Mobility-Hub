package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.emobility_hub.base.services.impl.BaseVehicleCRUDServiceImpl;
import org.unibl.etf.emobility_hub.models.domain.entity.ElectricScooterEntity;
import org.unibl.etf.emobility_hub.models.domain.entity.ManufacturerEntity;
import org.unibl.etf.emobility_hub.models.dto.request.ElectricScooterRequest;
import org.unibl.etf.emobility_hub.models.dto.response.ElectricScooterResponse;
import org.unibl.etf.emobility_hub.models.dto.response.detailed.DetailedElectricScooterResponse;
import org.unibl.etf.emobility_hub.repositories.ElectricScooterEntityRepository;
import org.unibl.etf.emobility_hub.repositories.ManufacturerEntityRepository;
import org.unibl.etf.emobility_hub.services.IElectricScooterService;


@Service
@Transactional
public class ElectricScooterServiceImpl
        extends BaseVehicleCRUDServiceImpl<ElectricScooterEntityRepository, ElectricScooterEntity, ElectricScooterRequest, ElectricScooterResponse, DetailedElectricScooterResponse>
        implements IElectricScooterService {
    public ElectricScooterServiceImpl(ModelMapper mapper, ElectricScooterEntityRepository repository, ManufacturerEntityRepository manufacturerEntityRepository) {
        super(mapper, repository, ElectricScooterEntity.class, ElectricScooterRequest.class, ElectricScooterResponse.class, DetailedElectricScooterResponse.class, manufacturerEntityRepository);
    }


    @Override
    public ElectricScooterResponse create(ElectricScooterRequest request) {
        ManufacturerEntity manufacturerEntity = findManufacturerById(request.getManufacturerId());

        ElectricScooterEntity entity = getMapper().map(request, getEntityClass());
        entity.setId(null);
        entity.setManufacturer(manufacturerEntity);
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
    public ElectricScooterResponse update(ElectricScooterRequest request) {
        ManufacturerEntity manufacturerEntity = findManufacturerById(request.getManufacturerId());

        ElectricScooterEntity entity = findById(request.getId());
        entity.setModel(request.getModel());
        entity.setPurchasePrice(request.getPurchasePrice());
        entity.setManufacturer(manufacturerEntity);
        entity.setPurchasePrice(request.getPurchasePrice());
        entity.setMaxSpeed(request.getMaxSpeed());
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            MultipartFile imageFile = request.getImage();
            String imagePath = saveImageToFileSystem(imageFile, entity);
            entity.setImage(imagePath);
        }
        getRepository().saveAndFlush(entity);

        return getMapper().map(entity, getResponseClass());
    }
}
