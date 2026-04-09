-- liquibase formatted sql
-- changeset admin:add-resource-system-codes

-- 1. Добавляем колонку (пока разрешаем NULL, чтобы не сломать существующие данные)
ALTER TABLE public.resources ADD COLUMN system_code VARCHAR(50);

-- 2. Привязываем жесткие системные коды к нашим существующим базовым ресурсам
UPDATE public.resources SET system_code = 'WATER' WHERE name LIKE '%Вода%';
UPDATE public.resources SET system_code = 'FOOD' WHERE name LIKE '%Сухой паек%';
UPDATE public.resources SET system_code = 'BLANKET' WHERE name LIKE '%Одеяло%';
UPDATE public.resources SET system_code = 'MEDICINE' WHERE name LIKE '%Аптечка%';

-- 3. Теперь, когда пустых значений нет, делаем колонку обязательной и уникальной
ALTER TABLE public.resources ALTER COLUMN system_code SET NOT NULL;
ALTER TABLE public.resources ADD CONSTRAINT uk_resources_system_code UNIQUE (system_code);