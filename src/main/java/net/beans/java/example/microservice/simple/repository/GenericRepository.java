package net.beans.java.example.microservice.simple.repository;

import lombok.RequiredArgsConstructor;
import net.beans.java.example.microservice.simple.data.model.jpa.AbstractEntity;
import net.beans.java.example.microservice.simple.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class GenericRepository<T extends AbstractEntity, R extends JpaRepository<T, Long>> {

    protected final R jpaRepository;

    protected final Class<T> type;

    public Optional<T> findById(Long id) {
        return jpaRepository.findById(id);
    }

    public T getById(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException.entityNotFoundException(type, id));
    }

}
