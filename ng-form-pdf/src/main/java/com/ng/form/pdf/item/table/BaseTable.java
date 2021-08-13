package com.ng.form.pdf.item.table;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ng.form.pdf.utils.PdfUtils; 

/**
 *  table
 * @author lyf
 *
 */
public class BaseTable extends PdfPTable{

	 private List<Tr> trs ;
 
	 
	 
	 public List<Tr> getTrs() {
		return trs;
	}
	 
	 public BaseTable(List<Tr> trs) {
		 this.trs = trs;
	}
	 
	public void initData(JSONObject value,PdfWriter writer) {
		 
		//查看到底几个col
		int cols = 0;
		if(trs != null && !trs.isEmpty()) {
			List<Td> tds = trs.get(0).getTds();
			for(Td t : tds) {
				cols += t.getColspan();
			}
		}
		if(cols == 0) {
			cols = 1;
		}
		resetColumnCount(cols);
	 
		if(cols > 1) {
			float[] ws = new float[cols];
			if(cols == 2) {
				ws = new float[]{0.15f , 0.85f};
			}else if(cols % 2 == 1) {
				// 奇数个
				ws[0] = 0.1f;
				
				float w = (float) (0.9 / cols) ;
				for(int i = 1 ; i < cols ; i++) {
					ws[i] = ((float) (i % 2 == 1 ? w - 0.05 : w + 0.05));
				}
			} else {
				float w = (float) (1.0 / cols) ;
				for(int i = 0 ; i < cols ; i++) {
					ws[i] = ((float) (i % 2 == 0 ? w - 0.1 : w + 0.1));
				}
			}
			
			 
			
			try {
				setWidths(ws);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	 
		if(trs != null)
		for(Tr tr : trs) {
			
			List<Td> tds = tr.getTds();
			
			// 判断每个td是不是都隐藏
			boolean hidden = true ;
			for(Td t : tds) {
				boolean tv = t.isVisible(value);
				 
				if(tv) {
					hidden = false;
					break;
				}
			}
			
			if(hidden) {
				// 整个tr隐藏
				continue ;
			}
 			
			for(Td td : tds) {
				  
				boolean visible = td.isVisible(value);
				if(visible) {
					if(td.getStyle() != null) { 
						td.setStyle(td.getStyle());
						
						// 尝试从style中解析出背景色
						Map<String, String> css = PdfUtils.parseCss(td.getStyle());
						if(css.containsKey("background")) {
							 
							td.setBackgroundColor(new BaseColor(PdfUtils.CELL_BACKGROUND));
							td.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
						}
						 
						
						//td.setBackgroundColor(BaseColor.CYAN);
						
						td.setVerticalAlignment(Element.ALIGN_MIDDLE );
					}
					td.initData(value,writer);
				
				} else {
					td.setPhrase(Phrase.getInstance(""));
				}
				
				
				addCell(td);
				 
				
			}
			
		}
		
	}
	
}
