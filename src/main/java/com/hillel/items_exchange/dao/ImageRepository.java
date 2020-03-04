package com.hillel.items_exchange.dao;

import com.hillel.items_exchange.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT resource_url FROM image WHERE  product_id = ?1", nativeQuery = true)
    List<String> findImageUrlsByProductId(Long id);

    @Query(value = "SELECT id, product_id, resource_url, default_photo FROM image WHERE  product_id = ?1", nativeQuery = true)
    List<Image> findImagesByProductId(Long id);
}