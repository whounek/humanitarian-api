package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {

    // 1. Поиск всех расчетов по региону автора (для фильтрации списка у менеджеров)
    List<Calculation> findAllByAuthorRegion(String region);

    // 2. Глобальная аналитика: группировка по статусам по ВСЕЙ стране (Только для ADMIN)
    @Query("SELECT c.status.name, COUNT(c) FROM Calculation c GROUP BY c.status.name")
    List<Object[]> countCalculationsByStatus();

    // 3. Локальная аналитика: группировка по статусам ТОЛЬКО В СВОЕМ РЕГИОНЕ (Для MANAGER)
    @Query("SELECT c.status.name, COUNT(c) FROM Calculation c WHERE c.author.region = :region GROUP BY c.status.name")
    List<Object[]> countCalculationsByStatusForRegion(@Param("region") String region);

    // Подсчет созданных расчетов в конкретном регионе
    long countByAuthorRegion(String region);
}