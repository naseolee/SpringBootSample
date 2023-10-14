package com.example.SpringBootSample.domain.user.service;

import java.util.List;

import com.example.SpringBootSample.domain.user.model.MUser;

public interface UserService {
	
	/** ユーザー 登録 */ 
	public void signup(MUser user);
	
	/** ユーザー 取得 */
	public List<MUser> getUsers(MUser user);
	
	/** ユーザー 取得(1件) */ 
	public MUser getUserOne(String userId);
	
	/** ユーザー 更新(1件) */ 
	public void updateUserOne(MUser user);
	
	/** ユーザー 削除(1件) */ 
	public void deleteUserOne(String userId);
	
	/** ログインユーザー情報取得 */ 
	public MUser getLoginUser(String userId);

	
}
