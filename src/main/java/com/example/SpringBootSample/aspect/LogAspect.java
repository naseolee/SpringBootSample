package com.example.SpringBootSample.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

// AspectとComponentのアノテーションを両方つける
@Aspect
@Component
@Slf4j
public class LogAspect {
	
	/** * サービスの実行前にログ出力する.
	 * 対象:[UserService]をクラス名に含んでいる. 
	 */ 
	// Joinpointのアノテーション指定
	@Before("execution(* *..*.*UserService.*(..))")
	public void startLog(JoinPoint jp) {
		log.info("メソッド開始：" + jp.getSignature());
	}

	
	/** * サービスの実行後にログ出力する.
	 * 対象:[UserService]をクラス名に含んでいる. 
	 */
	// Joinpointのアノテーション指定　executionは351p
	// execution(<return_type> <package>.<class>.<method>(<parameters>))
	@After("execution(* *..*.*UserService.*(..))")
	public void endLog(JoinPoint jp) {
		log.info("メソッド終了：" + jp.getSignature());
	}
	
	/** コントローラーの実行前後にログ出力する */
	// beanはDIコンテナーに登録されているbean名を指定する。正規表現使用可能、下は最後にControllerがついているbean(クラス)が対象になる
	// @Around("bean(*Controller)")
	// @annotationは指定したアノテーションがついているメソッドが対象になる
	// @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	// withinは指定したアノテーションがついているクラスの全てのメソッドが対象になる(パッケージ名を含める)
	@Around("within(org.springframework.stereotype.Controller)")
	public Object startLog(ProceedingJoinPoint jp) throws Throwable {
		log.info("メソッド開始： " + jp.getSignature());
		
		try {
			// ★メソッド実行(これがないと対象のメソッドが実行されないので注意！！！)
			Object result = jp.proceed();
			
			// 終了ログ出力
			log.info("メソッド終了： " + jp.getSignature());
			
			// 実行結果を呼び出し元に返却
			return result;
		} catch (Exception e) {
			
			// エラーログ出力
			log.error("メソッド異常終了：" + jp.getSignature(), e);
			
			//　エラー再スロー
			throw e;
		}
		
	}
	
 
 
}
