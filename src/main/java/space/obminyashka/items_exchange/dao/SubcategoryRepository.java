package space.obminyashka.items_exchange.dao;

import space.obminyashka.items_exchange.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    @Query(value = "SELECT name FROM subcategory WHERE category_id = ?", nativeQuery = true)
    List<String> findSubcategoriesNamesByCategory(Long categoryId);

    @Query(value = "select s.id from subcategory s join category c on c.id = s.category_id " +
            "where c.id = :categoryId and s.id in :subcategoriesId", nativeQuery = true)
    List<Long> findExistingIdForCategoryById(Long categoryId, List<Long> subcategoriesId);

    Optional<Subcategory> findById(long id);
}
