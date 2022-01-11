package com.ng.form;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.ng.form.tool.DateUtils;
import com.ng.pdf.form.NgPdfForm;

public class TestPdf {

	public void test1() throws IOException, DocumentException {
		
		 BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);   
		  com.itextpdf.text.Font FontChinese24 = new com.itextpdf.text.Font(bfChinese, 24, com.itextpdf.text.Font.BOLD);
		
		File pdfFile = new File("d:/" , "测试_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf");
		
		String template = IOUtils.toString(getClass().getResource("/pdf/template.json") , "UTF-8");
		String value = IOUtils.toString(getClass().getResource("/pdf/value.json") , "UTF-8");
		
		JSONObject templateJson = JSON.parseObject(template);
		JSONObject valueJson = JSON.parseObject(value);
		
		NgPdfForm pdfForm = new NgPdfForm(templateJson , valueJson , "test" , FontChinese24);
		 
		byte[] data = pdfForm.getPdfData();
		
		FileUtils.writeByteArrayToFile(pdfFile, data);
		
		 
		 
		 
		
	}
	 
	public static void main(String[] args) throws IOException, DocumentException {
		TestPdf t = new TestPdf();
		t.test1();
	}
}
