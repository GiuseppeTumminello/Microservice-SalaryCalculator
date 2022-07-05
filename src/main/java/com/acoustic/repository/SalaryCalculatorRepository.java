package com.acoustic.repository;


import com.acoustic.entity.SalaryCalculatorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface SalaryCalculatorRepository extends JpaRepository<SalaryCalculatorData, Integer> {

    @Query(value = "select avg(gross_monthly_salary) from salary_calculator_data where job_title=:jobTitle",
            nativeQuery = true)
    BigDecimal findAverageByJobTitle(
            @Param("jobTitle")
            String jobTitle);


}
