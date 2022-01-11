package com.ng.form.validator.entity;

import java.util.List;

import lombok.Data;

/**
 * batch验证结果
 * @author lyf
 *
 */
@Data
public class BatchListValidator {

	private Integer index ;
	
	private Boolean result ;
	
	List<FieldValidator> child;
	
}
