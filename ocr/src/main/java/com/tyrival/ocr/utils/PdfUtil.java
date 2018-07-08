package com.tyrival.ocr.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/7
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class PdfUtil {

    public static String read(String filePath) throws Exception {

        File pdfFile = new File(filePath);

        // 是否排序
        boolean sort = true;

        // 开始提取页数
        int startPage = 1;

        // 结束提取页数
        int endPage = Integer.MAX_VALUE;

        PDDocument document = PDDocument.load(pdfFile);

        // PDFTextStrippe提取文本
        PDFTextStripper stripper = new PDFTextStripper();

        // 设置是否排序
        stripper.setSortByPosition(sort);

        // 设置起始页
        stripper.setStartPage(startPage);

        // 设置结束页
        stripper.setEndPage(endPage);

        // 调用PDFTextStripper的writeText提取并输出文本
        System.out.println(stripper.getText(document));
        return stripper.getText(document);
    }
}
