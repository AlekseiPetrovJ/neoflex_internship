package ru.petrov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.petrov.util.exception.DurationVacationNotCorrectException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class VacationPayService {
    private final Environment environment;
    private static final int SCALE = 2;

    @Autowired
    public VacationPayService(Environment environment) {
        this.environment = environment;
    }

    /**
     * @return vacation pay
     * @throws DurationVacationNotCorrectException if numberOfVacationDays not between min and max days of vacation
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

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(SCALE, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
