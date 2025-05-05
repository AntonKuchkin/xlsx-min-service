package com.example.xlsx_min_service.service.api;

import com.example.xlsx_min_service.exception.FileProcessingException;
import com.example.xlsx_min_service.exception.InvalidCellFormatException;

import java.io.FileNotFoundException;

public interface SearchServiceApi {

    /**
     * Ищет число по заданному индексу после сортировки содержимого Excel-файла.
     *
     * <p>Метод открывает Excel-файл, считывает значения из первого столбца, сортирует их,
     * и возвращает минимальное число по переданному индексу (начиная с 1).</p>
     *
     * @param path   путь к файлу Excel (.xlsx)
     * @param number порядковый номер (начиная с 1) числа в отсортированном списке
     * @throws FileProcessingException если файл по указанному пути не найден
     * @throws InvalidCellFormatException если одна из ячеек не содержит числовое значение
     */
    Integer findMin(String path, Integer number) throws FileNotFoundException;

}
