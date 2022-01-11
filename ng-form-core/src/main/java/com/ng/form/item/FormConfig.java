package com.ng.form.item;

import lombok.Data;

/**
 * 表单配置项
 * @author lyf
 *
 */
@Data
public class FormConfig {
	/*
	 * "labelPosition": "left", "labelWidth": 100, "size": "default",
	 * "outputHidden": true, "hideRequiredMark": true, "customStyle": ""
	 */
	/**
	 * 表单内form-item的label位置 left,right,center
	 */
	private String labelPosition ;
	
	/**
	 * 表达那内form-item的label宽度
	 */
	private Integer labelWidth ;
	
	
}
