package com.finance.controller;

import com.finance.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return service.getSummary();
    }
}