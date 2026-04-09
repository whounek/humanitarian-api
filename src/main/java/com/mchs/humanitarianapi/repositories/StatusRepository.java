package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(String name); // Ищем статус по его текстовому названию
}