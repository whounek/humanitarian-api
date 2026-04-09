package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.*;
import com.mchs.humanitarianapi.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CalculationService {

    private final StandardRepository standardRepository;
    private final CalculationRepository calculationRepository;
    private final CalculationItemRepository calculationItemRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final DisasterTypeRepository disasterTypeRepository;
    private final ShelterRepository shelterRepository;
    private final OsmGeoService osmGeoService;
    private final RegionRepository regionRepository; // Подключили справочник регионов!

    public CalculationService(StandardRepository standardRepository,
                              CalculationRepository calculationRepository,
                              CalculationItemRepository calculationItemRepository,
                              StatusRepository statusRepository,
                              UserRepository userRepository,
                              DisasterTypeRepository disasterTypeRepository,
                              ShelterRepository shelterRepository,
                              OsmGeoService osmGeoService,
                              RegionRepository regionRepository) {
        this.standardRepository = standardRepository;
        this.calculationRepository = calculationRepository;
        this.calculationItemRepository = calculationItemRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.disasterTypeRepository = disasterTypeRepository;
        this.shelterRepository = shelterRepository;
        this.osmGeoService = osmGeoService;
        this.regionRepository = regionRepository;
    }

    public Map<String, Object> previewCalculation(Long disasterTypeId, Integer affectedPeople, Integer days,
                                                  BigDecimal climateCoefficient, BigDecimal truckCapacity) {
        List<Standard> standards = standardRepository.findByDisasterTypeId(disasterTypeId);

        Map<String, Object> finalResponse = new HashMap<>();
        Map<String, BigDecimal> resourcesNeeded = new HashMap<>();

        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal n = BigDecimal.valueOf(affectedPeople);
        BigDecimal t = BigDecimal.valueOf(days);
        BigDecimal k = (climateCoefficient != null) ? climateCoefficient : BigDecimal.ONE;

        for (Standard standard : standards) {
            String resourceName = standard.getResource().getName();
            BigDecimal q = standard.getQuantityPerPerson();

            BigDecimal totalAmount = n.multiply(q).multiply(t).multiply(k);
            resourcesNeeded.put(resourceName, totalAmount);

            totalVolume = totalVolume.add(totalAmount);
        }

        finalResponse.put("1_resources_needed", resourcesNeeded);
        finalResponse.put("2_total_volume_units", totalVolume);

        if (truckCapacity != null && truckCapacity.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal trucksNeeded = totalVolume.divide(truckCapacity, 0, RoundingMode.CEILING);
            finalResponse.put("3_trucks_needed", trucksNeeded);
        }

        return finalResponse;
    }

    @Transactional
    public Calculation createDraft(Long disasterTypeId, Integer affectedPeople, Long authorId) {
        DisasterType disasterType = disasterTypeRepository.findById(disasterTypeId)
                .orElseThrow(() -> new RuntimeException("Тип ЧС не найден"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Status draftStatus = statusRepository.findByName("Черновик")
                .orElseThrow(() -> new RuntimeException("Статус 'Черновик' не найден"));

        // --- УМНЫЙ КЛИМАТИЧЕСКИЙ КОЭФФИЦИЕНТ ---
        BigDecimal climateCoefficient = BigDecimal.ONE; // По умолчанию 1.0
        String userRegion = author.getRegion();

        if (userRegion != null && !userRegion.isEmpty()) {
            Optional<Region> regionOpt = regionRepository.findByName(userRegion);
            if (regionOpt.isPresent()) {
                climateCoefficient = regionOpt.get().getClimateCoefficient();
            }
        }

        Calculation calculation = new Calculation();
        calculation.setDisasterType(disasterType);
        calculation.setAffectedPeople(affectedPeople);
        calculation.setAuthor(author);
        calculation.setStatus(draftStatus);
        calculation.setCreatedAt(LocalDateTime.now());

        Calculation savedCalculation = calculationRepository.save(calculation);

        List<Standard> standards = standardRepository.findByDisasterTypeId(disasterTypeId);
        BigDecimal n = BigDecimal.valueOf(affectedPeople);

        for (Standard standard : standards) {
            // УМНОЖАЕМ НА РЕГИОНАЛЬНЫЙ КОЭФФИЦИЕНТ
            BigDecimal totalQuantity = n.multiply(standard.getQuantityPerPerson()).multiply(climateCoefficient);

            CalculationItem item = new CalculationItem();
            item.setCalculation(savedCalculation);
            item.setResource(standard.getResource());
            item.setQuantity(totalQuantity);

            calculationItemRepository.save(item);
        }

        return savedCalculation;
    }

    @Transactional(readOnly = true)
    public List<Shelter> suggestShelters(Long calculationId) {
        Calculation calc = calculationRepository.findById(calculationId)
                .orElseThrow(() -> new RuntimeException("Расчет не найден"));

        Integer victimsCount = calc.getAffectedPeople();
        String emergencyRegion = calc.getAuthor().getRegion();

        if (emergencyRegion == null || emergencyRegion.isEmpty()) {
            throw new RuntimeException("У автора расчета не указан регион. Невозможно подобрать ПВР.");
        }

        List<Shelter> regionalShelters = shelterRepository.findByRegionOrderByCapacityDesc(emergencyRegion);
        List<Shelter> internetShelters = osmGeoService.findSheltersInRegion(emergencyRegion);

        List<Shelter> allAvailableShelters = new ArrayList<>(regionalShelters);
        allAvailableShelters.addAll(internetShelters);

        List<Shelter> suggestedShelters = new ArrayList<>();
        int currentCapacity = 0;

        for (Shelter shelter : allAvailableShelters) {
            suggestedShelters.add(shelter);
            currentCapacity += shelter.getCapacity();

            if (currentCapacity >= victimsCount) {
                break;
            }
        }

        if (currentCapacity < victimsCount) {
            throw new RuntimeException("КРИТИЧЕСКАЯ ОШИБКА: В регионе " + emergencyRegion +
                    " не хватает зданий даже с учетом карт OpenStreetMap! Доступно мест: " + currentCapacity + ", Пострадавших: " + victimsCount);
        }

        return suggestedShelters;
    }
}