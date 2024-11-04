package net.beans.java.example.microservice.simple.data.model.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInfo {

    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private SubcategoryInfo mainCategory;

    private SubcategoryInfo parentCategory;

    private Set<SubcategoryInfo> subcategories;

}
