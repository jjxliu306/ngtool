package form;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.ng.form.item.Options;
import com.ng.form.pdf.item.table.BaseTable;
import com.ng.form.tool.FormUtil; 

public class BaseForm {

	
	private JSONObject template;
	
	private JSONObject value ;
	
	private PdfWriter writer ;
	
	public BaseForm(JSONObject template , JSONObject value,PdfWriter writer) {
		// TODO Auto-generated constructor stub
		this.template = template;
		this.value = value;
		this.writer = writer;
		
		this.htmlModelToParagraph();
	}
	 
	
	
	public Paragraph htmlModelToParagraph() {
		
		// 2020-10-14 lyf 判断该整个page是否需要显示
		if(template.containsKey("config")) {
			Options htmlConfig = template.getObject("config", Options.class);
			
			boolean visisble = FormUtil.recordVisible(htmlConfig, value);
			
			if(!visisble) {
				return null;
			}
			 
		}
		
		
		if(template.containsKey("list")) {
			// 取到list
			JSONArray list = template.getJSONArray("list");
			
			int listSize = list.size();
			
			Paragraph p = new Paragraph();
			
			for(int j = 0 ; j < listSize ; j++) {
				JSONObject listValue = list.getJSONObject(j);
				
				FormItem item = new FormItem(listValue,writer);
				
			 
				Element element = item.initData(value);
				p.add(element);
			
			}
			
			if(listSize == 0) {
				BaseTable btable = new BaseTable();
		  
				p.add(btable);
			}

			return p ;
		 
			
		}
		
		return null;
	}
	
}
