package ru.petrov.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import ru.petrov.util.exception.DurationVacationNotCorrectException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VacationPayServiceTest {
    private final Environment env;
    private final VacationPayService vacationPayService;
    private final int minDays;
    private final int maxDays;
    private final double daysPerYear;

    @Autowired
    public VacationPayServiceTest(Environment env, VacationPayService vacationPayService) {
        this.env = env;
        this.vacationPayService = vacationPayService;
        minDays = Integer.parseInt(env.getProperty("salary-settings.min-days-vacation"));
        maxDays = Integer.parseInt(env.getProperty("salary-settings.max-days-vacation"));
        daysPerYear = Double.parseDouble(env.getProperty("salary-settings.number-days-per-year"));
    }

    @Test
    @DisplayName("Расчет отпускных при корректных входных данных")
    void calculateVacationPay() {
        double averageSalary = 100.5;
        int vacationDays = 10;
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
}