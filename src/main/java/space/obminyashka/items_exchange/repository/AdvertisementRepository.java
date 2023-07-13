package space.obminyashka.items_exchange.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.obminyashka.items_exchange.repository.model.Advertisement;
import space.obminyashka.items_exchange.repository.model.User;
import space.obminyashka.items_exchange.repository.projection.AdvertisementTitleProjection;

import java.util.*;

@Repository
@Transactional
public interface AdvertisementRepository extends JpaRepository<Advertisement, UUID>, QuerydslPredicateExecutor<Advertisement> {

    boolean existsAdvertisementByIdAndUser(UUID id, User user);

    @Query("SELECT a FROM Advertisement a WHERE LOWER(a.topic) LIKE %?1% OR LOWER(a.description) LIKE %?1%")
    Page<Advertisement> search(String keyword, Pageable pageable);

    @Query("SELECT a FROM Advertisement a WHERE LOWER(a.topic) IN :topics")
    Page<Advertisement> search(@Param("topics") Set<String> topics, Pageable pageable);

    Optional<Advertisement> findAdvertisementByIdAndUserUsername(UUID id, String username);

    Collection<AdvertisementTitleProjection> findAllByUserUsername(String username);

    @Query("SELECT a FROM User u JOIN u.favoriteAdvertisements a WHERE u.username = :username")
    Page<AdvertisementTitleProjection> findFavoriteAdvertisementsByUsername(String username, Pageable pageable);

    int removeFavoriteAdvertisementsByIdAndUserUsername(UUID advertisementId, String username);

    @Query("SELECT COUNT(a) FROM Advertisement a WHERE " +
            "(:id IS NULL OR a.id <> :id) AND " +
            "(a.subcategory.id IN :subcategoryIds)")
    Long countByIdNotAndSubcategoryId(UUID id, List<Long> subcategoryIds);

    boolean existsBySubcategoryId(long id);
}
