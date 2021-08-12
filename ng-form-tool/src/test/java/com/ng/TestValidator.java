package com.ng;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.ng.form.validator.NgFormValidator;
import com.ng.form.validator.entity.FormValidator;

public class TestValidator {

	public void test1() throws IOException {
		
		String template = IOUtils.toString(getClass().getResource("/template.json") , "UTF-8");
		String value = IOUtils.toString(getClass().getResource("/value.json") , "UTF-8");
		
		NgFormValidator validator = new NgFormValidator();
		FormValidator fv = validator.validator(template, value);
		
		System.out.println(JSON.toJSONString(fv));
		
	}
	
	
	public static void main(String[] args) throws IOException {
		TestValidator test = new TestValidator();
		test.test1();
	}
}
