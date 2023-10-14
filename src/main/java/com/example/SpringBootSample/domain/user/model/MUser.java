package com.example.SpringBootSample.domain.user.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
//「13章JAPで追加」データベーステーブルにマッピングするクラスに@Entityを付ける
//クラス名と同じテーブル名を生成してくれる
@Entity
//「13章JAPで追加」JPAではクラス名と同じテーブル名をマッピングしようとする
//クラス名とテーブル名が異なる場合は、@Tableを付けてname属性にマッピングしたいテーブル名を設定する
@Table(name="m_user")
public class MUser {
	//　主キーのフィールドに付ける
	@Id
	private String userId;
	private String password; 
	private String userName; 
	private Date birthday; 
	private Integer age; 
	private Integer gender; 
	private Integer departmentId; 
	private String role;
	// 「13章JAPで追加」O/Rマッピングをしたくないフィールドには@Transientアノテーションを付ける
	// 一時的につけている
	@Transient
	private Department department;
	@Transient
	private List<Salary> salaryList;
}

