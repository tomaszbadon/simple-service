package net.beans.java.example.microservice.simple.repository.jpa;

import net.beans.java.example.microservice.simple.data.model.jpa.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, UUID> {

    @Query("select c from Category c left outer join fetch c.subcategories where c.name = :name")
    List<Category> findByName(@Param("name") String name);

    @Query(nativeQuery = true, value = """           
            WITH RECURSIVE subcategories (id, name, description, parent_category_id, main_category_id) AS (
            	SELECT id, name, description, parent_category_id, main_category_id FROM categories WHERE name = :name
                UNION ALL
                SELECT c.id, c.name, c.description, c.parent_category_id, c.main_category_id FROM categories c INNER JOIN subcategories ON c.parent_category_id = subcategories.id
            ) SELECT * FROM subcategories
            """)
    List<Category> findCategoryByNameWithSubcategories(@Param("name") String name);

}
