package com.acoustic.controller;


import com.acoustic.enpoints.MicroservicesEndpoints;
import com.acoustic.entity.SalaryCalculatorData;
import com.acoustic.jobcategories.JobCategoriesConfigurationProperties;
import com.acoustic.model.Model;
import com.acoustic.repository.SalaryCalculatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/salaryCalculator")
@RequiredArgsConstructor
public class SalaryCalculatorController {

    private static final int MINIMUM_GROSS = 2000;
    private final MicroservicesEndpoints microservicesEndpoints;
    private final JobCategoriesConfigurationProperties jobCategoriesConfigurationProperties;
    private final RestTemplate restTemplate;


    private final SalaryCalculatorRepository salaryCalculatorRepository;

    @PostMapping("/getEndpoints/{grossMonthlySalary}")
    public Map<String, BigDecimal> getCalculationFromMicroservices(@PathVariable @Min(value = MINIMUM_GROSS, message = "Must be Greater than or equal to 2000.00") @NotNull BigDecimal grossMonthlySalary, @RequestParam(required = false) String departmentName, @RequestParam(required = false) Integer jobTitleId)
    {   Map<String, BigDecimal> response = new HashMap<>();
        for (var endpoint : microservicesEndpoints.getEndpoints()) {
            var responseFromEndpoints = this.restTemplate.postForEntity(endpoint + 6000, HttpMethod.POST, Model.class);
            response.put(Objects.requireNonNull(responseFromEndpoints.getBody()).getDescription(), responseFromEndpoints.getBody().getValue());
        }
        if (departmentName == null || jobTitleId == null) {
            return response;
        }

        return getAverage(grossMonthlySalary, departmentName, jobTitleId, response);
    }



    private Map<String, BigDecimal> getAverage(
            BigDecimal grossMonthlySalary, String departmentName, int jobTitleId, Map<String, BigDecimal> response) {
        if (this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles()
                .containsKey(departmentName.toLowerCase())) {
            BigDecimal average = statistic(departmentName, jobTitleId, grossMonthlySalary);
            if (average != null) {
                response.put("Average", average.setScale(2, RoundingMode.HALF_EVEN));
                return response;
            }
        }
        throw new IllegalArgumentException("Invalid department name");
    }


    public BigDecimal statistic(String departmentName, int jobTitleId, BigDecimal grossMonthlySalary) {
        List<String> jobTitlesList = this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().get(departmentName);
        if (jobTitleId <= jobTitlesList.size() && jobTitleId >= 1) {
            this.salaryCalculatorRepository.save(SalaryCalculatorData.builder()
                    .grossMonthlySalary(grossMonthlySalary)
                    .jobTitle(jobTitlesList.get(jobTitleId - 1))
                    .build());
            return this.salaryCalculatorRepository.findAverageByJobTitle(jobTitlesList.get(jobTitleId - 1));

        }
        throw new IllegalArgumentException("Wrong job id");

    }


}
