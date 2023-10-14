package com.example.SpringBootSample.aspect;


import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

// AspectとComponentのアノテーションを両方つける 例外発生時のaspect
@Aspect
@Component
@Slf4j
public class ErrorAspect {
	
	// 例外発生時のAOPを実装、throwing属性には例外クラスのメソッド引数を指定する。
	@AfterThrowing(value="execution(* *..*..*(..)) &&" 
			+ "(bean(*Controller) || bean(*Service) || bean(*Repository))",
			throwing="ex")
	public void throwingNull(DataAccessException ex) {
		//　例外処理の内容(ログ出力)
		log.error("DataAccessExceptionが発生しました");
	}
}
