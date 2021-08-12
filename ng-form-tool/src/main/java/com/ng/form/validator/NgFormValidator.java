package com.ng.form.validator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ng.form.exception.NgFormNullException;
import com.ng.form.item.Options;
import com.ng.form.item.Record;
import com.ng.form.item.Rule;
import com.ng.form.tool.FormUtil;
import com.ng.form.validator.entity.BatchListValidator;
import com.ng.form.validator.entity.FieldValidator;
import com.ng.form.validator.entity.FormValidator;

/**
 * ng-form表单校验
 * @author lyf
 *
 */
public class NgFormValidator {
	
	/**
	 * 表单校验
	 * @param formTemplate 表单模板json
	 * @param formValue 表单填写数据
	 * @return
	 */
	public FormValidator validator(String formTemplate , String formValue) {
		
		if(StringUtils.isBlank(formTemplate)) {
			throw new NgFormNullException("form template is null");
		}
		
		if(StringUtils.isBlank(formValue)) {
			throw new NgFormNullException("form value is null");
		}
		
		JSONObject form = JSON.parseObject(formTemplate);
		JSONObject value = JSON.parseObject(formValue);
		
		return validator(form, value);
		
	}
	
	
	/**
	 * 表单校验
	 * @param formTemplate 表单模板json
	 * @param formValue 表单填写数据
	 * @return
	 */
	public FormValidator validator(JSONObject formTemplate , JSONObject formValue) {
		
		List<Record> records = parseModel(formTemplate);
		
		FormValidator formv = new FormValidator();
		formv.setResult(true);
		
		if(records == null || records.isEmpty()) return formv ;
		

		List<FieldValidator> fieldValidaotrs = new LinkedList<>(); 
		
		// 循环每个字段 
		for(Record r : records) {
	
			FieldValidator fv = validateFields(r, formValue);
			
			if(fv != null) {
				fieldValidaotrs.add(fv);
			}
			  
			 
		}
		//如果表单验证成功则返回null 不输出
		if(!fieldValidaotrs.isEmpty()) {
			 
			
			formv.setResult(false);
			formv.setFailField(fieldValidaotrs); 
			
		}
		
		return formv;
		
	}


	/**
	 * 验证record
	 * @param records
	 * @param value
	 */
	private  FieldValidator  validateFields(Record record  , JSONObject value) {
		if(record == null ) return null;
	 
		// 判断是否有parent  并且parent是否隐藏
		if(record.getParantOptions() != null) {
			// 如果父级隐藏了 直接返回
			boolean parentVisible =FormUtil.recordVisible(record.getParantOptions(), value);
			if(!parentVisible) return null ;
		}

		// 判断是否隐藏
		boolean visible = FormUtil.recordVisible(record, value);
		if(!visible) return null ;
		
		// 是否存在规则 没有规则校验则忽略校验
		if(record.getRules() == null || record.getRules().isEmpty()) return null ;

		FieldValidator fv  = new FieldValidator();
		
		fv.setFieldName(record.getLabel());
		fv.setModel(record.getModel());
		 
		// 校验规则
		Rule ruleValidaor = FormUtil.recordRuleValidator(record, value);
		 
		fv.setResult(ruleValidaor == null);
		fv.setRule(ruleValidaor);
		if(ruleValidaor != null ) {
			fv.setMessage(ruleValidaor.getMessage());
			fv.setType(record.getType());
		}

		if(record.getType().equals("batch")) {
			
			
			// 取出来数据
			JSONArray batchValue = value.getJSONArray(record.getModel());
			if(batchValue == null) {
				// 如果结果验证通过返回null
				if(fv.getResult() == true) return null ;
				return fv ;
			}
			
			// 动态表格中每条数据得验证结果
			List<BatchListValidator> batchVlidatorList = new LinkedList<>();
						
			List<Record> childReocrd = record.getChild();
			
			int batchSize = batchValue.size();
			for(int j = 0 ; j < batchSize ; j++) {
				List<FieldValidator> batchValidate = new ArrayList<>();
				BatchListValidator batchValidator = new BatchListValidator();
				
				JSONObject batchSubValue = batchValue.getJSONObject(j);
				for(Record cr : childReocrd) {
					FieldValidator vali = validateFields(cr, batchSubValue);
				
					if(vali != null) {
						batchValidate.add(vali);
					}
				
				}
				
				if(!batchValidate.isEmpty()) {
					batchValidator.setIndex(j);
					batchValidator.setResult(false);
					batchValidator.setChild(batchValidate);
					
					batchVlidatorList.add(batchValidator);
				}
				
			}
			
			if(!batchVlidatorList.isEmpty()) {
				// 验证不通过 
				fv.setChild(batchVlidatorList); 
				fv.setResult(false);  
			}
			
		}  
		
		// 如果验证通过并且没有子child 则返回null
		if((fv == null || fv.getResult() == true)  && (fv.getChild() == null || fv.getChild().isEmpty()) ) {
			return null ;
		}
		
		return fv;
	}
	
 
	
	/**
	 * 从模板中解析出所有的校验字段
	 * @param template
	 * @return
	 */
	private   List<Record> parseModel(JSONObject jo) {
		
		List<Record> list = new ArrayList<>();
		
		iterModel(list, jo);
		
		return list ;
		
	}
	
	
	private   void iterModel(List<Record> list , Object jo) {
		//System.out.println("jo class: " + jo.getClass().getSimpleName());
		if(jo instanceof JSONArray) {
			JSONArray joArray = (JSONArray) jo;
			
			int size = joArray.size();
			for(int i = 0 ; i < size ; i++) {
				Object json = joArray.get(i);
				
				iterModel(list, json);
			}
			
		} else if(jo instanceof JSONObject) {
			JSONObject jobject = (JSONObject) jo;
			
			
			
			if(jobject.containsKey("model") && jobject.containsKey("key") && jobject.getString("model").equals(jobject.getString("key"))) {
				
				Record r = jobject.toJavaObject(Record.class);
				 
				String type = r.getType();
				if(type.equals("batch")) {
					JSONArray batchList = jobject.getJSONArray("list");
					List<Record> childList = batchList.toJavaList( Record.class);
					 
					r.setChild(childList);
				} 
			
				list.add(r);
			} else if(jobject.containsKey("type") && jobject.getString("type").equals("table")) {
				 
				Record r = jobject.toJavaObject(Record.class);
				
				// 判断动态显隐是否有值
				Options parentOptions = r.getOptions();
				
				// 构建空record 集合 回填table下所有组件
				List<Record> tableChildList = new LinkedList<>();
				iterModel(tableChildList, jobject.getJSONArray("trs"));
				
				if(tableChildList != null && !tableChildList.isEmpty()) {
					tableChildList.stream().forEach(t->t.setParantOptions(parentOptions));
					
					list.addAll(tableChildList);
				} 
				 
			} else {
				
				Set<String> keys = jobject.keySet();
				for(String key : keys) {
					Object obj = jobject.get(key);
					
					iterModel(list, obj);
				}
				
			}
			
		}
		
	}
}
