// Publisherクラス　イベントを発行する
package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;

// @ComponentをつけてDIコンテナに登録し、呼び出すクラス（今回はコントローラ）に対して依存性の注入（DI）できるようにする
@Component
public class SignupEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
	// SignupEventクラスのコンストラクタの第1引数には自分自身（SignupEventPublishe）rのインスタンスを渡す
	// SignupEventクラスにはこのインスタンスがイベントのソースとして渡される
	public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;                
	}

	// イベントを発行するにはpublishEvent()を使う　引数には発行したいEventクラスのインスタンスを渡す
	public void publishSignupEvent(User user, String requestUrl) {
		applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
	}
}