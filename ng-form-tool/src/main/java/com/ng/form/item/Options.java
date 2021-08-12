package com.ng.form.item;

import lombok.Data;

/**
 * record中配置信息
 * @author lyf
 *
 */
@Data
public class Options {
	/*disabled: false, //是否禁用
    hidden: false, // 是否隐藏，false显示，true隐藏
    defaultValue: [], 
    dynamic: 0,
    tooptip: '', // 提示
    
    
*/

	private Boolean disabled;
	private Boolean hidden;
	
	// type=date 范围日期选择，为true则会显示两个时间选择框（同时defaultValue和placeholder要改成数组），
	private Boolean range;
	// type=date,time
	private String format ;
	
	// type =fileupload,uploadImg
	private Boolean  multiple;
	
	// type=batch 动态表格
	private Boolean showLabel ;
	
	// type=batch 动态表格
	private Boolean hideSequence;
	
	/**
	 * 2021-05-28 lyf 是否强制依赖回显
	 */
	private Boolean relyCbColumn;
	
	/**
	 * 动态显示隐藏
	 */
	private Boolean dynamicVisible;
	
	/**
	 * 动态显示隐藏条件
	 */
	private String dynamicVisibleValue ;
	
	/**
	 * 数据来源 针对 select radio等
	 *  = 2 的时候标识数据字典
	 */
	private Integer dynamic ;
	
	/**
	 * 数据字典key
	 */
	private String dictType ;
	
	/**
	 * 从实体中回填的字段
	 */
	private String cbColumn;
	
	/**
	 * 追加单位信息
	 */
	private String append;
	
	// 其他值忽略  这里只需要展示的字段
	
	
	
}
