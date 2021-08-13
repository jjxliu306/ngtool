package form;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;
import com.ng.form.item.Record;
import com.ng.form.pdf.item.batch.BatchTable;
import com.ng.form.pdf.item.table.BaseTable;
import com.ng.form.pdf.item.table.Tr;
import com.ng.form.pdf.utils.PdfUtils;
import com.ng.form.tool.FormUtil; 

/**
 * form中基础元素
 * @author lyf
 *
 */
public class FormItem {

	private JSONObject template ; 
	
	private PdfWriter writer ;
	
	public FormItem(JSONObject template ,PdfWriter writer) {
		// TODO Auto-generated constructor stub
		this.template = template;
		this.writer = writer;
	}
	
 
	
	@SuppressWarnings("static-access")
	public Element initData(JSONObject value , String name) {
		
		String type = template.getString("type");
		 
		if(type.equals("table")) {
			// 表格布局
			// 判断表格是否有动态显隐
			if(template.containsKey("options")) {
				Record r = template.toJavaObject(Record.class);
				
				boolean visible = FormUtil.recordVisible(r, value);
				if(!visible) {
					return null;
				}
				
			}
			
			List<Tr> trs = template.parseArray(template.getString("trs"), Tr.class);
			
			BaseTable btable = new BaseTable(trs);
			btable.setName(name);
			 
			btable.initData(value,writer);
			
			return btable ;
			
		} else if(type.equals("grid")) {
			// 栅格布局
			return null;
		} else if(type.equals("batch")) {
			// 动态表单
			// 判断动态表单是否有动态显隐
			if(template.containsKey("options")) {
				Record r = template.toJavaObject(Record.class);

				boolean visible = FormUtil.recordVisible(r, value);
				if(!visible) {
					return null;
				}

			}
			
			List<Record> list = template.parseArray(template.getString("list"), Record.class);
			
		
			String model = template.getString("model");
			JSONArray batchValue = null;
			
			// 2020-10-14 如果value中没有此model得值,而且name为空(表明为一个大项中小项) 则跳过返回null
			if(!value.containsKey(model) && name == null) {
				return null;
			}
			
			// 没有值也要带一个空表头进去
			if(value.containsKey(model)) {
				batchValue = value.getJSONArray(model); 
			} 
			
			// 获取showItem
			List<String> showItems = new ArrayList<>();
			if(template.containsKey("options")) {
				JSONObject options = template.getJSONObject("options");
				if(options.containsKey("showItem")) {
					JSONArray hitems = options.getJSONArray("showItem");
					
					showItems = hitems.toJavaList(String.class);
					
				}
			}
			
			BatchTable btable = new BatchTable(list ,writer, batchValue,name , showItems);
			 
			return btable ;
			
			
		}  
		
		else {
			
			Record r = template.toJavaObject(Record.class);
			
			// 没有布局 普通要素
			return PdfUtils.getValue(value, r , writer);
			
			
		}
		
	}
	
}
