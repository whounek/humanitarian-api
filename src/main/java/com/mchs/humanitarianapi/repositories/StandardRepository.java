package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRepository extends JpaRepository<Standard, Long> {
    // Этот метод Spring Boot напишет сам под капотом!
    // Он найдет все нормативы для конкретной ЧС
    List<Standard> findByDisasterTypeId(Long disasterTypeId);
}