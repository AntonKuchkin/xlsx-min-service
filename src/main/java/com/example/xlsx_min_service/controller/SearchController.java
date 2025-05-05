package com.example.xlsx_min_service.controller;

import com.example.xlsx_min_service.service.api.SearchServiceApi;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchServiceApi searchService;

    @GetMapping("/min")
    @Operation(summary = "Получить минимальное число")
    public Integer findMin (@RequestParam String path,@RequestParam Integer number) throws Exception {
        return searchService.findMin(path, number);
    }
}
