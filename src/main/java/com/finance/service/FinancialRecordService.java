package com.finance.service;

import com.finance.dto.FinancialRecordRequest;
import com.finance.dto.FinancialRecordResponse;
import com.finance.entity.FinancialRecord;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface FinancialRecordService {

    FinancialRecordResponse create(FinancialRecordRequest request);

    List<FinancialRecord> getAll();

    List<FinancialRecord> filter(String category, String type);

    FinancialRecord updateRecord(Long id, FinancialRecordRequest request);

    void delete(Long id);

    Map<String, Double> getDashboardSummary();

    Page<FinancialRecord> getAllPaged(int page, int size, String sortBy, String direction);

    List<FinancialRecordResponse> getMyRecords();




}