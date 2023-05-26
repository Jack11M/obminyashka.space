package space.obminyashka.items_exchange.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

import java.util.*;

@Repository
@Transactional
public interface AdvertisementRepository extends JpaRepository<Advertisement, UUID>, QuerydslPredicateExecutor<Advertisement> {

    boolean existsAdvertisementByIdAndUser(UUID id, User user);

    @Query("SELECT a FROM Advertisement a WHERE LOWER(a.topic) LIKE %?1% OR LOWER(a.description) LIKE %?1%")
    Page<Advertisement> search(String keyword, Pageable pageable);

    @Query("SELECT a FROM Advertisement a WHERE LOWER(a.topic) IN :topics")
    Page<Advertisement> search(@Param("topics") Set<String> topics, Pageable pageable);

    @Query("SELECT a FROM Advertisement a WHERE a.subcategory.category.id = :categoryId")
    Page<Advertisement> findAdvertisementByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    Optional<Advertisement> findAdvertisementByIdAndUserUsername(UUID id, String username);

    @Query("SELECT a from Advertisement a where " +
            "(:age is null or a.age = :age) and " +
            "(:gender is null or a.gender = :gender) and " +
            "(:size is null or a.size = :size) and" +
            "(:season is null or a.season = :season) and " +
            "(:subcategoryId is null or a.subcategory.id = :subcategoryId) and " +
            "(:categoryId is null or a.subcategory.category.id = :categoryId) and " +
            "(:locationId is null or a.location.id = :locationId)")
    Collection<Advertisement> findFirst10ByParams(@Param("age") AgeRange age,
                                                @Param("gender") Gender gender,
                                                @Param("size") String size,
                                                @Param("season") Season season,
                                                @Param("subcategoryId") Long subcategoryId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("locationId") UUID locationId);

    Collection<Advertisement> findAllByUserUsername(String username);

    @Query("SELECT a from Advertisement a where " +
            "(:id is null or a.id <> :id) and " +
            "(:subcategoryId is null or a.subcategory.id = :subcategoryId)")
    Page<Advertisement> findAllByIdNotAndSubcategoryId(@Param("id") UUID id,
                                                       @Param("subcategoryId") Long subcategoryId,
                                                       Pageable pageable);

    @Query("SELECT count(a) from Advertisement a where " +
            "(:id is null or a.id <> :id) and " +
            "(:subcategoryId is null or a.subcategory.id = :subcategoryId)")
    Long countByIdNotAndSubcategoryId(@Param("id") UUID id, @Param("subcategoryId") Long subcategoryId);

    boolean existsBySubcategoryId(long id);
}
