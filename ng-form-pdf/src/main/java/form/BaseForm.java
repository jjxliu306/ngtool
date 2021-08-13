package form;

import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.ng.form.item.Options;
import com.ng.form.pdf.item.table.BaseTable;
import com.ng.form.tool.FormUtil; 

public class BaseForm {

	
	private JSONArray template;
	
	private JSONObject value ;
	
	private PdfWriter writer ;
	
	public BaseForm(JSONArray template , JSONObject value,PdfWriter writer) {
		// TODO Auto-generated constructor stub
		this.template = template;
		this.value = value;
		this.writer = writer;
	}
	
	public List<Element> getPdfParagrahp() {
		
		List<Element> parps = new LinkedList<>();
		
		// 一个htmlModel就是一章
		int size = template.size();
		
		for(int i = 0 ; i < size ; i++) {
			
			JSONObject jsonTemp = template.getJSONObject(i);
			String name = jsonTemp.getString("name");
			
//			
//			Phrase p = FormUtils.getText(name);
//			 
//			parps.add(p);
			JSONObject htmlModel = jsonTemp.getJSONObject("htmlModel");
			 
			Paragraph p = htmlModelToParagraph(name, htmlModel);
			
			if(p == null) continue ;
			
			 
			parps.add(p);
			 
			
			// 判断是否包含children 
			if(jsonTemp.containsKey("children")) {
				JSONArray children = jsonTemp.getJSONArray("children");
				int csize = children.size();
				Paragraph cp = new Paragraph();
				for(int j = 0 ; j < csize ; j++) {
					JSONObject listValue = children.getJSONObject(j);
					String cname = listValue.getString("name");
					
					JSONObject chtmlModel = listValue.getJSONObject("htmlModel");
					
					Paragraph cpj = htmlModelToParagraph(name + "-" + cname, chtmlModel);
					 
					if(cpj != null)
						cp.add(cpj);
					
				}
				
				if(cp != null) {
					p.add(cp);
				}
				
			}
			
			
			
		}
		
		
		return parps ;
	}
	
	
	private Paragraph htmlModelToParagraph(String name , JSONObject htmlModel) {
		
		// 2020-10-14 lyf 判断该整个page是否需要显示
		if(htmlModel.containsKey("config")) {
			Options htmlConfig = htmlModel.getObject("config", Options.class);
			
			boolean visisble = FormUtil.recordVisible(htmlConfig, value);
			
			if(!visisble) {
				return null;
			}
			 
		}
		
		
		if(htmlModel.containsKey("list")) {
			// 取到list
			JSONArray list = htmlModel.getJSONArray("list");
			
			int listSize = list.size();
			
			Paragraph p = new Paragraph();
			
			for(int j = 0 ; j < listSize ; j++) {
				JSONObject listValue = list.getJSONObject(j);
				
				FormItem item = new FormItem(listValue,writer);
				
				if(j > 0) {
					name = null;
				}
				Element element = item.initData(value , name);
				p.add(element);
			
			}
			
			if(listSize == 0) {
				BaseTable btable = new BaseTable(null);
				btable.setName(name);
				 
				btable.initData(null , writer);
				p.add(btable);
			}

			return p ;
		 
			
		}
		
		return null;
	}
	
}
