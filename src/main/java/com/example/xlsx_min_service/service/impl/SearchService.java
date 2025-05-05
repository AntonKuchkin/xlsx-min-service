package com.example.xlsx_min_service.service.impl;

import com.example.xlsx_min_service.exception.FileProcessingException;
import com.example.xlsx_min_service.exception.FileReadingException;
import com.example.xlsx_min_service.exception.InvalidCellFormatException;
import com.example.xlsx_min_service.service.api.SearchServiceApi;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static com.example.xlsx_min_service.exception.ExceptionConst.ERROR_CHECKING_FILE;
import static com.example.xlsx_min_service.exception.ExceptionConst.ERROR_READING_FILE;
import static com.example.xlsx_min_service.exception.ExceptionConst.FILE_NOT_FOUND;

@Service
public class SearchService implements SearchServiceApi {

    @Override
    public Integer findMin(String path, Integer number) {

        File file = checkFile(path);
        List<Integer> numbers = checkNumberFormat(file);
        return numbers.get(number - 1);
    }

    /**
     * Проверяет существование файла по указанному пути.
     * <p>
     * Если файл не найден — выбрасывается исключение {@link FileProcessingException} с сообщением о том, что файл не найден.
     * Если в процессе проверки возникла другая ошибка — выбрасывается {@link FileProcessingException} с обернутым оригинальным исключением.
     *
     * @param path путь к файлу
     * @return объект {@link File}, если файл существует
     * @throws FileProcessingException если файл не существует или возникла ошибка при проверке
     */
    private File checkFile(String path) {
        try {
            File file = new File(path);

            if (!file.exists()) {
                throw new FileProcessingException(FILE_NOT_FOUND + path);
            }
            return file;

        } catch (FileProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new FileProcessingException(ERROR_CHECKING_FILE + path, e);
        }
    }

    /**
     * Читает файл Excel и извлекает числовые значения из первого столбца первого листа.
     * <p>
     * Если какая-либо ячейка не является числом, выбрасывается {@link InvalidCellFormatException}.
     * Если при чтении файла возникает ошибка ввода-вывода — выбрасывается {@link FileReadingException}.
     * Полученные значения сортируются с помощью быстрой сортировки.
     *
     * @param file файл Excel
     * @return список чисел, отсортированных по возрастанию
     * @throws InvalidCellFormatException если хотя бы одна ячейка содержит нечисловое значение
     * @throws FileReadingException       если возникает ошибка при чтении файла
     */
    private List<Integer> checkNumberFormat(File file) {
        List<Integer> numbers = new ArrayList<>();

        try (InputStream fileInputStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    numbers.add((int) cell.getNumericCellValue());
                } else {
                    throw new InvalidCellFormatException(row.getRowNum());
                }
            }

        } catch (IOException e) {
            throw new FileReadingException(ERROR_READING_FILE + e.getMessage(), e);
        }

        quickSort(numbers);
        return numbers;
    }

    /**
     * Сортирует список целых чисел с использованием алгоритма быстрой сортировки (QuickSort).
     *
     * @param list список чисел для сортировки;
     */
    private void quickSort(List<Integer> list) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{0, list.size() - 1});

        while (!stack.isEmpty()) {
            int[] range = stack.pop();
            int low = range[0];
            int high = range[1];

            if (low < high) {
                int pivotIndex = partition(list, low, high);

                if (pivotIndex + 1 < high) {
                    stack.push(new int[]{pivotIndex + 1, high});
                }
                if (pivotIndex - 1 > low) {
                    stack.push(new int[]{low, pivotIndex - 1});
                }
            }
        }
    }

    /**
     * Делит list на две части относительно опорного элемента (pivot).
     * Все элементы, меньшие или равные pivot, будут перемещены влево, остальные — вправо.
     *
     * @param list список чисел
     * @param low  начальный индекс list
     * @param high конечный индекс list (pivot)
     * @return индекс, по которому встал опорный элемент после разделения
     */
    private int partition(List<Integer> list, int low, int high) {
        int pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j) <= pivot) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }

    /**
     * Обменивает местами два элемента списка по их индексам.
     *
     * @param list список чисел
     * @param i    индекс первого элемента
     * @param j    индекс второго элемента
     */
    private void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }


}
