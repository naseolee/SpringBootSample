package com.example.SpringBootSample.domain.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SpringBootSample.domain.user.model.MUser;
import com.example.SpringBootSample.domain.user.service.UserService;
import com.example.SpringBootSample.repository.UserMapper;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper mapper;
	
	//　これでencode
	@Autowired
	private PasswordEncoder encoder;
		
	/** ユーザー登録 */
	@Override
	public void signup(MUser user) {
		user.setDepartmentId(1); // 部署
		user.setRole("ROLE_GENERAL"); // ロール
		
		// password encode
		user.setPassword(encoder.encode(user.getPassword()));
		
		mapper.insertOne(user);
	}

	/** ユーザー取得 */
	@Override
	public List<MUser> getUsers(MUser user) {
		return mapper.findMany(user);
	}
	
	/** ユーザー取得(1件) */ 
	@Override
	public MUser getUserOne(String userId) {
		return mapper.findOne(userId);
	}

	/** ユーザー更新(1件) */ 
	@Transactional // 宣言的トランザクション/このアノテーションをつｋれることで例外発生時自動でロールバックされる。
	@Override
	public void updateUserOne(MUser user) {
		
		// password encode
		user.setPassword(encoder.encode(user.getPassword()));
		
		mapper.updateOne(user);
		// 例外を発生させてロールバックされるのか確認する！！
		//int i = 1/0;
	}

	/** ユーザー削除(1件) */ 
	@Override
	public void deleteUserOne(String userId) {
		int count = mapper.deleteOne(userId);
	}

	/** ログインユーザー情報取得 */ 
	@Override
	public MUser getLoginUser(String userId) {
		return mapper.findLoginUser(userId);
	}
	
}
