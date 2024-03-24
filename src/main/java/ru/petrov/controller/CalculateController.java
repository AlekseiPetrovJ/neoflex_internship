package ru.petrov.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.petrov.service.VacationPayService;

@RestController
@Validated
@RequestMapping(path = "/calculacte")
public class CalculateController {
    private final VacationPayService vacationPayService;

    @Autowired
    public CalculateController(VacationPayService vacationPayService) {
        this.vacationPayService = vacationPayService;
    }

    @GetMapping()
    public String get(@RequestParam(value = "averageSalary", required = false)
                            @Min(value = 0, message = "Average annual salary not be less than 0") double averageSalary,
                      @RequestParam(value = "numberOfVacationDays", required = false)
                      @Min(value = 1, message = "Number days of vacation not be less than 1")
                      @Max(value = 50, message = "Number days of vacation not be greater than 50") int numberOfVacationDays) {
        return "vacation pay: " + vacationPayService.calculateVacationPay(averageSalary, numberOfVacationDays);
    }
}
