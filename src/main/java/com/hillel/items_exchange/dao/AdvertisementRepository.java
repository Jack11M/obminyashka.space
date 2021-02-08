package com.hillel.items_exchange.dao;

import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.model.enums.AgeRange;
import com.hillel.items_exchange.model.enums.Gender;
import com.hillel.items_exchange.model.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    boolean existsAdvertisementByIdAndUser(Long id, User user);

    Iterable<Advertisement> findFirst10ByTopicIgnoreCaseContaining(String topic);

    @Query("SELECT a from Advertisement a where " +
            "(:age is null or a.age = :age) and " +
            "(:gender is null or a.gender = :gender) and " +
            "(:size is null or a.size = :size) and" +
            "(:season is null or a.season = :season) and " +
            "(:subcategoryId is null or a.subcategory.id = :subcategoryId) and " +
            "(:categoryId is null or a.subcategory.category.id = :categoryId) and " +
            "(:locationId is null or a.location.id = :locationId)")
    Iterable<Advertisement> findFirst10ByParams(@Param("age") AgeRange age,
                                                @Param("gender") Gender gender,
                                                @Param("size") String size,
                                                @Param("season") Season season,
                                                @Param("subcategoryId") Long subcategoryId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("locationId") Long locationId);
}
