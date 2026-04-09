package com.mchs.humanitarianapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchs.humanitarianapi.models.Standard;
import com.mchs.humanitarianapi.repositories.StandardRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class StandardSyncService {

    private final StandardRepository standardRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String EXTERNAL_API_URL = "https://gist.githubusercontent.com/whounek/458171771dcf266aaf0a2e5b624c7937/raw/un-norms.json";

    public StandardSyncService(StandardRepository standardRepository) {
        this.standardRepository = standardRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    // БРОНЯ: При сбое (Exception) пробуем 3 раза, ждем 2000 мс между попытками
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public String syncWithGlobalStandards() {
        try {
            String rawJsonText = restTemplate.getForObject(EXTERNAL_API_URL, String.class);

            if (rawJsonText == null || rawJsonText.isEmpty()) {
                throw new RuntimeException("Внешний сервер не вернул данные.");
            }

            Map<String, Object> response = objectMapper.readValue(rawJsonText, Map.class);

            BigDecimal newWaterNorm = new BigDecimal(response.getOrDefault("water_norm_liters", "3.0").toString());
            BigDecimal newFoodNorm = new BigDecimal(response.getOrDefault("food_norm_pcs", "1.0").toString());
            BigDecimal newBlanketNorm = new BigDecimal(response.getOrDefault("blanket_norm_pcs", "1.0").toString());
            BigDecimal newMedicineNorm = new BigDecimal(response.getOrDefault("medicine_kit_norm_pcs", "0.05").toString());

            List<Standard> standards = standardRepository.findAll();

            for (Standard s : standards) {
                // Используем надежные системные коды, которые мы внедрили ранее
                String code = s.getResource().getSystemCode();

                if (code == null) continue;

                switch (code) {
                    case "WATER":
                        s.setQuantityPerPerson(newWaterNorm);
                        break;
                    case "FOOD":
                        s.setQuantityPerPerson(newFoodNorm);
                        break;
                    case "BLANKET":
                        s.setQuantityPerPerson(newBlanketNorm);
                        break;
                    case "MEDICINE":
                        s.setQuantityPerPerson(newMedicineNorm);
                        break;
                    default:
                        break;
                }
            }

            standardRepository.saveAll(standards);

            return "УСПЕХ: Синхронизировано. Расширенные нормативы обновлены.";

        } catch (Exception e) {
            System.err.println("Сбой при синхронизации (инициируем повтор): " + e.getMessage());
            throw new RuntimeException("Сбой API синхронизации: " + e.getMessage());
        }
    }
}