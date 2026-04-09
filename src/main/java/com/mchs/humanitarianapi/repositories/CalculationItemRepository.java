package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.CalculationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculationItemRepository extends JpaRepository<CalculationItem, Long> {

    // Spring сам найдет все элементы (CalculationItem), у которых calculation.id совпадает с переданным
    List<CalculationItem> findByCalculationId(Long calculationId);

    // Метод для подсчета общего количества каждого ресурса (для аналитики)
    @Query("SELECT ci.resource.name, SUM(ci.quantity) FROM CalculationItem ci GROUP BY ci.resource.name")
    List<Object[]> sumTotalResourcesNeeded();

    // Подсчет ресурсов по региону автора расчета
    @Query("SELECT ci.resource.name, SUM(ci.quantity) FROM CalculationItem ci WHERE ci.calculation.author.region = :region GROUP BY ci.resource.name")
    List<Object[]> sumTotalResourcesNeededByRegion(@org.springframework.data.repository.query.Param("region") String region);
}