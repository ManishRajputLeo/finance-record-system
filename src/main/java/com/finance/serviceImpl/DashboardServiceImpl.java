package com.finance.serviceImpl;

import com.finance.entity.RecordType;
import com.finance.repository.FinancialRecordRepository;
import com.finance.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final FinancialRecordRepository repo;

    public DashboardServiceImpl(FinancialRecordRepository repo) {
        this.repo = repo;
    }

    @Override
    public Map<String, Object> getSummary() {

        Double income = repo.sumByType(RecordType.INCOME);
        Double expense = repo.sumByType(RecordType.EXPENSE);

        double totalIncome = income != null ? income : 0;
        double totalExpense = expense != null ? expense : 0;

        Map<String, Object> map = new HashMap<>();
        map.put("totalIncome", totalIncome);
        map.put("totalExpense", totalExpense);
        map.put("netBalance", totalIncome - totalExpense);

        return map;
    }
}
