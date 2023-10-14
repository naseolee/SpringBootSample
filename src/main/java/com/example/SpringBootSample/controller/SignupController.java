package com.example.SpringBootSample.controller;

import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SpringBootSample.form.GroupOrder;
import com.example.SpringBootSample.form.SignupForm;
import com.example.SpringBootSample.application.service.UserApplicationService;
import com.example.SpringBootSample.domain.user.model.MUser;
import com.example.SpringBootSample.domain.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class SignupController {
	
	@Autowired
	private UserApplicationService userApplicationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/** ユーザー登録画面を表示 */
	@GetMapping("/signup")
	public String getSignup(Model model, Locale locale, @ModelAttribute SignupForm form) {
		// 性別を取得
		Map<String, Integer> genderMap = userApplicationService.getGenderMap(locale);
		model.addAttribute("genders", genderMap);
		// ユーザー登録画面に遷移
		return "user/signup";
	}
	
	// @Validatedアノテーションをクラスに付けると、バリデーションが実行されます。
	// 本には順番が逆になっているけど、現状実装のアノテーション順番じゃないとうまくいかないみたい
	/** ユーザー登録処理 */
	@PostMapping("/signup")
	public String postSignup(Model model, Locale locale, @Validated(GroupOrder.class) @ModelAttribute SignupForm form, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			// NGの場合、戻る
			return getSignup(model, locale, form);
		}
		
		log.info(form.toString());
		
		// formをMUserクラスに変換
		// コピー元とコピー先のフィールド名が一致する必要がある
		MUser user = modelMapper.map(form, MUser.class);
		
		userService.signup(user);
		
		// ログイン画面にリダイレクト(PRGpattern[Post-Redirect-Get])
		return "redirect:/login";
	}
	
	
	// ExceptionHandlerアノテーションをつけたメソッドを用意すると例外処理の実装ができる
	// 複数用意することも可能である
	/** データベース関連の例外処理 */
	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e, Model model) {
		
		// 空文字をセット
		model.addAttribute("error","");
		
		// メッセージをModelに登録
		model.addAttribute("message", "SignupControllerで例外が発生しました");
		
		// HTTPのエラーコード(500)をModelに登録
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
		
		return "error";
	}
	
	/** その他例外処理 */
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {
		// 空き文字をセット
		model.addAttribute("error","");
		
		// メッセージをModelに登録
		model.addAttribute("message", "SignupControllerで例外が発生しました");
		
		// HTTPのエラーコード(500)をModelに登録
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
		
		return "error";
	}
	 
	
	
}
