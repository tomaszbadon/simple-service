package net.beans.java.example.microservice.simple.repository;

import net.beans.java.example.microservice.simple.data.model.jpa.Category;
import net.beans.java.example.microservice.simple.repository.jpa.CategoryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRepository extends GenericRepository<Category, CategoryJpaRepository> {

    public CategoryRepository(CategoryJpaRepository jpaRepository) {
        super(jpaRepository, Category.class);
    }

    public List<Category> findByName(String name) {
        return jpaRepository.findByName(name);
    }

    public List<Category> findSubcategoriesRecursively(String name) {
        return jpaRepository.findCategoryByNameWithSubcategories(name);
    }

}
