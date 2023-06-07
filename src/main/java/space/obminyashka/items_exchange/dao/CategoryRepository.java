package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT name FROM category", nativeQuery = true)
    List<String> findAllCategoriesNames();

    boolean existsByNameIgnoreCase(String name);

    boolean existsByIdAndNameIgnoreCase(long categoryId, String categoryName);

    boolean existsBySubcategoriesAdvertisementsEmptyAndId(long categoryId);
}
