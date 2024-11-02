package net.beans.java.example.microservice.simple.exception;

import net.beans.java.example.microservice.simple.data.model.jpa.AbstractEntity;

import java.util.UUID;
import java.util.function.Supplier;

import static java.text.MessageFormat.format;


public class EntityNotFoundException extends RuntimeException {
    private final static String CANNOT_FIND_OBJECT = "Cannot find Object: ''{0}'' by ''{1}'': ''{2}''";
    private final static String ID = "id";
    private final Class<? extends AbstractEntity> entity;
    private final UUID id;

    public EntityNotFoundException(Class<? extends AbstractEntity> entity, UUID id) {
        super(format(CANNOT_FIND_OBJECT, entity.getName(), ID, id));
        this.entity = entity;
        this.id = id;
    }

    public static Supplier<EntityNotFoundException> entityNotFoundException(Class<? extends AbstractEntity> entity, UUID id) {
        return () -> new EntityNotFoundException(entity, id);
    }

}
