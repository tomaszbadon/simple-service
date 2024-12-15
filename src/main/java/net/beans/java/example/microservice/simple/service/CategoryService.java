package net.beans.java.example.microservice.simple.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.data.model.dto.category.CategoriesInfo;
import net.beans.java.example.microservice.simple.data.model.dto.category.CategoryInfo;
import net.beans.java.example.microservice.simple.data.model.factory.GenericEntityFactory;
import net.beans.java.example.microservice.simple.data.model.jpa.Category;
import net.beans.java.example.microservice.simple.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final GenericEntityFactory factory;

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category create(CategoryInfo info) {
        Category category = factory.create(Category.class, info);
        return categoryRepository.save(category);
    }

}
