package com.hillel.items_exchange.dao;

import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.model.enums.AgeRange;
import com.hillel.items_exchange.model.enums.Gender;
import com.hillel.items_exchange.model.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    boolean existsAdvertisementByIdAndUser(Long id, User user);

    Iterable<Advertisement> findFirst10ByTopicIgnoreCaseContaining(String topic);

    Iterable<Advertisement> findFirst10ByAgeOrGenderOrSizeOrSeasonOrSubcategoryId(AgeRange age, Gender gender, String size, Season season, long subcategoryId);
}
