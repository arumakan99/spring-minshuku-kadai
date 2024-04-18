package com.example.samuraitravel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 設定用クラスとして機能させる（@Beanつけるために必要）
@Configuration
// SpringSecurityによるセキュリティ機能を有効にし、認証・認可のルールやログイン・ログアウト処理など各種設定を行えるようにする
@EnableWebSecurity
// メソッドレベルでのセキュリティ機能を有効にする（例：管理者のみがそのメソッドにアクセスできるようにする）
@EnableMethodSecurity
public class WebSecurityConfig {

	// そのメソッドの戻り値をDIコンテナに登録する
	// SpringSecurity側で適切にセキュリティ設定やPWのハッシュアルゴリズムを適用してくれる
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		// 誰にどのページへのアクセスを許可するか設定
		.authorizeHttpRequests((requests) -> requests
				// 末尾に/**をつけるとそのパス以下の全てのファイルが対象になる
				// 全てのユーザーにアクセスを許可するURL
                .requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses", "/houses/{id}", "/stripe/webhook").permitAll()
				// 管理者にのみアクセスを許可するURL
				.requestMatchers("/admin/**").hasRole("ADMIN")
				// 上記以外のURLはログインが必要（会員または管理者のどちらでもOK）
				.anyRequest().authenticated()
				)
		// ログインに関するURLを設定
		.formLogin((form) -> form
				// ログインページのURL
				.loginPage("/login")
				// ログインフォームの送信先URL
				.loginProcessingUrl("/login")
				// ログイン成功時のリダイレクト先URL
				.defaultSuccessUrl("/?loggedIn")
				// ログイン失敗時のリダイレクト先URL
				.failureUrl("/login?error")
				// ログイン関連のURLは誰でもアクセス可能
				.permitAll()
				)
		// ログアウトに関するURLを設定
		.logout((logout) -> logout
				// ログアウト時のリダイレクト先URL
				.logoutSuccessUrl("/?loggedOut")
				// ログアウト関連のURLは誰でもアクセス可能
				.permitAll()
	             )
	             .csrf().ignoringRequestMatchers("/stripe/webhook");
			    
		return http.build();
	}

	@Bean
	// PWのハッシュアルゴリズムをBCryptに設定
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
