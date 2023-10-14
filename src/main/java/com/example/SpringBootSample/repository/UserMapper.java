package com.example.SpringBootSample.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.SpringBootSample.domain.user.model.MUser;

@Mapper
public interface UserMapper {
	
	/** ユーザー 登録 */
	public int insertOne(MUser user);
	
	/** ユーザー 取得 */
	//戻り値が複数件の場合Listにする
	public List<MUser> findMany(MUser user);
	
	/** ユーザー 取得(1件) */ 
	public MUser findOne(String userId);
	
	/** ユーザー 更新(1件) */
	public void updateOne(@Param("user") MUser user);
	
	/** ユーザー 削除(1件) */
	public int deleteOne(@Param("userId") String userId);
	
	/** ログインユーザー取得 */
	public MUser findLoginUser(String userId);
}
