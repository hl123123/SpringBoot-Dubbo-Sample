package com.tyrival.ocr.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.tyrival.common.ocr.OcrService;
import com.tyrival.ocr.utils.ExcelUtil;
import com.tyrival.ocr.utils.FileUtil;
import com.tyrival.ocr.utils.OcrUtil;
import com.tyrival.ocr.utils.WordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/8
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@Service
@org.springframework.stereotype.Service
public class OcrServiceImpl implements OcrService {

    @Value("${tess.data}")
    private String tessData;

    @Override
    public String doOCR(String filePath) throws Exception {
        if (FileUtil.isWord(filePath)) {
            return WordUtil.read(filePath);
        }
        if (FileUtil.isExcel(filePath)) {
            return ExcelUtil.read(filePath);
        }
        String result = OcrUtil.doOCR(filePath, this.getTessData());
        return result;
    }

    private String getTessData() {
        return ClassUtils.getDefaultClassLoader().getResource("").getPath() + tessData;
    }
}
