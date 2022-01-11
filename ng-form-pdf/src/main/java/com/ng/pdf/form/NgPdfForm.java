package com.ng.pdf.form;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.ng.form.item.FormConfig;
import com.ng.form.item.Record;
import com.ng.form.tool.DateUtils;
import com.ng.pdf.item.FormItem;
import com.ng.pdf.tool.PdfUtils;

/**
 * pdf form组件
 * @author lyf
 *
 */
public class NgPdfForm {

	private JSONObject template;
	
	private JSONObject value ;
	
	private String title;
	 
	private Font font ;
	
	public NgPdfForm(JSONObject template , JSONObject value ) {
		// TODO Auto-generated constructor stub
		this.template = template;
		this.value = value; 
		 
	}
	
	public NgPdfForm(JSONObject template , JSONObject value , String title) {
		// TODO Auto-generated constructor stub
		this.template = template;
		this.value = value; 
		this.title = title ;
	}
	
	public NgPdfForm(JSONObject template , JSONObject value , String title,Font font) {
		// TODO Auto-generated constructor stub
		this.template = template;
		this.value = value; 
		this.title = title ;
		this.font = font ;
		
		PdfUtils.setFont(font);
	}
	 
	/**
	 * 输出pdf流数据
	 * @return
	 */
	public byte[] getPdfData() {
		
		Paragraph paragraph = toParagraph();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		 Document document = new Document(PageSize.A4, 10, 10, 10, 10);
	        PdfWriter mPdfWriter;
	        try {
	            mPdfWriter = PdfWriter.getInstance(document, out);

	            document.open();
	            //title 不为空则加一个封皮
	            if(StringUtils.isNotBlank(title)) {
	            	document.addTitle(title);

		            // 写一个封皮
		            for (int i = 0; i < 5; i++) {
		                document.add(Chunk.NEWLINE);
		            }

		            Paragraph para = new Paragraph();
		            para.setSpacingAfter(50f);
		            para.setSpacingBefore(50f);
		            para.setAlignment(Paragraph.ALIGN_CENTER);
		            para.setLeading(30);

		            Phrase p = PdfUtils.getText(title);
		            p.getFont().setSize(20f);

		            para.add(p);

		            for (int i = 0; i < 15; i++) {
		                para.add(Chunk.NEWLINE);
		            }


		           

		            Phrase p3 = PdfUtils.getText(DateUtils. format(new Date(), "yyyy-MM-dd"));

		            p3.getFont().setSize(15f);
		            para.add(p3);

		            document.add(para);

		            document.newPage();
	            }
	            	

	            document.add(paragraph);

	            document.close();
	            mPdfWriter.flush();
	            mPdfWriter.close();

	        } catch (DocumentException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }

		
		return out.toByteArray();
	}
	
	public Paragraph toParagraph() {
		
		FormConfig formConfig = null;
		
		// 2020-10-14 lyf 判断该整个page是否需要显示
		if(template.containsKey("config")) {
			
			formConfig = template.getObject("config", FormConfig.class);
			 
		} else {
			formConfig = new FormConfig();
			
		} 
		
		Paragraph p = new Paragraph();
		
		p.setIndentationLeft(50);
	    p.setIndentationRight(50);
	    p.setSpacingBefore(50);
	    
		
		if(template.containsKey("list")) {
			// 取到list
			JSONArray list = template.getJSONArray("list");
			
			int listSize = list.size();
			 
			for(int j = 0 ; j < listSize ; j++) {
				Record record = list.getObject(j, Record.class);
				
				FormItem item = new FormItem();
				 
				Element element = item.reder(formConfig,value,record);
				p.add(element);
				
				// 换行
				p.add(Chunk.NEWLINE);
			
			}
			  
		}
		
		return p ;
	}
}
