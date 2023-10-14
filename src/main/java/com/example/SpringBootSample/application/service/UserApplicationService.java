package com.example.SpringBootSample.application.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {
	
	@Autowired
	MessageSource massageSource;
	
	/** 性別のMapを生成する。 */
	public Map<String, Integer> getGenderMap(Locale locale) {
		Map<String, Integer> genderMap = new HashMap<>();
		// getMessage( キー 名, 埋め込み パラメーター, ロ ケール)
		String male = massageSource.getMessage("male", null, locale);
		String female = massageSource.getMessage("female", null, locale);
		genderMap.put(male, 1);
		genderMap.put(female, 2);
		return genderMap;
	}
}
