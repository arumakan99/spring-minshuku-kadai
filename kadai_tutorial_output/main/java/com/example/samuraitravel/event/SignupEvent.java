// Eventクラス　Listenerクラスにイベントが発生したことを知らせる

package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEvent;

import com.example.samuraitravel.entity.User;

import lombok.Getter;

// 外部（Listenerクラス）から情報を取得できうようにGetterを定義
@Getter
// ApplicationEventクラスを継承（イベントを作成するための基本的なクラス、イベントのソースなどを保持）
public class SignupEvent extends ApplicationEvent {
	private User user;
	private String requestUrl;
	
	public SignupEvent(Object source, User user, String requestUrl) {
		// 親クラスのコンストラクタ呼出　イベントのソースを渡す
		super(source);
		
		this.user = user;
		this.requestUrl = requestUrl;
	}
}
