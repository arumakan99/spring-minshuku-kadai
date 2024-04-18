package com.example.samuraitravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// このクラスをエンティティとして機能させる
@Entity
// このエンティティにマッピングされるテーブル名を指定
// 今回はhousesテーブルをマッピング
@Table(name = "houses")
// ゲッターやセッターを自動生成
@Data
public class House {
	// このフィールドを主キーに指定
	@Id
	// テーブル内のAUTO_INCREMENTを指定したカラムを利用して値を生成（今回はidの値を自動採番）
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// このフィールドにマッピングされるカラム名を指定
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private Integer price;

	@Column(name = "capacity")
	private Integer capacity;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "address")
	private String address;

	@Column(name = "phone_number")
	private String phoneNumber;

	// insertabel属性：そのカラムに値を挿入できるか（falseにすると値の管理をDBに任せられる）
	// updatable属性：そのカラムの値を更新できるか
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
}
