package net.beans.java.example.microservice.simple.repository;

import lombok.RequiredArgsConstructor;
import net.beans.java.example.microservice.simple.data.model.jpa.AbstractEntity;
import net.beans.java.example.microservice.simple.exception.EntityNotFoundException;
import org.hibernate.boot.model.source.spi.Sortable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class GenericRepository<T extends AbstractEntity, R extends JpaRepository<T, UUID>> {

    protected final R jpaRepository;

    protected final Class<T> type;

    public Optional<T> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    public T getById(UUID id) {
        return findById(id).orElseThrow(EntityNotFoundException.entityNotFoundException(type, id));
    }

    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    public T save(T object) {
        return jpaRepository.save(object);
    }

}
