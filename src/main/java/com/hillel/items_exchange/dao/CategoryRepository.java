package com.hillel.items_exchange.dao;

import com.hillel.items_exchange.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT name FROM category", nativeQuery = true)
    List<String> findAllCategoriesNames();
    Category findCategoryById(long id);
    boolean existsByName(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByIdAndNameIgnoreCase(long categoryId, String categoryName);

    @Query(value = "SELECT IF((SELECT COUNT(*) FROM category " +
            "           WHERE (id = :categoryId AND upper(name) = :categoryName)) > 0" +
            "               OR" +
            "                   (SELECT EXISTS(SELECT * FROM category WHERE id = :categoryId)" +
            "                       AND" +
            "                   (SELECT NOT EXISTS(SELECT * FROM category WHERE upper(name) LIKE :categoryName))" +
            "                       AND" +
            "                   :categoryName <> ''), 'true', 'false')", nativeQuery = true)
    boolean isExistsByIdAndNameOrNewCategoryNameHasNotDuplicate(@Param("categoryId") long categoryId,
                                                                   @Param("categoryName") String categoryName);
}
