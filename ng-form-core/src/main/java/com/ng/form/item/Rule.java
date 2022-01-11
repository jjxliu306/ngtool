package com.ng.form.item;

import lombok.Data;

@Data
public class Rule {

	private String message ;
	
	private Boolean required ;
	
	/**
	 * 验证类型 = 1（正则） =2（函数）
	 */
	private Integer vtype ;
	
	/**
	 * 函数验证的脚本 vtype = 2
	 */
	private String script ;
	
	/**
	 * 正则验证  vtype=2
	 */
	private String pattern ;
	
}
