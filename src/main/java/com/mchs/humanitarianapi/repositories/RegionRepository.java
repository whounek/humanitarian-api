package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    // Метод для поиска региона по точному совпадению имени
    Optional<Region> findByName(String name);
}