package com.example.parser_excel.service;

import com.example.parser_excel.model.Payment;
import com.example.parser_excel.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final Transaction transaction;

    public Transaction read(MultipartFile file) throws IOException {
        List<Payment> payments = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet worksheet = workbook.getSheetAt(0);
        log.info("Парсинг файла excel...");
        try {
            for (int i = 4; i < worksheet.getPhysicalNumberOfRows(); i++) {
                Row row = worksheet.getRow(i);
                String account = row.getCell(0).getStringCellValue();
                String date = row.getCell(1).getStringCellValue();
                double sum = row.getCell(2).getNumericCellValue();
                double commission = row.getCell(3).getNumericCellValue();
                String service = row.getCell(4).getStringCellValue();
                long num = (long) row.getCell(5).getNumericCellValue();
                String id = String.valueOf(num);
                Payment payment = new Payment();
                payment.setDate(date);
                payment.setSum(sum);
                payment.setPersonalAccount(account);
                payment.setCommission(commission);
                payment.setService(service);
                payment.setId(id);
                payments.add(payment);
            }
        } catch (NullPointerException | IllegalStateException e) {
            log.error("Пустое поле или строка в xlsx фале! {}", e.getMessage());
            e.printStackTrace();
        }
        Double totalAmount = payments.stream().mapToDouble(Payment::getSum).sum();
        Double totalCommission = payments.stream().mapToDouble(Payment::getCommission).sum();
        transaction.setPayments(payments);
        transaction.setTotalAmount(totalAmount);
        transaction.setTotalCommission(totalCommission);
        return transaction;
    }
}
