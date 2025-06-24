package com.totfd.lms.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totfd.lms.dto.mcq.McqDTO;
import com.totfd.lms.dto.mcqupload.McqUploadResult;
import com.totfd.lms.entity.Mcq;
import com.totfd.lms.repository.McqRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ExcelMcqParser {

    private final McqRepository mcqRepository;

    public McqUploadResult parseAndValidate(InputStream is, Long subTopicId) throws IOException {
        List<McqDTO> validMcqs = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        Set<String> seenQuestions = new HashSet<>();

        List<String> existing = mcqRepository.findBySubTopicId(subTopicId)
                .stream().map(q -> q.getQuestion().trim().toLowerCase()).toList();

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next(); // Skip header

        int rowNumber = 1;
        while (rows.hasNext()) {
            rowNumber++;
            Row row = rows.next();

            // ✅ Skip entirely empty rows
            boolean isEmptyRow = true;
            for (Cell cell : row) {
                if (!StringUtils.isBlank(getCellString(cell))) {
                    isEmptyRow = false;
                    break;
                }
            }
            if (isEmptyRow) continue;

            try {
                String question = getCellString(row.getCell(0));
                String correctRaw = getCellString(row.getCell(1));

                if (StringUtils.isBlank(question) || StringUtils.isBlank(correctRaw)) {
                    skipped.add("Row " + rowNumber + ": Missing question or correct option(s)");
                    continue;
                }

                String normalized = question.trim().toLowerCase();
                if (seenQuestions.contains(normalized) || existing.contains(normalized)) {
                    skipped.add("Row " + rowNumber + ": Duplicate question");
                    continue;
                }

                seenQuestions.add(normalized);

                // Parse options starting from column index 2 onward
                Map<String, String> options = new LinkedHashMap<>();
                int optCol = 2;
                char label = 'A';

                while (true) {
                    Cell cell = row.getCell(optCol);
                    if (cell == null || StringUtils.isBlank(cell.toString())) break;

                    String labelStr = String.valueOf(label);
                    options.put(labelStr, cell.toString().trim());
                    optCol++;
                    label++;
                }

                if (options.size() < 2) {
                    skipped.add("Row " + rowNumber + ": Less than 2 options provided");
                    continue;
                }

                // Normalize correct options (comma or space separated)
                Set<String> validKeys = options.keySet();
                Set<String> correctOptions = Arrays.stream(correctRaw.split("[,\\s]+"))
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .filter(validKeys::contains)
                        .collect(Collectors.toSet());

                if (correctOptions.isEmpty()) {
                    skipped.add("Row " + rowNumber + ": No valid correct option found");
                    continue;
                }

                McqDTO dto = new McqDTO();
                dto.setQuestion(question);
                dto.setOptions(new ObjectMapper().writeValueAsString(options));
                dto.setCorrectOption(String.join(",", correctOptions));
                dto.setSubTopicId(subTopicId);
                dto.setVersion(1);
                dto.setIsLatest(true);

                validMcqs.add(dto);

            } catch (Exception e) {
                skipped.add("Row " + rowNumber + ": Exception - " + e.getMessage());
            }
        }

        workbook.close();
        return new McqUploadResult(validMcqs, skipped);
    }

    private String getCellString(Cell cell) {
        return cell == null ? "" : cell.toString().trim();
    }
}
