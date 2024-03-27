package ru.petrov.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import ru.petrov.util.exception.DurationVacationNotCorrectException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VacationPayServiceTest {
    private final VacationPayService vacationPayService;
    private final int minDays;
    private final int maxDays;
    private final double daysPerYear;
    private final double averageSalary = 100.5;
    private final int vacationDays = 10;

    @Autowired
    public VacationPayServiceTest(Environment env, VacationPayService vacationPayService) {
        this.vacationPayService = vacationPayService;
        minDays = Integer.parseInt(env.getProperty("salary-settings.min-days-vacation"));
        maxDays = Integer.parseInt(env.getProperty("salary-settings.max-days-vacation"));
        daysPerYear = Double.parseDouble(env.getProperty("salary-settings.number-days-per-year"));
    }

    @Test
    @DisplayName("Расчет отпускных при корректных входных данных")
    void calculateVacationPay() {
        double expectedPay = averageSalary * vacationDays / daysPerYear;
        double actualPay = vacationPayService.calculateVacationPay(averageSalary, vacationDays);
        assertEquals(expectedPay, actualPay, 0.01);
    }

    @Test
    @DisplayName("Расчет отпускных вводе дней отпуска меньше минимального")
    void calculateVacationPayWithTooSmallDays() {
        DurationVacationNotCorrectException thrown = assertThrows(DurationVacationNotCorrectException.class, () ->
                vacationPayService.calculateVacationPay(100.0, minDays - 1));

        assertTrue(thrown.getMessage().contains("minimum"));

    }

    @Test
    @DisplayName("Расчет отпускных вводе дней отпуска больше максимального")
    void calculateVacationPayWithTooManyDays() {
        DurationVacationNotCorrectException thrown = assertThrows(DurationVacationNotCorrectException.class, () ->
                vacationPayService.calculateVacationPay(100.0, maxDays + 1));

        assertTrue(thrown.getMessage().contains("maximum"));
    }

    @Test
    @DisplayName("Расчет отпускных с корректными входными данными и указанием даты начала отпуска. " +
            "На отпуск не приходятся праздничные дни")
    void calculateVacationPayWithStartDate(){
        double expectedPay = averageSalary * vacationDays / daysPerYear;
        double actualPay = vacationPayService.calculateVacationPay(averageSalary, vacationDays, LocalDate.parse("2024-02-01"));
        assertEquals(expectedPay, actualPay, 0.01);
    }
    @Test
    @DisplayName("Расчет отпускных с корректными входными данными и указанием даты начала отпуска. " +
            "На отпуск приходится праздничный день")
    void calculateVacationPayWithStartDateAndHolidays(){
        double expectedPay = averageSalary * (vacationDays -1) / daysPerYear;
        double actualPay = vacationPayService.calculateVacationPay(averageSalary, vacationDays, LocalDate.parse("2024-02-15"));
        assertEquals(expectedPay, actualPay, 0.01);
    }
}