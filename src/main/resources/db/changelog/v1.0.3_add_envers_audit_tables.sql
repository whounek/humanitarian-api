-- liquibase formatted sql
-- changeset admin:add-envers-audit-tables

-- Создаем последовательность и базовую таблицу ревизий для Envers
CREATE SEQUENCE IF NOT EXISTS revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE revinfo (
    rev INTEGER PRIMARY KEY,
    revtstmp BIGINT
);

-- Создаем таблицу для истории расчетов
CREATE TABLE calculations_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    author_id BIGINT,
    status_id BIGINT,
    disaster_type_id BIGINT,
    affected_people INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    manager_comment VARCHAR(255),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_calculations_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

-- Создаем таблицу для истории позиций расчетов
CREATE TABLE calculation_items_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    calculation_id BIGINT,
    resource_id BIGINT,
    quantity NUMERIC(10, 2),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_calculation_items_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);