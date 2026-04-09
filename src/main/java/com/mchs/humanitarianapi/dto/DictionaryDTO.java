package com.mchs.humanitarianapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DictionaryDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DisasterTypeDTO {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResourceDTO {
        private Long id;
        private String name;
        private Long categoryId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShelterDTO {
        private Long id;
        private String name;
        private Integer capacity;
    }
}