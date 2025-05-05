package com.example.xlsx_min_service;

import com.example.xlsx_min_service.exception.FileProcessingException;
import com.example.xlsx_min_service.exception.InvalidCellFormatException;
import com.example.xlsx_min_service.service.impl.SearchService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    private SearchService searchService;

    private String filePath = "src/test/resources/numbers.xlsx";
    private String filePathNotFound = "invalid/path/to/file.xlsx";
    private String fileInvalidPath = "src/test/resources/invalid_format.xlsx";
    private String message = "Ожидалось другое минимальное значение";

    @BeforeEach
    void setUp() {
        searchService = new SearchService();
    }

    @Test
    void testFindMin_validExcel_shouldReturnCorrectValue() throws Exception {
        File file = new File(filePath);
        List<Integer> numbers = new ArrayList<>();

        try (InputStream is = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    numbers.add((int) cell.getNumericCellValue());
                }
            }
        }

        Collections.sort(numbers);
        Integer expectedMin = numbers.get(0);

        Integer actualMin = searchService.findMin(filePath, 1);
        assertEquals(expectedMin, actualMin, message);
    }

    @Test
    void testFindMin_fileNotFound_shouldThrowException() {
        assertThrows(FileProcessingException.class, () -> searchService.findMin(filePathNotFound, 1));
    }

    @Test
    void testFindMin_invalidCellFormat_shouldThrowException() {
        assertThrows(InvalidCellFormatException.class, () -> searchService.findMin(fileInvalidPath, 1));
    }
}