package com.ng.form.validator.entity;

import java.util.List;

import lombok.Data;

/**
 * 事项验证结果
 * @author lyf
 *
 */
@Data
public class FormValidator {
	 
	
	
	/**
	 * 结果
	 */
	private Boolean result ;
	 
	/**
	 * 异常字段
	 */
	private List<FieldValidator> failField;
	
}
