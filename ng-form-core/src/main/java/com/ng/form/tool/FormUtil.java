package com.ng.form.tool;
 

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ng.form.item.Options;
import com.ng.form.item.Record;
import com.ng.form.item.Rule; 

public class FormUtil {

	static Logger  logger = LoggerFactory.getLogger(FormUtil.class.getName());
 

	/**
	 * record是否需要隐藏
	 * @param r
	 * @param value
	 * @return
	 */
	public static boolean recordVisible(Record r , JSONObject formValue) {

		if(r.getOptions() == null) return true ;

		Options option = r.getOptions();
		return recordVisible(option, formValue);

	}

	/**
	 * record是否需要隐藏
	 * @param r
	 * @param value
	 * @return 返回没有匹配通过的rule
	 */
	public static Rule recordRuleValidator(Record record , JSONObject value) {

		if(record.getRules() == null || record.getRules().isEmpty()) return null ;

		List<Rule> rules = record.getRules() ;

		Object recordValue = value.get(record.getModel());


		// 2021-05-15 lyf 办理地址办理时间特殊校验 
		for(Rule r : rules) { 
			// 判断是否有验证
			Integer vtype = r.getVtype();

			//2021-04-20 如果存在vtype 则不校验是否必填
			if(vtype == null) {
				if(r.getRequired() != null && r.getRequired() == true) {
					// 必填
					if(recordValue == null || recordValue.equals("") || recordValue.toString().equals("[]")) {
						return r;
					}
				}
			}


			if(vtype == null) continue ;

			// =1 正则
			if(vtype == 1) {
				if(StringUtils.isBlank(r.getPattern())) continue ;

				if(recordValue == null) return r ;

				Pattern p = Pattern.compile(r.getPattern().trim());

				if(!p.matcher(recordValue.toString().trim()).find()) return r ;

				// 正则匹配
				//if(!PatternMatchUtils.simpleMatch(r.getPattern(), recordValue.toString())) return false;
				//if(!recordValue.toString().trim().matches()) return r;



			} else if(vtype == 2) {
				// 表达式
				if(StringUtils.isBlank(r.getScript())) continue ;
				boolean validator = getVisible(r.getScript(), value);
				if(!validator) return r;
			}
 
		} 
		return null ; 
	}

	/**
	 * record是否需要隐藏
	 * @param r
	 * @param value
	 * @return
	 */
	public static boolean recordVisible(Options option , JSONObject formValue) {
 
		// 直接隐藏
		if(option.getHidden() != null && option.getHidden() == true) return false;

		// 动态显隐没有配置 或者配置false
		if(option.getDynamicVisible() == null || option.getDynamicVisible() == false) return true ;


		String script = option.getDynamicVisibleValue();
		if(StringUtils.isBlank(script)) {
			// 2020-10-14 如果动态显示得脚本为空 则说明需要显示
			return true ;
		}

		return getVisible(script, formValue);


	}


	public static boolean getVisible(String script , Map<String, Object> formValue) {
		Object ret = getScirptValue(script, formValue);
		if(ret instanceof Boolean) {
			return (Boolean)ret;
		} else {
			return ret != null && !ret.toString().trim().equals("0") && !ret.toString().trim().equals("false");
		}
	}

	private static  ScriptEngine engine =  null;

	/*
	static {
		try {
			engine.eval("function dynamicFun(script,model,data){" +
					//" if(!script) return false ;" +
					//"  var func = script.indexOf('return') >= 0 ? '{' + script + '}' : 'return (' + script + ')' ; " +
					" var Fn = new Function('$', 'data', script) ;" +
					"  return  Fn(model , data);" +
					"}");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	public static ScriptEngine getScriptEngine() {
		if(engine == null) {

			ScriptEngineManager manager = new ScriptEngineManager();
			engine =  manager.getEngineByName("javascript"); 
			try {
				engine.eval("function dynamicFun(script,model,data){" +
						//" if(!script) return false ;" +
						//"  var func = script.indexOf('return') >= 0 ? '{' + script + '}' : 'return (' + script + ')' ; " +
						" var Fn = new Function('$', 'data', script) ;" +
						"  return  Fn(model , data);" +
						"}");
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		 
		return engine;
			 
	}

	public static Object getScirptValue(String script , Map<String, Object> value) {
		// 配置了动态显隐,则根据表达式判断当前是否需要显示


		if(StringUtils.isBlank(script)) {
			return false ;
		}

		if(script.contains("return")) {
			script = "{" + script + "}";
		} else {
			script = "return (" + script + ")" ;
		}


		if(script.contains("includes")) {
			script = script.replace("includes", "contains");
		}
 
		 ScriptEngine engine = getScriptEngine();
		try{

			 if (engine instanceof Invocable) {
				 Invocable in = (Invocable) engine;
		 
				Object ret = in.invokeFunction("dynamicFun",script,value) ;
				//System.out.println("script:" + script + " , ret: " + ret);
				return ret ;
			 }
		}catch(Exception e){
			//System.out.println("id:" + (itemData != null ? itemData.getString("id") + " ; " + itemData.getString("name") : "")+ ",script:" + script);
			//e.printStackTrace()
			 
			
			logger.error("script handler error.  script:[{}]" ,   script);
			logger.error(e.toString());

		}

		return null;
	//	return true ;
	}
 


	public static void main(String[] args) {
//		String css = "width:10%;background:#F2F7FE;font-size:14px;font-weight:bolder;";
//
//		Map<String, String> map = parseCss(css);
//
//		System.out.println(map);

//
//		String url = " http://zwfw.shaanxi.gov.cn/icity/icity/proinfo?id=fc5f0ddc-c200-4873-97d8-0d477aaf8bdf" ;
//		String pattern = "([hH][tT]{2}[pP]:\\/\\/|[hH][tT]{2}[pP][sS]:\\/\\/)(([A-Za-z0-9-~]+)\\.)+([A-Za-z0-9-~\\/])+" ;
//
//
//		Pattern p = Pattern.compile(pattern);
//
//		System.out.println(p.matcher(url).find());
//
//		System.out.println(url.toString().trim().matches(pattern.trim()));


		String script =
				"var reg1 = /^((\\d{3}-\\d{8}|\\d{4}-\\d{7})(-\\d{1,4})?)$/\r\n" +
				"var reg2 = /^1[34578][0-9]{9}$/\r\n" +
				"if(!$.A099){\r\n" +
				"  return true;\r\n" +
				"}\r\n" +
				"if(reg1.test($.A099)|| reg2.test($.A099)){\r\n" +
				"   return true;\r\n" +
				"}else{\r\n" +
				"  return false;\r\n" +
				"}"
				 ;
		JSONObject model = JSON.parseObject("{\"A099\":\"\"}");


		Object ret = getScirptValue(script, model);

		System.out.println("ddd:" + ret);

	}

 

}
