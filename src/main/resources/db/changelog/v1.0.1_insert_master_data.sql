-- liquibase formatted sql
-- changeset admin:insert-master-data

-- 1. Статусы расчетов
INSERT INTO statuses (name) VALUES
('Черновик'),
('На согласовании'),
('Утверждено'),
('Отклонено');

-- 2. Климатические регионы
INSERT INTO regions (name, climate_coefficient) VALUES
('Москва', 1.00),
('Амурская область', 1.50),
('Краснодарский край', 0.80),
('Республика Саха (Якутия)', 2.00);

-- 3. Базовые типы ЧС
INSERT INTO disaster_types (name) VALUES
('Наводнение'),
('Лесной пожар'),
('Землетрясение');

-- 4. Базовые ресурсы
INSERT INTO resources (name) VALUES
('Вода питьевая (бутылка 1л)'),
('Сухой паек (ИРП)'),
('Одеяло теплое'),
('Аптечка первой помощи');