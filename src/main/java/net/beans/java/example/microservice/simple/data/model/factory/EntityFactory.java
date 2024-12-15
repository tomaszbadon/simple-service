package net.beans.java.example.microservice.simple.data.model.factory;

import net.beans.java.example.microservice.simple.data.model.jpa.AbstractEntity;

public interface EntityFactory {

    <T extends AbstractEntity> T create(Class<T> type);

    <T extends AbstractEntity, D> T create(Class<T> type, D dto);

}
