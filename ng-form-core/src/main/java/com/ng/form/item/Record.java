package com.ng.form.item;

import java.util.List;

import lombok.Data;

/**
 * 一个基本要素数据
 * @author lyf
 *
 */
@Data
public class Record {

	/*type: "input", // 表单类型
    label: "输入框", // 标题文字 
    index: 'A',
    options: {
      type: "text",
      width: "100%", // 宽度
      defaultValue: "", // 默认值
      placeholder: "请输入", // 没有输入时，提示文字
      clearable: false,
      maxLength: null,
      prepend: '', // 前缀
      append: '', // 后缀
      tooptip: '', // 提示
      hidden: false, // 是否隐藏，false显示，true隐藏
      disabled: false // 是否禁用，false不禁用，true禁用
    },
    model: "", // 数据字段
    key: "",
    rules: [
      //验证规则
      {
        required: false, // 必须填写
        message: "必填项",
        trigger: ['change','blur']
      }
    ]*/
	
	
	private String type;
	private String label;
	private String model;
	private String key;
	private Options options ;
	private List<Rule> rules ;
	
	/**
	 *  2020-12-01 如果type=batch 这里会有子表单
	 */
	private List<Record> child ;
	
	/**
	 *  2020-12-01 父级得options配置 主要针对table上是否设置了动态显隐
	 */
	private Options parantOptions;
	
	@Override
	public int hashCode() {
		return model.hashCode();
		
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Record) {
			Record r = (Record)o;
			
			return r.getModel().equals(getModel());
		}
		return false;
		
	}
	
}
