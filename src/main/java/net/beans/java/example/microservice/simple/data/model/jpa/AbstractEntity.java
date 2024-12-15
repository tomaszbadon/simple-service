package net.beans.java.example.microservice.simple.data.model.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @EqualsAndHashCode.Include
    protected UUID id;

    public <D> D toDto(ModelMapper modelMapper, Class<D> dto) {
        return modelMapper.map(this, dto);
    }

}
