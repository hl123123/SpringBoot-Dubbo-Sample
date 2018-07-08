package com.tyrival.ocr.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

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
public class WordUtil {

    private final static String WORD_SUFFIX = ".doc";
    private final static String WORDX_SUFFIX = ".docx";

    public static String read(String filePath) throws IOException, OpenXML4JException, XmlException {
        if (filePath.endsWith(WORD_SUFFIX)) {
            //word 2003
            InputStream is = new FileInputStream(new File(filePath));
            WordExtractor ex = new WordExtractor(is);
            return ex.getText();
        } else {
            //word 2007
            OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
            POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
            return extractor.getText();
        }
    }
}
