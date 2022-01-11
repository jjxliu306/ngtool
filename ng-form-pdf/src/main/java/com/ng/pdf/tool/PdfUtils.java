package com.ng.pdf.tool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;

public class PdfUtils {

	
	public static final Integer CELL_BACKGROUND = Integer.valueOf("a9c8ea" , 16);

	
	private static Font defaultFont = new Font();
	
	private static Font getDefaultFont() {
		 
		return defaultFont;
	}
	 
	
	public static void setFont(Font font) {
		defaultFont = font ;
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
