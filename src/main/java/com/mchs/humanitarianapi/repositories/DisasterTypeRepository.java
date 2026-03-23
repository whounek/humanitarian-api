package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.DisasterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisasterTypeRepository extends JpaRepository<DisasterType, Long> {
}