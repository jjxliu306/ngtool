package com.ng.form.pdf.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.ng.form.item.Record;
import com.ng.form.pdf.item.PdfItem;
import com.ng.form.tool.FormUtil;

public class PdfUtils {

	
	public static final Integer CELL_BACKGROUND = Integer.valueOf("a9c8ea" , 16);

	
	private static Font getDefaultFont() {
		 
		return new Font();
	}
	
	/**
	 * 从json中解析出当前record的值
	 * @param value
	 * @param model
	 * @param type
	 * @return
	 */
	public static Element getValue(JSONObject value , Record record , PdfWriter writer , Font font) {

		String type = record.getType();
		String model = record.getModel();
		 
		if(type.equals("text")) {
			return getText( record.getLabel() ,font)   ;
		}else if(type.equals("uploadImg") || type.equals("uploadFile") ) {

		} else {
			
			String label = value.getString(model);
			
			// 文本
			String labelKey = model + "_label";
			if(value.containsKey(labelKey)) {
				label = value.getString(labelKey);
			}
			
			// 判断有没有append
			String append = record.getOptions() != null ? record.getOptions().getAppend() : null;
			if(StringUtils.isNotBlank(append)) {

				if(append.contains("$")) {
					Object appendValue = FormUtil.getScirptValue(append, value);
					if(appendValue != null) {
						append = appendValue.toString();
					}
				}

				label += " " + append;
			}
			
			// 判断有没有前缀
			String prepend = record.getOptions() != null ? record.getOptions().getPrepend() : null;
			if(StringUtils.isNotBlank(prepend)) {

				if(prepend.contains("$")) {
					Object prependValue = FormUtil.getScirptValue(prepend, value);
					if(prependValue != null) {
						prepend = prependValue.toString();
					}
				}

				label = prepend + " " + label;
			}
			
			if(StringUtils.isBlank(label)) {
				label = "" ;
			}
			
			return getText( label,font);
			
		}
		 

		return null;
	}
	
	/**
	 * 从json中解析出当前record的值
	 * @param value
	 * @param model
	 * @param type
	 * @return
	 */
	public static Element getValue(JSONObject value , Record record , PdfWriter writer ) {
 
		return getValue(value, record, writer , getDefaultFont());
	}
	
	public static PdfItem getPdfItem(JSONObject item) {
		
		return null;
	}
	
	public static Phrase getText(String text) { 
		return getText(text  , getDefaultFont());
	}
	
	public static Phrase getText(String text,Font font) {
		//Font font = new Font(bfChinese, 10.5f, Font.NORMAL);
		if(text == null || text.trim().equals("undefined")) {
			text = "" ;
		}
		return new Phrase(text.trim() , font);
	}
	
	

	public static Map<String, String> parseCss(String css) {

		if(StringUtils.isBlank(css)) {
			return Collections.emptyMap();
		}

		Map<String, String>  map = new HashMap<>();

		String[] cs = css.split(";");
		for(String c : cs) {
			if(StringUtils.isBlank(c)) continue ;

			String[] kv = c.split(":");
			if(kv == null || kv.length < 2) continue ;
			map.put(kv[0], kv[1]);

		}

		return map ;
	}
}
