package com.example.parser_excel.controller;

import com.example.parser_excel.model.Payment;
import com.example.parser_excel.model.Transaction;
import com.example.parser_excel.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;


    @PostMapping
    public ResponseEntity<Transaction> getFileXlsx(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(paymentService.readXlsx(file), HttpStatus.OK);
    }

    @PostMapping("/xls")
    public ResponseEntity<List<Payment>> getFileXls(@RequestParam(value = "file")
                                                    MultipartFile file) throws IOException {
        return new ResponseEntity<>(paymentService.readXls(file), HttpStatus.OK);
    }
}
