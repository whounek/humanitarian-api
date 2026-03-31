package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.Standard;
import com.mchs.humanitarianapi.repositories.StandardRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculationService {

    private final StandardRepository standardRepository;

    public CalculationService(StandardRepository standardRepository) {
        this.standardRepository = standardRepository;
    }

    // Возвращаем Map<String, Object>, чтобы положить туда и ресурсы (Map), и логистику (Числа)
    public Map<String, Object> previewCalculation(Long disasterTypeId, Integer affectedPeople, Integer days, BigDecimal climateCoefficient, BigDecimal truckCapacity) {
        List<Standard> standards = standardRepository.findByDisasterTypeId(disasterTypeId);

        Map<String, Object> finalResponse = new HashMap<>();
        Map<String, BigDecimal> resourcesNeeded = new HashMap<>();

        BigDecimal totalVolume = BigDecimal.ZERO;

        BigDecimal n = BigDecimal.valueOf(affectedPeople);
        BigDecimal t = BigDecimal.valueOf(days);
        BigDecimal k = (climateCoefficient != null) ? climateCoefficient : BigDecimal.ONE;

        // 1. Считаем ресурсы по формуле Q = N * q * T * K
        for (Standard standard : standards) {
            String resourceName = standard.getResource().getName();
            BigDecimal q = standard.getQuantityPerPerson();

            BigDecimal totalAmount = n.multiply(q).multiply(t).multiply(k);
            resourcesNeeded.put(resourceName, totalAmount);

            // Плюсуем к общему объему (для прототипа считаем 1 ед. ресурса = 1 кг/л)
            totalVolume = totalVolume.add(totalAmount);
        }

        finalResponse.put("1_resources_needed", resourcesNeeded);
        finalResponse.put("2_total_volume_units", totalVolume);

        // 2. Считаем машины по формуле M = Q_total / L_capacity
        if (truckCapacity != null && truckCapacity.compareTo(BigDecimal.ZERO) > 0) {
            // Делим общий объем на вместимость машины и округляем ВВЕРХ (CEILING)
            BigDecimal trucksNeeded = totalVolume.divide(truckCapacity, 0, RoundingMode.CEILING);
            finalResponse.put("3_trucks_needed", trucksNeeded);
        }

        return finalResponse;
    }
}