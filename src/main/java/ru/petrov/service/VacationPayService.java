package ru.petrov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.petrov.model.Holidays;
import ru.petrov.util.exception.DurationVacationNotCorrectException;

import java.time.LocalDate;
import java.util.List;

import static ru.petrov.util.Util.*;

@Service
public class VacationPayService {
    private final Environment environment;
    private final Holidays holidays;

    @Autowired
    public VacationPayService(Environment environment, Holidays holidays) {
        this.environment = environment;
        this.holidays = holidays;
    }

    /**
     * @param averageSalary годовая зарплата
     * @param numberOfVacationDays количество дней отпуска
     * @return Отпускные
     * @throws DurationVacationNotCorrectException если число дней отпуска меньше min или больше max
     */
    public double calculateVacationPay(Double averageSalary, int numberOfVacationDays) {
        int minDays = Integer.parseInt(environment.getProperty("salary-settings.min-days-vacation"));
        int maxDays = Integer.parseInt(environment.getProperty("salary-settings.max-days-vacation"));

        if (numberOfVacationDays < minDays) {
            throw new DurationVacationNotCorrectException(numberOfVacationDays + " days less than minimum (" + minDays + ")");
        }
        if (numberOfVacationDays > maxDays) {
            throw new DurationVacationNotCorrectException(numberOfVacationDays + " days more than maximum (" + maxDays + ")");
        }
        double daysPerYear = Double.parseDouble(environment.getProperty("salary-settings.number-days-per-year"));
        return round(averageSalary * numberOfVacationDays / daysPerYear);
    }

    /**
     *
     * @param averageSalary годовая зарплата
     * @param numberOfVacationDays количество дней отпуска
     * @param startDateVacation дата начала отпуска
     * @return Отпускные. Праздничные дни не оплачиваются.
     */
    public double calculateVacationPay(Double averageSalary, int numberOfVacationDays, LocalDate startDateVacation) {
        List<LocalDate> datesVacation = getDatesBetween(startDateVacation, numberOfVacationDays);
        int countDaysWithoutHolidays = getCountDaysWithoutHolidays(datesVacation, holidays.getFederal());
        return calculateVacationPay(averageSalary, countDaysWithoutHolidays);
    }
}