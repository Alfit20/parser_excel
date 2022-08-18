package com.example.parser_excel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private List<Payment> payments;
    @JsonProperty("total_amount")
    private Double totalAmount;
    @JsonProperty("total_commission")
    private Double totalCommission;
}
