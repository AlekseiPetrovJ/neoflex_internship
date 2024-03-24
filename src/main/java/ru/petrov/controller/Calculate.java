package ru.petrov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.petrov.service.VacationPayService;

@RestController
@RequestMapping(path = "/calculacte")
public class Calculate {
    private final VacationPayService vacationPayService;

    @Autowired
    public Calculate(VacationPayService vacationPayService) {
        this.vacationPayService = vacationPayService;
    }

    @GetMapping()
    public String get(@RequestParam(value = "averageSalary", required = false) double averageSalary,
                      @RequestParam(value = "numberOfVacationDays", required = false) int numberOfVacationDays) {
        return "vacation pay: " + vacationPayService.calculateVacationPay(averageSalary, numberOfVacationDays);
    }
}
