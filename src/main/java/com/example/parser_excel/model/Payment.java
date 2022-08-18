package com.example.parser_excel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @JsonProperty("personal_account")
    private String personalAccount;
    private String date;
    private Double sum;
    private Double commission;
    private String service;
    private String id;
}
