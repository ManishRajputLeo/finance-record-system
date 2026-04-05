package com.finance.serviceImpl;

import com.finance.dto.FinancialRecordRequest;
import com.finance.entity.FinancialRecord;
import com.finance.entity.RecordType;
import com.finance.repository.FinancialRecordRepository;
import com.finance.service.FinancialRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.finance.dto.FinancialRecordResponse;
import org.modelmapper.ModelMapper;
import com.finance.repository.UserRepository;
import com.finance.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordRepository repo;

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    public FinancialRecordServiceImpl(FinancialRecordRepository repo, UserRepository userRepository, ModelMapper mapper) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }


    @Override
    public FinancialRecordResponse create(FinancialRecordRequest req) {
        FinancialRecord record = mapper.map(req, FinancialRecord.class);
        record.setCreatedBy(getLoggedInUserId());
        FinancialRecord saved = repo.save(record);
        return mapToResponse(saved);
    }

    @Override
    public List<FinancialRecord> getAll() {
        return repo.findAll();
    }

    @Override
    public List<FinancialRecord> filter(String category, String type) {
        return repo.findByCategoryAndType(category, RecordType.valueOf(type.toUpperCase()));
    }

    @Override
    public FinancialRecord updateRecord(Long id, FinancialRecordRequest request) {

        FinancialRecord record = repo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Record not found with id: " + id));

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());
        return repo.save(record);

    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Map<String, Double> getDashboardSummary() {
        Double income = repo.getTotalIncome();
        Double expense = repo.getTotalExpense();

        // avoid null
        income = (income == null) ? 0.0 : income;
        expense = (expense == null) ? 0.0 : expense;

        Double net = income - expense;

        Map<String, Double> result = new HashMap<>();
        result.put("totalIncome", income);
        result.put("totalExpense", expense);
        result.put("netBalance", net);

        return result;
    }

    @Override
    public Page<FinancialRecord> getAllPaged(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repo.findAll(pageable);
    }
    private FinancialRecordResponse mapToResponse(FinancialRecord record) {
        return mapper.map(record, FinancialRecordResponse.class);
    }

    private Long getLoggedInUserId() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }

    @Override
    public List<FinancialRecordResponse> getMyRecords() {

        Long userId = getLoggedInUserId();

        return repo.findByCreatedBy(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

}
