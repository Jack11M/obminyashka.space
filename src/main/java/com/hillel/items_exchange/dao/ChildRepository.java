package com.hillel.items_exchange.dao;

import com.hillel.items_exchange.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {
}
