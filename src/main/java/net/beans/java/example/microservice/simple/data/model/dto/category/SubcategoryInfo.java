package net.beans.java.example.microservice.simple.data.model.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryInfo {

    private UUID id;

    private String name;

    private String description;

}
