package com.mchs.humanitarianapi.config;

import com.mchs.humanitarianapi.models.DisasterType;
import com.mchs.humanitarianapi.models.Resource;
import com.mchs.humanitarianapi.models.Status;
import com.mchs.humanitarianapi.repositories.DisasterTypeRepository;
import com.mchs.humanitarianapi.repositories.ResourceRepository;
import com.mchs.humanitarianapi.repositories.StandardRepository;
import com.mchs.humanitarianapi.repositories.StatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DisasterTypeRepository disasterTypeRepository;
    private final ResourceRepository resourceRepository;
    private final StandardRepository standardRepository;
    private final StatusRepository statusRepository;

    public DataInitializer(DisasterTypeRepository disasterTypeRepository,
                           ResourceRepository resourceRepository,
                           StandardRepository standardRepository,
                           StatusRepository statusRepository) {
        this.disasterTypeRepository = disasterTypeRepository;
        this.resourceRepository = resourceRepository;
        this.standardRepository = standardRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Инициализация статусов документов
        if (statusRepository.count() == 0) {
            Status draft = new Status();
            draft.setName("Черновик");

            Status pending = new Status();
            pending.setName("На проверке");

            Status approved = new Status();
            approved.setName("Согласовано");

            Status rejected = new Status();
            rejected.setName("Отклонено");

            Status inProgress = new Status();
            inProgress.setName("В работе"); // <--- Наш новый статус для финализации

            statusRepository.saveAll(List.of(draft, pending, approved, rejected, inProgress));
            System.out.println("✅ Базовые статусы документооборота загружены!");
        }

        // Проверяем, пустая ли таблица ЧС
        if (disasterTypeRepository.count() == 0) {
            DisasterType flood = new DisasterType();
            flood.setName("Наводнение");

            DisasterType fire = new DisasterType();
            fire.setName("Лесной пожар");

            DisasterType earthquake = new DisasterType();
            earthquake.setName("Землетрясение");

            disasterTypeRepository.saveAll(List.of(flood, fire, earthquake));
            System.out.println("✅ Базовые типы ЧС успешно загружены в БД!");
        }

        // Проверяем, пустая ли таблица Ресурсов
        if (resourceRepository.count() == 0) {
            Resource water = new Resource();
            water.setName("Питьевая вода (литры)");

            Resource food = new Resource();
            food.setName("Сухой паек (шт)");

            Resource tent = new Resource();
            tent.setName("Палатка зимняя (шт)");

            resourceRepository.saveAll(List.of(water, food, tent));
            System.out.println("✅ Базовые ресурсы успешно загружены в БД!");
        }

        // Загружаем тестовые нормативы, если таблица пустая
        if (standardRepository.count() == 0 && disasterTypeRepository.count() > 0 && resourceRepository.count() > 0) {
            com.mchs.humanitarianapi.models.Standard floodWaterStandard = new com.mchs.humanitarianapi.models.Standard();
            floodWaterStandard.setDisasterType(disasterTypeRepository.findAll().get(0)); // Наводнение
            floodWaterStandard.setResource(resourceRepository.findAll().get(0)); // Вода
            floodWaterStandard.setQuantityPerPerson(new java.math.BigDecimal("2.5")); // 2.5 литра на человека

            standardRepository.save(floodWaterStandard);
            System.out.println("✅ Базовые нормативы загружены!");
        }
    }
}