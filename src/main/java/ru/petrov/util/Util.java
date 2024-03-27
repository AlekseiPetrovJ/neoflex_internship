package ru.petrov.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    private static final int SCALE = 2;

    /**
     *
     * @param startDate дата начала периода
     * @param endDate дата окончания периода
     * @return List содержащий даты периода
     */
    public static List<LocalDate> getDatesBetween(
            LocalDate startDate, LocalDate endDate) {

        return startDate.datesUntil(endDate)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param startDate Дата начала периода
     * @param duration длительность
     * @return List содержащий даты периода
     */
    public static List<LocalDate> getDatesBetween(
            LocalDate startDate, int duration) {

        return startDate.datesUntil(startDate.plusDays(duration))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param period List содержащий даты периода
     * @param holidays List содержащий даты праздничных (нерабочих) дней
     * @return List содержащий даты периода за исключением праздничных (нерабочих) дней
     */

    public static List<LocalDate> getDaysWithoutHolidays(
            List<LocalDate> period, List<LocalDate> holidays) {

        return period.stream()
                .filter(localDate -> !holidays.contains(localDate))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param period List содержащий даты периода
     * @param holidays List содержащий даты праздничных (нерабочих) дней
     * @return количество дней периода за исключением праздничных дней
     */
    public static int getCountDaysWithoutHolidays(
            List<LocalDate> period, List<LocalDate> holidays) {

        return (int) period.stream()
                .filter(localDate -> !holidays.contains(localDate))
                .count();
    }

    /**
     * Округляет double до нужного разряда
     * @param value огругляемое значение
     * @return округленное значение
     */
    public static double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(SCALE, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}