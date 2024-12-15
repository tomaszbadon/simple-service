package net.beans.java.example.microservice.simple.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ModelMapperTest {

    Converter<Address, BasicAddressDto> CONVERTER =
            new AbstractConverter<Address, BasicAddressDto>() {
                @Override
                protected BasicAddressDto convert(Address address) {
                    return new AddressDto(address.getUuid(), address.getStreet(), address.getCity());
                }
            };

    
    @Test
    void test() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.emptyTypeMap(Person.class, PersonDto.class).addMappings(mapper -> mapper.using(CONVERTER)
                .map(Person::getAddress, PersonDto::setAddress)).implicitMappings();

        Person person = new Person(
                UUID.randomUUID(), "John Doe", new Address(UUID.randomUUID(), "Palm Ave", "Gold Potato City"));
        PersonDto personDto = modelMapper.map(person, PersonDto.class);

        assertThat(personDto.getAddress()).isInstanceOf(AddressDto.class);
    }

}

@Data
@AllArgsConstructor
class Person {
    private UUID uuid;
    private String name;
    private Address address;
}

@Data
@AllArgsConstructor
class Address {
    private UUID uuid;
    private String street;
    private String city;
}

@Getter
@Setter
class PersonDto {
    private UUID uuid;
    private String name;
    private BasicAddressDto address;
}

@Getter
@Setter
class AddressDto extends BasicAddressDto {
    private String street;
    private String city;

    public AddressDto(UUID uuid, String street, String city) {
        super(uuid);
        this.street = street;
        this.city = city;
    }
}

@Getter
@Setter
@AllArgsConstructor
class BasicAddressDto {
    private UUID uuid;
}