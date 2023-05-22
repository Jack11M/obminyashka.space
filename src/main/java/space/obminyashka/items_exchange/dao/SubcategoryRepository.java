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

    Optional<Subcategory> findById(long id);
}
