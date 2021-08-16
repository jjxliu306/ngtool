package com.ng.form.pdf.item;

import com.itextpdf.text.Element;
import com.ng.form.item.Record;

public interface PdfItem extends Element{

	/**
	 * 渲染 表单中一个组件 子组件根据类型产生迭代
	 * @param record
	 * @return
	 */
	Element reder(Record record);
	
}
