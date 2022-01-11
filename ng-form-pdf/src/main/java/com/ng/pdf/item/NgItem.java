package com.ng.pdf.item;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Element;
import com.ng.form.item.FormConfig;
import com.ng.form.item.Record;

public interface NgItem  extends Element{
	
	/**
	 * 支持解析的组件类型 多个用逗号分割
	 * @return
	 */
	String[] getType();

	/**
	 * 渲染 表单中一个组件 子组件根据类型产生迭代
	 * @param record
	 * @return
	 */
	Element reder(FormConfig formConfig ,JSONObject value , Record record);
	
}