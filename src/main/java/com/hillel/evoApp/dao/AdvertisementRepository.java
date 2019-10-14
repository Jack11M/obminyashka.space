package com.hillel.evoApp.dao;

import com.hillel.evoApp.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    @Query(value ="SELECT name FROM categories", nativeQuery=true)
    List<String> findAllCategories();
}
