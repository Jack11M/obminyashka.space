package com.hillel.items_exchange.dao;

import com.hillel.items_exchange.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    @Query(value = "SELECT name FROM subcategory WHERE category_id = ?", nativeQuery = true)
    List<String> findSubcategoriesNamesByCategory(Long categoryId);

    Subcategory findById(long id);
}
