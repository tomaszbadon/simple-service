package net.beans.java.example.microservice.simple.controller.rest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.beans.java.example.microservice.simple.data.model.dto.category.CategoriesInfo;
import net.beans.java.example.microservice.simple.data.model.dto.category.CategoryInfo;
import net.beans.java.example.microservice.simple.data.model.jpa.Category;
import net.beans.java.example.microservice.simple.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping({"/v1/categories", "/v1/categories/"})
public class CategoryResource {

    private final CategoryService categoryService;

    private final ModelMapper modelMapper;

    @GetMapping
    @Transactional
    public ResponseEntity<CategoriesInfo> getAllCategories() {
        CategoriesInfo categoriesInfo = categoryService.getAllCategories().stream().map(category -> modelMapper.map(category, CategoryInfo.class)).collect(
                CategoriesInfo::new,
                (categoriesInf, category) -> categoriesInf.getCategories().add(category),
                (categoriesInfo1, categoriesInfo2) -> categoriesInfo1.getCategories().addAll(categoriesInfo2.getCategories())
        );
        return ResponseEntity.ok(categoriesInfo);
    }

    @PostMapping
    public ResponseEntity<CategoryInfo> createCategory(UriComponentsBuilder builder, @Validated @RequestBody CategoryInfo categoryInfo) {
        Category category = categoryService.create(categoryInfo);
        UriComponents uriComponents = builder.path("/v1/categories/{id}").buildAndExpand(category.getId());
        URI uri2 = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(category.getId())
                .toUri();
        return ResponseEntity.created(uriComponents.toUri()).body(category.toDto(modelMapper, CategoryInfo.class));
    }

}
