package net.beans.java.example.microservice.simple.data.model.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.data.model.jpa.AbstractEntity;
import net.beans.java.example.microservice.simple.exception.ApplicationInternalException;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static java.text.MessageFormat.format;


@Slf4j
@Component
@RequiredArgsConstructor
public class GenericEntityFactory implements EntityFactory {

    private final ModelMapper modelMapper;

    @Override
    public <T extends AbstractEntity> T create(Class<T> type) {
        try {
            T abstractEntity = type.getConstructor().newInstance();
            abstractEntity.setId(UUID.randomUUID());
            log.info("New instance of: '{}' has been  created with id: '{}'", type.getName(), abstractEntity.getId());
            return abstractEntity;
        } catch (Exception e) {
            throw new ApplicationInternalException(format("Creation of new instance: ''{0}'' has failed", type.getName()), e);
        }
    }

    @Override
    public <T extends AbstractEntity, D> T create(Class<T> type, D object) {
        T instance = create(type);
        modelMapper.map(object, instance);
        return instance;
    }
}
