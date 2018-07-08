package com.tyrival.ocr.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/8
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class OcrUtil {

    private final static String CHINESE = "chi_sim";
    private final static String ENGLISH = "eng";

    private static ITesseract instance;

    public static String doOCR(String filePath, String tessData) throws TesseractException, FileNotFoundException {
        return doOCR(filePath, tessData, CHINESE);
    }

    public static String doOCR(String filePath, String tessData, String language) throws TesseractException, FileNotFoundException {

        ITesseract instance = getTesseract();
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new FileNotFoundException("未找到 " + filePath + " 文件");
        }
        instance.setDatapath(tessData);
        instance.setLanguage(language);
        String result = instance.doOCR(file);
        return result;
    }

    private static ITesseract getTesseract() {
        if (instance == null) {
            instance = new Tesseract();
        }
        return instance;
    }
}
