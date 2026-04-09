package com.mchs.humanitarianapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

// Отключаем дефолтного юзера, но оставляем всё остальное
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableScheduling // Поддержка фоновых задач
@EnableCaching    // Кэширование (OsmGeoService)
@EnableRetry      // Автоматические повторы при ошибках
@EnableAsync      // Поддержка асинхронных потоков
public class HumanitarianApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumanitarianApiApplication.class, args);
    }

}