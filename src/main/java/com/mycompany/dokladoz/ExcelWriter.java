package com.mycompany.dokladoz;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ExcelWriter {

    /**
     * Добавляет данные в уже открытый объект Workbook.
     * НЕ сохраняет файл на диск.
     *
     * @param workbook  Объект Workbook, с которым вы работаете.
     * @param sheetName Имя листа, в который нужно добавить данные.
     * @param rowData   Строки данных для записи.
     */
    public static void appendToOpenWorkbook(Workbook workbook, String sheetName, String... rowData) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }

        // --- НОВАЯ ЛОГИКА ПОИСКА СТРОКИ ---
        // Ищем последнюю строку, в которой есть данные.
        // Начинаем поиск с максимально возможного номера строки вниз.
        int lastRowNum = sheet.getLastRowNum();
        int targetRowIndex = 0; // По умолчанию будем писать в первую строку, если лист пуст

        // Если на листе вообще нет строк, lastRowNum будет -1.
        // В этом случае targetRowIndex останется 0 (создадим первую строку).

        if (lastRowNum > 0) {
            // Цикл идет от последней строки вверх до первой (0)
            for (int i = lastRowNum; i >= 0; i--) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    // Проверяем, есть ли в этой строке хотя бы одна непустая ячейка
                    if (!isRowEmpty(row)) {
                        // Если нашли строку с данными, новая строка будет под ней
                        targetRowIndex = i + 1;
                        break; // Выходим из цикла, мы нашли нужное место
                    }
                }
            }
        }

        // --- СОЗДАНИЕ И ЗАПОЛНЕНИЕ НОВОЙ СТРОКИ ---
        Row newRow = sheet.createRow(targetRowIndex);

        for (int i = 0; i < rowData.length; i++) {
            Cell cell = newRow.createCell(i);
            cell.setCellValue(rowData[i]);
        }

        System.out.println("Данные добавлены в строку с индексом: " + targetRowIndex);
    }

    // Вспомогательный метод для проверки, пуста ли строка
    private static boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false; // Нашли непустую ячейку
            }
        }
        return true; // Все ячейки пустые или отсутствуют
    }


    public static void go(String... rowData) {
        // ВАЖНО: Указываем путь к исходному файлу
        String originalFilePath = "data/ЖОЗ.xlsm";

        // Создаем имя для временного файла в системной папке Temp
        String tempFilePath = System.getProperty("java.io.tmpdir") + "temp_excel_workbook.xlsm";

        Workbook workbook = null;

        try {
            // --- ЭТАП 1: СОЗДАНИЕ ВРЕМЕННОЙ КОПИИ ---
            // Это позволит нам работать с файлом, даже если оригинал открыт в Excel.
            Path source = Paths.get(originalFilePath);
            Path destination = Paths.get(tempFilePath);

            // Копируем файл. Если файл существует, заменяем его.
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Создана временная копия для работы: " + tempFilePath);


            // --- ЭТАП 2: ОТКРЫТИЕ ВРЕМЕННОЙ КОПИИ ---
            try (FileInputStream fis = new FileInputStream(tempFilePath)) {
                workbook = WorkbookFactory.create(fis);

                // --- ЭТАП 3: РАБОТА С ДАННЫМИ ---
                ExcelWriter.appendToOpenWorkbook(workbook, "Журнал учёта", rowData);
                System.out.println("Данные добавлены в объект Workbook.");
            }

            // --- ЭТАП 4: ЗАПИСЬ ИЗМЕНЕНИЙ ВО ВРЕМЕННУЮ КОПИЮ ---
            try (FileOutputStream fileOut = new FileOutputStream(tempFilePath)) {
                workbook.write(fileOut);
                System.out.println("Изменения успешно записаны во временный файл.");
            }

            // --- ЭТАП 5: ЗАМЕНА ИСХОДНОГО ФАЙЛА ---
            // Теперь, когда все изменения сохранены во временный файл,
            // мы заменяем им заблокированный оригинал.

            // Формируем команду для Robocopy

            Path temp = Paths.get(tempFilePath);
            Path backup = Paths.get(originalFilePath + ".bak"); // Резервная копия

            try {
                // Шаг 1: Переименовываем (перемещаем) заблокированный файл в .bak
                // Операция MOVE часто проходит там, где COPY дает сбой.
                Files.move(source, backup, StandardCopyOption.REPLACE_EXISTING);

                // Шаг 2: Перемещаем наш новый файл на освободившееся место
                Files.move(temp, source, StandardCopyOption.REPLACE_EXISTING);

                // Шаг 3: Удаляем старый файл-резервную копию
                Files.deleteIfExists(backup);

            } catch (IOException e) {
                System.err.println("Ошибка при перемещении файлов: " + e.getMessage());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Безопасное закрытие workbook
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
