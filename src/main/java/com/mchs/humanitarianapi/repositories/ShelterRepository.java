package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    // Находит ПВР по региону и сортирует от самых больших к маленьким
    List<Shelter> findByRegionOrderByCapacityDesc(String region);
}