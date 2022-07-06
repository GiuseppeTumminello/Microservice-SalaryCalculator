package com.acoustic.SpringPolandSalaryCalculator.controller;


import com.acoustic.SpringPolandSalaryCalculator.calculator.SalaryCalculatorTest;
import com.acoustic.repository.DataSalaryCalculatorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SalaryCalculatorControllerPostTest {


    public static final String CALCULATOR_ENDPOINTS = "/salaryCalculator/calculator/";
    public static final String DEPARTMENT_NAME_REQUEST_PARAM = "?departmentName=";
    public static final String JOB_TITLE_ID_REQUEST_PARAM = "&jobTitleId=";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SalaryCalculatorTest salaryCalculatorTest;

    @MockBean
    private DataSalaryCalculatorRepository dataSalaryCalculatorRepository;


    @ParameterizedTest
    @CsvSource({"6000, finance, 1, true",
            "7000, it, 2, true",
            "15891.68, airline, 1, true",
            "7700, restaurant, 6, true",
            "12191.68, it, 10, true",
            "185891.68, finance, 2, true"})
    public void getSalaryCalculation(
            BigDecimal grossMonthlySalary, String departmentName, int jobTitleId, boolean average) throws Exception {
        given(dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        this.mockMvc.perform(post(
                        CALCULATOR_ENDPOINTS + grossMonthlySalary + DEPARTMENT_NAME_REQUEST_PARAM + departmentName +
                                JOB_TITLE_ID_REQUEST_PARAM + jobTitleId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(objectMapper.writeValueAsString(salaryCalculatorTest.expectedValue(grossMonthlySalary,
                                average))));

    }

    @ParameterizedTest
    @CsvSource({"6000, finance, -10, true",
            "7000, it, -1000, true",
            "15891.68, airline, 11, true",
            "7700, restaurant, 20, true",
            "12191.68, it, 30, true",
            "185891.68, finance, 40, true"})
    public void getSalaryCalculationIdOutOfBounds(
            BigDecimal grossMonthlySalary, String departmentName, int jobTitleId, boolean average) {
        given(dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        Assertions.assertThrows(NestedServletException.class,
                () -> this.mockMvc.perform(post(
                                CALCULATOR_ENDPOINTS + grossMonthlySalary + DEPARTMENT_NAME_REQUEST_PARAM + departmentName +
                                        JOB_TITLE_ID_REQUEST_PARAM + jobTitleId))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content()
                                .string(this.objectMapper.writeValueAsString(salaryCalculatorTest.expectedValue(
                                        grossMonthlySalary,
                                        average)))));

    }


    @ParameterizedTest
    @CsvSource({"6000, fff, 1, true",
            "7000, its, 1, true",
            "15891.68, airlines, 1, true",
            "7700, restaurants, 1, true",
            "12191.68, rest, 2, true",
            "185891.68, finances, 3, true"})
    public void getSalaryCalculationWrongDepartmentName(
            BigDecimal grossMonthlySalary, String departmentName, int jobTitleId, boolean average) {
        given(dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        Assertions.assertThrows(NestedServletException.class,
                () -> this.mockMvc.perform(post(
                                CALCULATOR_ENDPOINTS + grossMonthlySalary + DEPARTMENT_NAME_REQUEST_PARAM + departmentName +
                                        JOB_TITLE_ID_REQUEST_PARAM + jobTitleId))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content()
                                .string(this.objectMapper.writeValueAsString(salaryCalculatorTest.expectedValue(
                                        grossMonthlySalary,
                                        average)))));
    }


    @ParameterizedTest
    @CsvSource({"-6000, finance, 1, true",
            "-7000, it, 1, true",
            "-15891.68, airline, 1, true",
            "-7700, restaurant, 1, true",
            "1999.9999, it, 2, true",
            "0, finance, 3, true"})
    public void getSalaryCalculationGrossBelowTrashHold(
            BigDecimal grossMonthlySalary, String departmentName, int jobTitleId, boolean average) {
        given(dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        Assertions.assertThrows(NestedServletException.class,
                () -> this.mockMvc.perform(post(
                                CALCULATOR_ENDPOINTS + grossMonthlySalary + DEPARTMENT_NAME_REQUEST_PARAM + departmentName +
                                        JOB_TITLE_ID_REQUEST_PARAM + jobTitleId))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content()
                                .string(this.objectMapper.writeValueAsString(salaryCalculatorTest.expectedValue(
                                        grossMonthlySalary,
                                        average)))));
    }


    @ParameterizedTest
    @CsvSource({"6000, false", "7000, false", "15891.68, false", "7700, false", "2999.9999, false"})

    public void getSalaryCalculationGrossNoStatistic(BigDecimal grossMonthlySalary, boolean average) throws Exception {
        given(dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        this.mockMvc.perform(post(CALCULATOR_ENDPOINTS + grossMonthlySalary))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(objectMapper.writeValueAsString(salaryCalculatorTest.expectedValue(grossMonthlySalary,
                                average))));

    }

}
