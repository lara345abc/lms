package com.totfd.lms.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class McqExcelTemplateGenerator {

    public ByteArrayInputStream generateTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("MCQs");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Question");
        header.createCell(1).setCellValue("CorrectOptions (e.g., A,C or A C)");
        header.createCell(2).setCellValue("OptionA");
        header.createCell(3).setCellValue("OptionB");
        header.createCell(4).setCellValue("OptionC");
        header.createCell(5).setCellValue("OptionD");
        header.createCell(6).setCellValue("OptionE");
        header.createCell(7).setCellValue("OptionF");

        // Example Row (optional, helps users understand format)
        Row sample = sheet.createRow(1);
        sample.createCell(0).setCellValue("Which are prime numbers?");
        sample.createCell(1).setCellValue("B D");
        sample.createCell(2).setCellValue("4");
        sample.createCell(3).setCellValue("2");
        sample.createCell(4).setCellValue("6");
        sample.createCell(5).setCellValue("3");
        sample.createCell(6).setCellValue("9");
        sample.createCell(7).setCellValue(""); // OptionF left blank

        // Auto-size columns for better readability
        for (int i = 0; i <= 7; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    public ByteArrayInputStream generateTemplateWithSubTopic() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("MCQsWithSubTopic");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("SubTopicId");
        header.createCell(1).setCellValue("Question");
        header.createCell(2).setCellValue("CorrectOptions (e.g., A,C or A C)");
        header.createCell(3).setCellValue("OptionA");
        header.createCell(4).setCellValue("OptionB");
        header.createCell(5).setCellValue("OptionC");
        header.createCell(6).setCellValue("OptionD");
        header.createCell(7).setCellValue("OptionE");
        header.createCell(8).setCellValue("OptionF");

        // Sample row
        Row sample = sheet.createRow(1);
        sample.createCell(0).setCellValue("1001");
        sample.createCell(1).setCellValue("Which are prime numbers?");
        sample.createCell(2).setCellValue("B D");
        sample.createCell(3).setCellValue("4");
        sample.createCell(4).setCellValue("2");
        sample.createCell(5).setCellValue("6");
        sample.createCell(6).setCellValue("3");
        sample.createCell(7).setCellValue("9");
        sample.createCell(8).setCellValue(""); // OptionF

        for (int i = 0; i <= 8; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

}
