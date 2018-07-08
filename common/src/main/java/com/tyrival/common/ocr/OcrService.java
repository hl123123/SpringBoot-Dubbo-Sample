package com.tyrival.common.ocr;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/8
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public interface OcrService {

    /**
     * OCR文件内容
     * @param filePath
     * @return
     */
    String doOCR(String filePath) throws Exception;
}
