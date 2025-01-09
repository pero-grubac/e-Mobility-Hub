package org.unibl.etf.emobility_hub.services.implementations;

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.dto.request.BaseRequest;
import org.unibl.etf.emobility_hub.services.IBaseService;

import java.util.Optional;

@Getter
@Transactional
public class BaseServiceImpl<TEntity, TRequest extends BaseRequest<ID>, TResponse, TDetailedResponse, ID> implements IBaseService<TRequest, TResponse, TDetailedResponse, ID> {

    private ModelMapper mapper;
    private JpaRepository<TEntity, ID> repository;
    private Class<TEntity> entityClass;
    private Class<TRequest> requestClass;
    private Class<TResponse> responseClass;
    private Class<TDetailedResponse> detailedResponseClass;


    public BaseServiceImpl(ModelMapper mapper, JpaRepository<TEntity, ID> repository, Class<TEntity> entityClass, Class<TRequest> requestClass, Class<TResponse> responseClass, Class<TDetailedResponse> detailedResponseClass) {
        this.mapper = mapper;
        this.repository = repository;
        this.entityClass = entityClass;
        this.requestClass = requestClass;
        this.responseClass = responseClass;
        this.detailedResponseClass = detailedResponseClass;
    }

    @Override
    public Page<TResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(te -> mapper.map(te, responseClass));
    }

    @Override
    public TDetailedResponse getById(ID id) {
        Optional<TEntity> entity = repository.findById(id);
        if (entity.isEmpty())
            throw new EntityNotFoundException(entityClass.getSimpleName() + " with ID " + id + " not found");
        return mapper.map(entity.get(), detailedResponseClass);
    }

    @Override
    public TResponse create(TRequest tRequest) {
        TEntity te = mapper.map(tRequest, entityClass);
        repository.saveAndFlush(te);
        return mapper.map(te, responseClass);
    }

    @Override
    public TResponse update(TRequest tRequest) {
        existsById(tRequest.getId());

        TEntity te = mapper.map(tRequest, entityClass);
        repository.saveAndFlush(te);
        return mapper.map(te, responseClass);
    }

    @Override
    public void delete(ID id) {
        existsById(id);
        repository.deleteById(id);
        repository.flush();
    }

    protected void existsById(ID id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException(entityClass.getSimpleName() + " with ID " + id + " not found");

    }
}
