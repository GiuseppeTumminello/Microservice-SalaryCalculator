package com.acoustic.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalaryCalculatorData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private BigDecimal grossMonthlySalary;
}
