package core.statistics.domain.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import core.statistics.domain.entity.TransactionHistoryEntity;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, String> {
	List<TransactionHistoryEntity> findByCreateDateBetween(Date start,Date end);   
	List<TransactionHistoryEntity> findByPaymentDateBetween(LocalDateTime start,LocalDateTime end);
	List<TransactionHistoryEntity> findByPaymentDateBefore(LocalDateTime end);
}
