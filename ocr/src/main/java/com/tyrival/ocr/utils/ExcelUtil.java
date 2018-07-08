package com.tyrival.ocr.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/7
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class ExcelUtil {

    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";

    public static String read(String filePath) throws Exception {
        File excelFile = new File(filePath);
        FileInputStream in = new FileInputStream(excelFile);
        validate(excelFile);
        Workbook workbook = getWorkbok(in, excelFile);
        int sheetCount = workbook.getNumberOfSheets();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (Row row : sheet) {
                try {
                    if (row.getCell(0).toString().equals("")) {
                        continue;
                    }
                    int end = row.getLastCellNum();
                    for (int j = 0; j < end; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            continue;
                        }

                        Object obj = getValue(cell);
                        sb.append(obj.toString()).append("\t");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param in
     * @param file
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbok(InputStream in, File file) throws IOException {
        Workbook wb = null;
        if (file.getName().endsWith(EXCEL_XLS)) {
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 判断文件是否是excel
     *
     * @throws Exception
     */
    private static void validate(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception("文件不存在");
        }
        if (!file.isFile()) {
            throw new Exception("文件不是Excel");
        }
        if (!file.getName().endsWith(EXCEL_XLS) || !file.getName().endsWith(EXCEL_XLSX)) {
            throw new Exception("文件不是Excel");
        }
    }

    private static Object getValue(Cell cell) {
        Object obj = null;
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case ERROR:
                obj = cell.getErrorCellValue();
                break;
            case NUMERIC:
                obj = cell.getNumericCellValue();
                break;
            case STRING:
                obj = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return obj;
    }

}
