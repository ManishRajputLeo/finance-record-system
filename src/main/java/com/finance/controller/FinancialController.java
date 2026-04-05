package com.finance.controller;


import com.finance.dto.FinancialRecordRequest;
import com.finance.entity.FinancialRecord;
import com.finance.service.FinancialRecordService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.finance.dto.FinancialRecordResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/records")
public class FinancialController {

    private final FinancialRecordService service;

    public FinancialController(FinancialRecordService service) {
        this.service = service;
    }
    /* Create Record */

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FinancialRecordResponse create(@RequestBody FinancialRecordRequest request) {
        return service.create(request);
    }
    /* Find All Record */

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping("/all")
    public List<FinancialRecord> getAll() {
        return service.getAll();
    }

    /* Record Filter */
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping("/filter")
    public List<FinancialRecord> filter(@RequestParam String category,
                                        @RequestParam String type) {
        return service.filter(category, type);
    }

    /* Delete Record By Id */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Deleted";
    }

    /* Update a Record */

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public FinancialRecord updateRecord(
            @PathVariable Long id,
            @RequestBody FinancialRecordRequest request
    ) {
        return service.updateRecord(id, request);
    }

    @GetMapping("/dashboard/summary")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public Map<String, Double> getDashboardSummary() {
        return service.getDashboardSummary();
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public Page<FinancialRecord> getAllPaged(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return service.getAllPaged(page, size, sortBy, direction);
    }

    @GetMapping("/my-records")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public List<FinancialRecordResponse> myRecords() {
        return service.getMyRecords();
    }
}
