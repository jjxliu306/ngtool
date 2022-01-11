package com.ng.pdf.item;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.ng.form.item.FormConfig;
import com.ng.form.item.Record;
import com.ng.form.tool.PackageUtil;
import com.ng.pdf.item.typehandler.BaseItem;

import lombok.extern.slf4j.Slf4j;

/**
 * 表单要素转pdf要素
 * @author lyf
 *
 */
@Slf4j
public class FormItem extends Phrase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4618108757857670913L;
 
	public Element reder(FormConfig formConfig ,JSONObject value ,Record record) {
		
		// 判断类型 
		String type = record.getType();
		
		//根据类型找到对应的解析类 进行解析
		NgItem item = getHandler(type);
		
		if(item == null) {
			log.error(" pdf item type: [{}] is not typehandler." , type);
			return null ;
		}
		
		
		return item.reder(formConfig,value, record);
	}
	
	private NgItem getHandler(String type) {
		
		// 2020-02-11 修改为spring容器获取bean方式 
		
		// 从包下开始扫
		String packageName= BaseItem.class.getPackage().getName() ;
		 
		List<Class<NgItem>> handlers = PackageUtil.getClass(packageName, NgItem.class);
		 
		
		for(Class<NgItem> hanlder : handlers) {
			NgItem instance;
			try {
				instance = hanlder.newInstance();
				String htype_[] = instance.getType();
				if(htype_ == null || htype_.length == 0) continue ;
				 
				if(ArrayUtils .contains(htype_, type)) 
					return instance ; 
				
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} 
		
		return null; 
	}

}
