package com.finance.repository;

import com.finance.entity.FinancialRecord;
import com.finance.entity.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    List<FinancialRecord> findByCategoryAndType(String category, RecordType type);

    @Query("SELECT SUM(f.amount) FROM FinancialRecord f WHERE f.type = :type")
    Double sumByType(RecordType type);

    Page<FinancialRecord> findByType(RecordType type, Pageable pageable);

    Page<FinancialRecord> findByCategory(String category,Pageable pageable);

    @Query("SELECT SUM(f.amount) FROM FinancialRecord f WHERE f.type = 'INCOME'")
    Double getTotalIncome();

    @Query("SELECT SUM(f.amount) FROM FinancialRecord f WHERE f.type = 'EXPENSE'")
    Double getTotalExpense();

    List<FinancialRecord> findByCreatedBy(Long createdBy);

}
