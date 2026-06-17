package com.barboza.finance_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barboza.finance_api.dto.summary.SummaryResponse;
import com.barboza.finance_api.service.SummaryService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Validated
@RequestMapping("/summary")
public class SummaryController {
    
    private final SummaryService service;

    public SummaryController(SummaryService service) {
        this.service = service;
    }
    
    @GetMapping("/annual")
    public ResponseEntity<List<SummaryResponse>> getSummaryByYear(
        @AuthenticationPrincipal String email,
        @RequestParam(required = false) @Min(2000) @Max(2100) Integer year) {
    
        var resolvedYear = year != null ? year : LocalDate.now().getYear();
        return ResponseEntity.ok(service.getSummaryYear(email, resolvedYear));
    }

    @GetMapping
    public ResponseEntity<SummaryResponse> getSummaryByMonthAndYear(
        @AuthenticationPrincipal String email,
        @RequestParam(required = false) @Min(1) @Max(12) Integer month,
        @RequestParam(required = false) @Min(2000) @Max(2100) Integer year) {
        
        var now = LocalDate.now();

        var resolvedMonth = month != null ? month : now.getMonthValue();
        var resolvedYear = year != null ? year : now.getYear();

        return ResponseEntity.ok(service.getSummaryByMonth(email, resolvedMonth, resolvedYear));
    }
}
