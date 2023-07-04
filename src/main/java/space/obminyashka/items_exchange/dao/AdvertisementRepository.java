package space.obminyashka.items_exchange.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.projection.AdvertisementTitleProjection;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    Collection<AdvertisementTitleProjection> findAllByUserUsername(String username);

    @Query("SELECT a from Advertisement a where " +
            "(:id is null or a.id <> :id) and " +
            "(:subcategoryId is null or a.subcategory.id = :subcategoryId)")
    Page<AdvertisementTitleProjection> findAllByIdNotAndSubcategoryId(@Param("id") UUID id,
                                                                      @Param("subcategoryId") Long subcategoryId,
                                                                      Pageable pageable);

    @Query("SELECT a FROM User u JOIN u.favoriteAdvertisements a WHERE u.username = :username")
    Page<AdvertisementTitleProjection> findFavoriteAdvertisementsByUsername(String username, Pageable pageable);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM favorite_advertisements a " +
            "WHERE a.user_id = (select id from user where username = :username) " +
            "and a.advertisement_id = :advertisementId")
    boolean deleteFavoriteAdvertisementByAdvIdAndUsername(UUID advertisementId, String username);

    @Query("SELECT count(a) from Advertisement a where " +
            "(:id is null or a.id <> :id) and " +
            "(:subcategoryId is null or a.subcategory.id = :subcategoryId)")
    Long countByIdNotAndSubcategoryId(@Param("id") UUID id, @Param("subcategoryId") Long subcategoryId);

    boolean existsBySubcategoryId(long id);
}
