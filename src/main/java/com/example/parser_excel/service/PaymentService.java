package com.example.parser_excel.service;

import com.example.parser_excel.model.Payment;
import com.example.parser_excel.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final Transaction transaction;

    public Transaction readXlsx(MultipartFile file) throws IOException {
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

    public List<Payment> readXls(MultipartFile file) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        List<Payment> payments = new ArrayList<>();
        log.info("Парсинг файла xls");
        try {
            for (int i = 4; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                String date = row.getCell(0).getStringCellValue();
                String account = row.getCell(1).getStringCellValue();
                Double sum = row.getCell(2).getNumericCellValue();
                String num = row.getCell(3).getStringCellValue();
                double commission = Double.parseDouble(num);
                Payment payment = new Payment();
                payment.setDate(date);
                payment.setPersonalAccount(account);
                payment.setSum(sum);
                payment.setCommission(commission);
                payments.add(payment);
            }
        } catch (NumberFormatException | IllegalStateException e) {
            log.error("Пустое поле или строка в xls фале! {}", e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }
}
