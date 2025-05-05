package com.example.xlsx_min_service;

import com.example.xlsx_min_service.controller.SearchController;
import com.example.xlsx_min_service.exception.FileProcessingException;
import com.example.xlsx_min_service.exception.InvalidCellFormatException;
import com.example.xlsx_min_service.service.api.SearchServiceApi;
import com.example.xlsx_min_service.service.impl.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
@Import(SearchService.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchServiceApi searchService;

    @Test
    void testFindMin_success() throws Exception {
        String path = "some/path.xlsx";
        when(searchService.findMin(path, 1)).thenReturn(42);

        mockMvc.perform(get("/api/min")
                        .param("path", path)
                        .param("number", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));
    }

    @Test
    void testFindMin_fileNotFound() throws Exception {
        String path = "invalid.xlsx";
        when(searchService.findMin(path, 1)).thenThrow(new FileProcessingException("Файл не найден"));

        mockMvc.perform(get("/api/min")
                        .param("path", path)
                        .param("number", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Файл не найден")));
    }

    @Test
    void testFindMin_invalidCellFormat() throws Exception {
        String path = "bad.xlsx";
        when(searchService.findMin(path, 1)).thenThrow(new InvalidCellFormatException("Неверный формат"));

        mockMvc.perform(get("/api/min")
                        .param("path", path)
                        .param("number", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Неверный формат")));
    }
}
