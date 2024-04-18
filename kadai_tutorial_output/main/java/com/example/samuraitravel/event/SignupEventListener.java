// Listenerクラス　SignupEventクラスから通知を受け、メール認証用のメールを送信する
package com.example.samuraitravel.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.service.VerificationTokenService;

// @ComponentでListenerクラスのインスタンスがDIコンテナに登録されるようにする
@Component
public class SignupEventListener {
	private final VerificationTokenService verificationTokenService;    
	private final JavaMailSender javaMailSender;

	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;        
		this.javaMailSender = mailSender;
	}

	// @EventListenerイベント発生時に実行したいメソッドにつける
	@EventListener
	// どのイベント発生時に実行するか、通知を受け付けるEventクラスを引数に設定
	// 今回はsignupEventクラスから通知を受けた時にonSignupEvent()メソッドが実行される
	private void onSignupEvent(SignupEvent signupEvent) {
		User user = signupEvent.getUser();
		//トークンはUUIDで生成し、ユーザーIDとともにDBに保存		
		String token = UUID.randomUUID().toString();
		verificationTokenService.create(user, token);

		String recipientAddress = user.getEmail();
		String subject = "メール認証";
		// 生成したトークンをメール認証用のURLにパラメータとして埋め込み、メールメッセージ内に記載
		// そうすることで、アクセスされたときにDBの値と一致するかどうか確認できる
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
		String message = "以下のリンクをクリックして会員登録を完了してください。";

		// シンプルなメールメッセージをオブジェクトとして作成
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		// setTo()：送信先のメアドをセット
		mailMessage.setTo(recipientAddress);
		// setSubject()：件名をセット
		mailMessage.setSubject(subject);
		// setText()：本文をセット
		mailMessage.setText(message + "\n" + confirmationUrl);
		// メールの送信処理を実行
		javaMailSender.send(mailMessage);
	}
}
