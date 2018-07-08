package com.tyrival.controller.ocr;

import com.tyrival.entity.base.Result;
import com.tyrival.entity.param.QueryParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/ocr")
public interface OcrController {


    @GetMapping("/do")
    Result doOCR(HttpServletRequest httpReq, HttpServletResponse httpRsp, String file) throws Exception;
}
