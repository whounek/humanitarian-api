package com.mchs.humanitarianapi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchs.humanitarianapi.models.Shelter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OsmGeoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OsmGeoService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    // Говорим Спрингу: "Складывай результаты в память, а ключом поиска делай название региона"
    @Cacheable(value = "shelters", key = "#region")
    public List<Shelter> findSheltersInRegion(String region) {

        // Эта строка появится в консоли ТОЛЬКО при первом (реальном) запросе в интернет
        System.out.println("======> [OSM API] Идем в интернет за данными для региона: " + region + " <======");

        List<Shelter> foundShelters = new ArrayList<>();

        String[] queries = {"школа " + region, "гостиница " + region};

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "MchsDisasterReliefApp/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        for (String query : queries) {
            String url = "https://nominatim.openstreetmap.org/search?q=" + query + "&format=json&limit=3";

            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                for (JsonNode node : rootNode) {
                    String name = node.has("name") && !node.get("name").asText().isEmpty() ?
                            node.get("name").asText() : "Здание ПВР";
                    String address = node.get("display_name").asText();
                    String type = node.get("type").asText();

                    Shelter shelter = new Shelter();
                    shelter.setName(name + " (OpenStreetMap)");
                    shelter.setAddress(address);
                    shelter.setRegion(region);

                    // Эвристика вместимости
                    if (type.contains("school") || type.contains("college")) {
                        shelter.setCapacity(300);
                    } else if (type.contains("hotel") || type.contains("hostel") || type.contains("guest_house")) {
                        shelter.setCapacity(150);
                    } else {
                        shelter.setCapacity(100);
                    }

                    foundShelters.add(shelter);
                }

                // Защита от блокировки со стороны OSM
                Thread.sleep(1000);

            } catch (Exception e) {
                System.err.println("[ОШИБКА OSM API] Не удалось получить данные по запросу '" + query + "': " + e.getMessage());
            }
        }

        return foundShelters;
    }
}