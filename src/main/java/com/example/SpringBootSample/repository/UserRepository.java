package com.example.SpringBootSample.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.example.SpringBootSample.domain.user.model.MUser;

// JPAでリポジトリーを生成する場合、JpaRepositoryを継承する。
// JpaRepository<データモデル型, 主キーの型>
// JpaRepositoryを継承する場合は、@Repositoryを省略できる
public interface UserRepository extends JpaRepository<MUser, String>{
	
	ここで原因不明のエラーがでているので
	一応ソースコードは13-2-2のものから使用する(13章の応用編から対応になる)
	
	/** ログインユーザー検索 */
	@Query("select user"
			+ " from MUser user"
			+ " where userId = :userId")
	public MUser findLoginUser(@Param("userId") String userId);
	// ここで@Paramはdata.repository.query.Paramのもの
	// @Queryに書いたJPQLの:変数名に埋め込む。
	// nativeQuery属性にtrueを指定すると純粋なSQLを書くことも絵できる
	
	
	/** ユーザー更新 */
	// @Queryを使ってupdate, delete, insert文と　DDLを実行する場合必ず@Modifyingを付けなければいけない
	@Modifying
	@Query("update MUser u" 
			+ " set u.password = :password, u.userName = :userName"
			+ " where u.userId = :userId")
	public void updateUser(@Param("userId") String userId, 
			@Param("password") String password, @Param("userName") String userName);

	
	// 特定ルールに沿ったメソッドを用意すればselect分を自動生成することも可能(ここでは扱ってない)
}
