package com.hillel.items_exchange.dao;

import com.hillel.items_exchange.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT i.resource_url FROM image i, advertisement a WHERE a.id = ?1 AND a.product_id = ?2 AND i.product_id = a.product_id", nativeQuery = true)
    List<String> findImageUrlsByAdvertisementIdAndProductId(Long advId, Long productId);

    @Query(value = "SELECT i.* FROM image i, advertisement a WHERE a.id = ?1 AND a.product_id = ?2 AND i.product_id = a.product_id", nativeQuery = true)
    List<Image> findByAdvertisementIdAndProductId(Long advId, Long productId);
}