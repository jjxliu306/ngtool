package com.ng.pdf.item.typehandler;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.text.pdf.PdfDiv.FloatType;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.ng.form.item.FormConfig;
import com.ng.form.item.Options;
import com.ng.form.item.Record;
import com.ng.form.tool.FormUtil;
import com.ng.pdf.item.NgItem;
import com.ng.pdf.tool.PdfUtils;

/**
 * 基础要素 包含  'input',
          'textarea',
          'date',
          'time',
          'datePicker',
          'number',
          'radio',
          'checkbox',
          'select',
          'rate',
          'switch',
          'slider', 
          'cascader' 
 * @author lyf
 *
 */
public class BaseItem extends Phrase implements NgItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3764331204932898794L;

	
	
	@Override
	public Element reder(FormConfig formConfig , JSONObject formValue ,Record record) {
		 
		// 2020-10-14 lyf 判断是否需要显示
		
		boolean visible = FormUtil.recordVisible(record, formValue);
		if(!visible) {
			return null;
		}
		
		// 判断是否有label的值
		String model = record.getModel();
		String labelModel = model + "_label";
		
		String modelValue = "" ;
		if(formValue.containsKey(labelModel)) {
			modelValue = formValue.getString(labelModel);
		} else {
			modelValue = formValue.getString(model);
		}
		
		// 判断有无前缀和后缀
		
		// 判断有没有prepend
		String prepend = record.getOptions() != null ? record.getOptions().getPrepend() : null;
		if(StringUtils.isNotBlank(prepend)) {

			if(prepend.contains("$")) {
				Object prependValue = FormUtil.getScirptValue(prepend, formValue);
				if(prependValue != null) {
					prepend = prependValue.toString();
				}
			}

			modelValue =  prepend + " " + modelValue;
		}
		
		// 判断有没有append
		String append = record.getOptions() != null ? record.getOptions().getAppend() : null;
		if(StringUtils.isNotBlank(append)) {

			if(append.contains("$")) {
				Object appendValue = FormUtil.getScirptValue(append, formValue);
				if(appendValue != null) {
					append = appendValue.toString();
				}
			}

			modelValue += " " + append;
		}
		
		 
		
		// 要素值
		Phrase  modelElement = PdfUtils.getText(modelValue);
		
		
		
		// 判断要素label是否需要显示 formConfig中宽度>0 
		if(formConfig != null && formConfig.getLabelWidth() > 0) {
			String labelPosition = formConfig.getLabelPosition();
			Integer labelWidth = formConfig.getLabelWidth();
			
			String label = record.getLabel();
			
			Phrase  labelElement = PdfUtils.getText(label);
			 
		  
			PdfDiv pdiv = new PdfDiv();
			//pdiv.setWidth(labelWidth * 1.0f);
			pdiv.addElement(labelElement);
			pdiv.setFloatType(FloatType.LEFT);
			pdiv.addElement(modelElement);
			pdiv.setTextAlignment(ALIGN_BASELINE);
			
			return pdiv;
			
//			// 构造一个小的table 两列 一个是label 一个是值 
//			PdfPTable ptable = new PdfPTable(2);
//			float width = ptable.getTotalWidth();
//			try {
//				ptable.setWidths(new int[] {labelWidth , 0});
//			} catch (DocumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			ptable.setSkipFirstHeader(true); 
//			 
//			
//			System.out.println("tatol width: " + width);
//				
//			PdfPCell labelP = new PdfPCell(labelElement);
//			 
//			if(StringUtils.equals(labelPosition, "left")) {
//				labelP.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
//			} else if (StringUtils.equals(labelPosition, "center")) {
//				labelP.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//			}  else if (StringUtils.equals(labelPosition, "right")) {
//				labelP.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//			} 
//		
//			labelP.setVerticalAlignment(Element.ALIGN_MIDDLE );
//			 
//			
//			ptable.addCell(labelP);
//			
//			
//			ptable.addCell(modelElement);
//			
//			 
//			return ptable ;
 		}  else {
 			return modelElement ;
 		}
		
	 
	}

	@Override
	public String[] getType() {
		// TODO Auto-generated method stub
		return new String[]{"input" , "textarea" , "date" , "time" , "datePicker" , "number" , "radio" , "checkbox" , "select" , "rate" , 
				"switch" , "slider" , "cascader"};
	}

}
