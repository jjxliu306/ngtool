package com.ng.form.pdf.item;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.ng.form.item.Record;

/**
 * 基础组件
 * input,textarea,radio 等等
 * @author lyf
 *
 */
public class BaseItem extends Phrase implements PdfItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3764331204932898794L;

	@Override
	public Element reder(Record record) {
		// TODO Auto-generated method stub
		return null;
	}

}
