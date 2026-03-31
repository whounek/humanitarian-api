package com.mchs.humanitarianapi.services;

import com.mchs.humanitarianapi.models.DisasterType;
import com.mchs.humanitarianapi.models.Resource;
import com.mchs.humanitarianapi.repositories.DisasterTypeRepository;
import com.mchs.humanitarianapi.repositories.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryService {

    private final DisasterTypeRepository disasterTypeRepository;
    private final ResourceRepository resourceRepository;

    // Явный конструктор: Spring Boot автоматически передаст сюда нужные репозитории
    public DictionaryService(DisasterTypeRepository disasterTypeRepository, ResourceRepository resourceRepository) {
        this.disasterTypeRepository = disasterTypeRepository;
        this.resourceRepository = resourceRepository;
    }

    public List<DisasterType> getAllDisasterTypes() {
        // Делаем SQL-запрос SELECT * FROM disaster_types
        return disasterTypeRepository.findAll();
    }

    public List<Resource> getAllResources() {
        // Делаем SQL-запрос SELECT * FROM resources
        return resourceRepository.findAll();
    }
}