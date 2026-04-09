package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}