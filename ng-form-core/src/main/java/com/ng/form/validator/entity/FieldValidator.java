package com.ng.form.validator.entity;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.ng.form.item.Rule;

import lombok.Data;

/**
 *  字段验证结果
 * @author lyf
 *
 */
@Data
public class FieldValidator {

	/**
	 * 字段名称
	 */
	private String fieldName ;
	
	/**
	 * key
	 */
	private String model ;
	
	/**
	 * 类型
	 */
	private String type ;
	
	/**
	 * 结果
	 */
 
	@JSONField(serialize=false)
	private Boolean result ;
	
	/**
	 * 描述
	 */
	private String message ;
	
 
	@JSONField(serialize=false)
	private  Rule  rule ;
	
	
	
	/**
	 *  针对batch这种字段得下一级验证结果
	 */
	private List<BatchListValidator> child;
	
}
