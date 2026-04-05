package com.finance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "financial_records")
public class FinancialRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Positive
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RecordType type;
    @NotNull
    private String category;
    @NotNull
    private LocalDate date;
    @NotNull
    private String notes;
    @NotNull
    private Long createdBy;

    private Boolean isDeleted = false;
}
