package com.mchs.humanitarianapi.repositories;

import com.mchs.humanitarianapi.models.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {
    List<ApprovalHistory> findByCalculationId(Long calculationId); // Чтобы потом выводить историю конкретного документа
}