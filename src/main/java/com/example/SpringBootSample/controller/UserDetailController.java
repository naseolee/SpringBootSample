package com.example.SpringBootSample.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SpringBootSample.domain.user.model.MUser;
import com.example.SpringBootSample.domain.user.service.UserService;
import com.example.SpringBootSample.form.UserDetailForm;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserDetailController {
	@Autowired
	private UserService userSerivce;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/** ユーザー 詳細 画面を表示 */
	//　メール形式のuserIdを受け取る為には正規表現を利用する必要がある
	@GetMapping("/detail/{userId:.+}")
	public String getUser(UserDetailForm form, Model model, @PathVariable("userId") String userId) {
		
		// ユーザーを1件取得
		MUser user = userSerivce.getUserOne(userId);
		user.setPassword(null);
		
		// MUserをformに変換 
		form = modelMapper.map(user, UserDetailForm. class);
		// ModelMapperではリストのコピーができないため、setterを使用
		form.setSalaryList(user.getSalaryList());
		
		// Model に 登録
		model.addAttribute("userDetailForm", form);

		// ユーザー詳細画面を表示
		return "user/detail";
	}
	
	/** ユーザー更新処理 */
	// params属性には「button」タグのname属性と同じ値を指定してマッピングする　valueはurl
	@PostMapping(value = "/detail", params = "update")
	public String updateUser(UserDetailForm form, Model model) {
		
		MUser user = modelMapper.map(form, MUser.class);
		
		// エラー確認用
		try {
			// ユーザーを1件更新
			userSerivce.updateUserOne(user);
		} catch (Exception e) {
			log.error("ユーザー更新でエラー", e);
		}
				
		// ユーザー一覧画面にリダイレクト
		return "redirect:/user/list";
	}
	
	/** ユーザー削除処理 */
	@PostMapping(value = "/detail", params = "delete")
	public String deleteUser(UserDetailForm form, Model model) {
		
		// ユーザーを1件削除
		userSerivce.deleteUserOne(form.getUserId());
		
		// ユーザー一覧画面にリダイレクト
		return "redirect:/user/list";
	}
	
		
}
