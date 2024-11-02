package net.beans.java.example.microservice.simple.repository;

import lombok.RequiredArgsConstructor;
import net.beans.java.example.microservice.simple.data.model.jpa.AbstractEntity;
import net.beans.java.example.microservice.simple.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
