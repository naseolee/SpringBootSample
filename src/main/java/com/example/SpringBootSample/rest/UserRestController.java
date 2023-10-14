package com.example.SpringBootSample.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringBootSample.domain.user.model.MUser;
import com.example.SpringBootSample.domain.user.service.UserService;
import com.example.SpringBootSample.form.GroupOrder;
import com.example.SpringBootSample.form.SignupForm;
import com.example.SpringBootSample.form.UserDetailForm;
import com.example.SpringBootSample.form.UserListForm;

// RestControllerを付けるとそのクラス内のメソッドの戻り値をRESTで受け取ることができる
// 正確にはメソッドの戻り値がHTTPのレスポンスボディとして返される
@RestController
@RequestMapping("/user")
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageSource messageSource;
	
	/** ユーザー検索 */
	@GetMapping("/get/list")
	public List<MUser> getuserList(UserListForm form) {
		// formをMUserクラスに変換
		MUser user = modelMapper.map(form, MUser.class);
		
		// ユーザー一覧取得
		List<MUser> userList = userService.getUsers(user);
		
		//　これもJSONに変換される
		return userList;
	}
	
	
	/** ユーザーを登録 */
	@PostMapping("/signup/rest")
	public RestResult postSignup(@Validated(GroupOrder.class) SignupForm form, 
			BindingResult bindingResult, Locale locale) {
		// 入力チェック結果
		if (bindingResult. hasErrors()) { 
			// チェック 結果: NG 
			Map<String, String> errors = new HashMap<>();
			
			// エラーメッセージ取得
			/*
			 * バリデーション結果がNGとなったフィールドの名称は、FieldErrorクラスから取得することができる。
			 * ただし、FieldErrorから取得できるのは、単項目チェックに引っかかったフィールドだけ
			 * また、FieldErrorとLocaleをMessageSourceに渡すと、エラーメッセージを取得できる。
			 * 自作のバリデーションを作成すると相関チェックなどが行える。この場合ObjectErrorクラスにバリデーション結果が入られる
			 */
			for (FieldError error : bindingResult.getFieldErrors()) {
				String message = messageSource.getMessage(error, locale);
				errors.put(error.getField(), message);
			}
			
			//　エラー結果の返却
			return new RestResult(90, errors);
		}
		
		// formをMuserクラスに変換
		MUser user = modelMapper.map(form, MUser.class);
		
		// ユーザー登録
		userService.signup(user);

		
		/*
		 * 戻り値をJavaクラスに渡すと自動でJSONに変換され、HTTPレスポンスに設定される
		 * 
		 * */
		// 結果の返却
		return new RestResult(0, null);
		
	}
	
	/** ユーザーを更新 */ 
	@PutMapping("/update")
	public int updateUser(UserDetailForm form) {
		
		// formをMuserクラスに変換
		MUser user = modelMapper.map(form, MUser.class);
		//　ユーザーを更新
		userService.updateUserOne(user);
		
		return 0;
	}
	
	/** ユーザーを削除 */ 
	@DeleteMapping("/delete")
	public int deleteUser(UserDetailForm form) {
		
		//　ユーザーを削除
		userService.deleteUserOne(form.getUserId());
		
		return 0;
	}
		 
}
