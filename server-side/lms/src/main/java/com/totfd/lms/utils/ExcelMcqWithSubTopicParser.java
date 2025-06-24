package com.totfd.lms.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totfd.lms.dto.mcq.McqDTO;
import com.totfd.lms.dto.mcqupload.McqUploadResult;
import com.totfd.lms.repository.McqRepository;
import com.totfd.lms.repository.SubTopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelMcqWithSubTopicParser {

    private final McqRepository mcqRepository;
    private final SubTopicRepository subTopicRepository;

    public McqUploadResult parseAndValidate(InputStream is) throws IOException {
        List<McqDTO> validMcqs = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        Set<String> seenQuestions = new HashSet<>();

        // ✅ Fetch existing questions grouped by subtopicId
        Map<Long, Set<String>> existing = mcqRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        mcq -> mcq.getSubTopic().getId(),
                        Collectors.mapping(mcq -> mcq.getQuestion().trim().toLowerCase(), Collectors.toSet())
                ));

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next(); // Skip header

        int rowNumber = 1;
        while (rows.hasNext()) {
            rowNumber++;
            Row row = rows.next();

            boolean isEmptyRow = true;
            for (Cell cell : row) {
                if (!StringUtils.isBlank(getCellString(cell))) {
                    isEmptyRow = false;
                    break;
                }
            }
            if (isEmptyRow) continue;

            try {
                String subTopicIdStr = getCellString(row.getCell(0));
                String question = getCellString(row.getCell(1));
                String correctRaw = getCellString(row.getCell(2));
                log.info("subtopiidstr [{}]", subTopicIdStr);

                if (StringUtils.isBlank(subTopicIdStr) || StringUtils.isBlank(question) || StringUtils.isBlank(correctRaw)) {
                    skipped.add("Row " + rowNumber + ": Missing required fields");
                    continue;
                }

                Long subTopicId;
                try {
                    subTopicId = Long.parseLong(subTopicIdStr);
                } catch (NumberFormatException e) {
                    skipped.add("Row " + rowNumber + ": Invalid SubTopic ID");
                    continue;
                }

                if (!subTopicRepository.existsById(subTopicId)) {
                    skipped.add("Row " + rowNumber + ": SubTopic ID not found: " + subTopicId);
                    continue;
                }

                String normalized = question.trim().toLowerCase();
                String key = subTopicId + "|" + normalized;

                if (seenQuestions.contains(key) ||
                        existing.getOrDefault(subTopicId, Set.of()).contains(normalized)) {
                    skipped.add("Row " + rowNumber + ": Duplicate question for SubTopic ID " + subTopicId);
                    continue;
                }

                seenQuestions.add(key);

                Map<String, String> options = new LinkedHashMap<>();
                int optCol = 3;
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
                    skipped.add("Row " + rowNumber + ": Less than 2 options");
                    continue;
                }

                Set<String> validKeys = options.keySet();
                Set<String> correctOptions = Arrays.stream(correctRaw.split("[,\\s]+"))
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .filter(validKeys::contains)
                        .collect(Collectors.toSet());

                if (correctOptions.isEmpty()) {
                    skipped.add("Row " + rowNumber + ": No valid correct options");
                    continue;
                }

                McqDTO dto = new McqDTO();
                dto.setSubTopicId(subTopicId);
                dto.setQuestion(question);
                dto.setCorrectOption(String.join(",", correctOptions));
                dto.setOptions(new ObjectMapper().writeValueAsString(options));
                dto.setVersion(1);
                dto.setIsLatest(true);

                validMcqs.add(dto);

            } catch (Exception e) {
                skipped.add("Row " + rowNumber + ": Error - " + e.getMessage());
            }
        }

        workbook.close();
        return new McqUploadResult(validMcqs, skipped);
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue()); // avoid decimals
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> cell.toString().trim();
        };
    }
}
