package com.example.SpringBootSample.domain.user.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SpringBootSample.domain.user.model.MUser;
import com.example.SpringBootSample.domain.user.service.UserService;
import com.example.SpringBootSample.repository.UserRepository;
//[13-JPA]
@Service
// 同じinterfaceを実装した具体クラスが複数の場合はSpringがどのクラスをDIするか分からない
// その場合@Primaryを付けて明示的に指定する(方法は幾つかあるみたい)
@Primary
public class UserServiceImpl2 implements UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
		
	/** ユーザー登録 */
	@Transactional
	@Override
	public void signup(MUser user) {
		// 存在チェック(save()は存在する場合更新、存在しない場合は登録するため、ここでは更新を防止するためチェックしてる)
		boolean exists = repository.existsById(user.getUserId());
		if (exists) {
			throw new DataAccessException("ユーザーが既に存在") {};
		}
		
		user.setDepartmentId(1); // 部署
		user.setRole("ROLE_GENERAL"); // ロール
		
		// password encode
		user.setPassword(encoder.encode(user.getPassword()));
		
		// insert
		repository.save(user);
	}

	/** ユーザー取得 */
	@Override
	public List<MUser> getUsers(MUser user) {
		return repository.findAll();
	}
	
	/** ユーザー取得(1件) */ 
	@Override
	public MUser getUserOne(String userId) {
		Optional<MUser> option = repository.findById(userId);
		MUser user = option.orElse(null);
		return user;
	}

	/** ユーザー更新(1件) */ 
	// @Modifyingアノテーションを付けたメソッドを実行するためには@Transactionアノテーションを必ず付けること
	// @Modifying有無にかかわらず、登録・更新・削除のSQLであれば@Transactionを付けるように！！！！
	@Transactional
	@Override
	public void updateUserOne(MUser user) {
		String encryptPassword = encoder.encode(user.getPassword());
		
		//ユーザー更新
		//repository.updateUser(user.getUserId(), encryptPassword, user.getUserName());
	}

	/** ユーザー削除(1件) */
	@Transactional
	@Override
	public void deleteUserOne(String userId) {
		repository.deleteById(userId);
	}

	/** ログインユーザー情報取得 */ 
	@Override
	public MUser getLoginUser(String userId) {
		/* 任意のSQLを使用するため、廃止。
		Optional<MUser> option = repository.findById(userId);
		MUser user = option.orElse(null);
		*/
		return repository.findLoginUser(userId);
	}
	
}
