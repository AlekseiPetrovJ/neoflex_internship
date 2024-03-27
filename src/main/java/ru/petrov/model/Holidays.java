package ru.petrov.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "holidays")
public class Holidays {
    private List<LocalDate> federal;
    public List<LocalDate> getFederal() {
        return federal;
    }

    public void setFederal(List<LocalDate> federal) {
        this.federal = federal;
    }
}