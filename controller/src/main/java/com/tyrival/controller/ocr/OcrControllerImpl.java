package com.tyrival.controller.ocr;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tyrival.common.ocr.OcrService;
import com.tyrival.entity.base.Result;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/8
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@RestController
public class OcrControllerImpl implements OcrController {

    @Reference
    private OcrService ocrService;

    @Override
    public Result doOCR(HttpServletRequest httpReq, HttpServletResponse httpRsp, String file) throws Exception {
        String content = ocrService.doOCR(file);
        return new Result(content);
    }
}
