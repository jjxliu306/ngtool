package com.ng.form.script;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class Test {

	public static void main(String[] args) {
		ScriptEngineManager manager = new ScriptEngineManager();

		List<ScriptEngineFactory> factories = manager.getEngineFactories();
		// 这是Java SE 5 和Java SE 6的新For语句语法
		System.out.println("###");
		
		System.out.println(manager.getEngineByName("javascript"));
		
		for (ScriptEngineFactory factory: factories){
			// 打印脚本信息

			System.out.printf("Name: %s%n" +
					"Version: %s%n" +
					"Language name: %s%n" +
					"Language version: %s%n" +
					"Extensions: %s%n" +
					"Mime types: %s%n" +
					"Names: %s%n",
					factory.getEngineName(),
					factory.getEngineVersion(),
					factory.getLanguageName(),
					factory.getLanguageVersion(),
					factory.getExtensions(),
					factory.getMimeTypes(),
					factory.getNames());
			// 得到当前的脚本引擎
 
		} 

	}

}
