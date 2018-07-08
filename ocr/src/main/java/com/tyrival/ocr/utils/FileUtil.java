package com.tyrival.ocr.utils;

import org.apache.commons.lang.StringUtils;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/8
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class FileUtil {

    private final static String DOC = "doc";
    private final static String DOCX = "docx";
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    public static boolean isWord(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        if (filePath.endsWith(DOC) || filePath.endsWith(DOCX)) {
            return true;
        }
        return false;
    }

    public static boolean isExcel(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        if (filePath.endsWith(XLS) || filePath.endsWith(XLSX)) {
            return true;
        }
        return false;
    }
}
