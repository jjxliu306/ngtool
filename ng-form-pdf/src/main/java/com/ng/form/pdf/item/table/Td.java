package com.ng.form.pdf.item.table;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.ng.form.item.Record;
import com.ng.form.pdf.utils.PdfUtils;
import com.ng.form.tool.FormUtil;
 

public class Td extends PdfPCell{

	 
	private String style ;
	private List<Record> list ;
	
	private Boolean visible ;
	private PdfWriter writer;
	private Object data;
	
	public void setStyle(String style) {
		this.style = style;
	}
	public String getStyle() {
		return style;
	}
	
	public void setList(List<Record> list) {
		this.list = list;
	}
	public List<Record> getList() {
		return list;
	}
	 
	public void initData(Object data , PdfWriter writer) {
		this.data =  JSON.toJSON( data);
		this.writer = writer;
		// 从data解析出来当前需要的数据 
				Phrase p = getText();
				
				setPhrase(p);
	}
	
	
	public boolean isVisible(Object data) {
		 
		if(visible != null) {
			return visible ;
		}
		
		if( data instanceof JSONObject) {
			JSONObject jobject = (JSONObject)  data;
			
			for(Record r : list) {  
				boolean rv = FormUtil.recordVisible(r, jobject);
				if(rv) {
					visible = true ;
					return true ;
				}
			}
		}
		visible = false;
		
		return false ;
	}
	
	private Paragraph getText() {
		
		com.itextpdf.text.Paragraph p = new Paragraph();
		 
		if(this.data instanceof JSONObject) {
			JSONObject jobject = (JSONObject) this.data;
			
			// 循环
			int index = 0;
			int size = list.size();
			for(Record r : list) {
//				String model = r.getModel();
//				String type = r.getType();
				
				// 判断要素是否显示
				boolean v = FormUtil.recordVisible(r, jobject);
				if(!v) continue ;
				
				Element parseValue = PdfUtils.getValue(jobject, r,writer);
				 
				p.add(parseValue);
				if(index < size - 1) {
					p.add(Chunk.NEWLINE);
				}
				
				
			/*	if(parseValue instanceof Chunk) {
					setPhrase(new Phrase((Chunk)parseValue));
				} else if(parseValue != null){
					setPhrase(new Phrase(parseValue.toString()));
				}*/
				
				index++;
			}
			
			
		}
		
		
		
		return p;
		
	}
	
	
}
