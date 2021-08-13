package com.ng.form.pdf.item.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ng.form.item.Record;
import com.ng.form.pdf.utils.PdfUtils;
import com.ng.form.tool.FormUtil; 

/**
 * 动态表单
 * @author lyf
 *
 */
public class BatchTable extends PdfPTable{

	private List<Record> list ;
	
	/**
	 *  首页需要显示的元素 在这里这些字段用来占整行
	 */
	private List<String> showItem;
	
	private JSONArray value ;
	
	private String name ;
	
	private PdfWriter writer ;
	
	public void setName(String name) {
		this.name = name;
	}

	public BatchTable(List<Record> list ,PdfWriter writer, JSONArray value) {
		this(list , writer ,value , null);
		 
	}
	 
	public BatchTable(List<Record> list ,PdfWriter writer, JSONArray value , List<String> showItem) {
		 
		this.writer = writer;
		this.list = list;
		this.value = value ;
	 
		this.showItem = showItem;
		initRender();
	}
	
	public void initRender() {
		 
		resetColumnCount(4);
		try {
			setWidths(new float[] {0.15f,0.35f,0.15f,0.35f});
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(StringUtils.isNotBlank(name)) {
			
			Phrase title = PdfUtils.getText(name);
			float fontSize = 18f ;
			if(name.contains("-")) {
				fontSize = 13f ;
			}
			title.getFont().setSize(fontSize);
			
			PdfPCell titleCell = new PdfPCell(title);
			titleCell.setColspan(4);
			titleCell.setBackgroundColor(new BaseColor(PdfUtils.CELL_BACKGROUND));
			titleCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			
			addCell(titleCell);
			
		}
		
		if(value == null) {
			return ;
		}
		
		int size = value.size();
		int index = 0;
		for(int i = 0 ; i < size ; i++) {
			
			if(index % 4 != 0) {
				// 补两个位置
				addCell("");
				addCell("");
				index+=2;
			}
			
			JSONObject jvalue = value.getJSONObject(i);
			
			
			
			
			// 先展示showItem的字段 
			if(showItem != null && !showItem.isEmpty()) {
				
				for(String item : showItem) {
					
					// 找到对应的record 从list中删除 然后再这里添加
					List<Record> rs = list.stream().filter(t->t.getModel().equals(item)).collect(Collectors.toList()) ;
					
					if(rs == null || rs.isEmpty()) continue ;
					
					Record itemRecord = rs.get(0);
					
					if(itemRecord == null) continue ; 
						 
					String label = itemRecord.getLabel();
					Phrase title = PdfUtils.getText(label + " (" + (i+1) + ")");
					PdfPCell labelP = new PdfPCell(title);
					//Phrase labelP = FormUtils.getText(label + "-" + (i+1));
					
					labelP.setBackgroundColor(new BaseColor(PdfUtils.CELL_BACKGROUND));
					labelP.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					labelP.setVerticalAlignment(Element.ALIGN_MIDDLE );
					 
					
					addCell(labelP);
					
					// value
					
					Element valueP = PdfUtils.getValue(jvalue, itemRecord,writer);
					 
					PdfPCell valueCell = new PdfPCell();
					valueCell.addElement(valueP);
					valueCell.setColspan(3);
					
					addCell(valueCell);
					
					
				}

			}
			// 2021-03-25 判断是否有材料组标记
			List<Record> metaGroups = list.stream().filter(t->StringUtils.isNotBlank(t.getType()) && t.getType().equals("metaGroup")).collect(Collectors.toList());
			if(metaGroups != null && !metaGroups.isEmpty() && jvalue.containsKey("groupName")) {
				// 存在材料组标记 导出的时候先添加材料组名称 groupName
				String groupName =  jvalue.getString("groupName");

				String label = "所属材料组名称";
				Phrase title = PdfUtils.getText(label);
				PdfPCell labelP = new PdfPCell(title);
				//Phrase labelP = FormUtils.getText(label + "-" + (i+1));

				labelP.setBackgroundColor(new BaseColor(PdfUtils.CELL_BACKGROUND));
				labelP.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				labelP.setVerticalAlignment(Element.ALIGN_MIDDLE );


				addCell(labelP);

				// value

				Element valueP = PdfUtils.getText(groupName);

				PdfPCell valueCell = new PdfPCell();
				valueCell.addElement(valueP);
				valueCell.setColspan(3);

				addCell(valueCell);

			}

			for(Record r : list) {
				
				if(showItem.contains(r.getModel())) continue ;
				
				// 2021-03-25 lyf 忽略材料组标记这个组件
				if(StringUtils.isNotBlank(r.getType()) && r.getType().equals("metaGroup")) continue ;
				 
				// 判断是否有动态显示隐藏 
				boolean visible = FormUtil.recordVisible(r, jvalue);
				  
				if(!visible) {
					// 整个隐藏
					continue ;
				}
				
				
				String label = r.getLabel();
				Phrase title = PdfUtils.getText(label );
				PdfPCell labelP = new PdfPCell(title);
				//Phrase labelP = FormUtils.getText(label + "-" + (i+1));
				
				labelP.setBackgroundColor(new BaseColor(PdfUtils.CELL_BACKGROUND));
				labelP.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				labelP.setVerticalAlignment(Element.ALIGN_MIDDLE );
				 
				
				
				
				// value
				
				Element valueP = PdfUtils.getValue(jvalue, r,writer);
				PdfPCell valueCell = new PdfPCell();
				valueCell.addElement(valueP);
			 
				if(r.getType().equals("textarea") || r.getType().contains("file")) {
					valueCell.setColspan(3);
					if(index % 4 != 0) {
						// 补两个位置
						addCell("");
						addCell("");
						index+=2;
					}
					
					index+=4;
				} else {
					index+=2;
				}
					
				addCell(labelP);
				addCell(valueCell);
				
			}
		}
		
		if(index % 4 != 0) {
			// 补两个位置
			addCell("");
			addCell("");
			index+=2;
		}
	 
		
	}
	
	
}
