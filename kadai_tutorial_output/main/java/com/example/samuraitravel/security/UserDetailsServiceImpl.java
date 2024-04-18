package com.example.samuraitravel.security;


import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.UserRepository;

// サービスクラス
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	// UserDetailsServiceインターフェースで定義されているloadByUsername()抽象メソッドを上書き
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			// フォームから送信されたメアドに一致するユーザーを取得
			User user = userRepository.findByEmail(email);
			// そのユーザーのロールを取得する
			String userRoleName = user.getRole().getName();
			// 権限を格納するためのコレクションを作成
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			// ユーザーのロールをSimpleGrantedAuthorityオブジェクトに変換して権限のコレクションに追加
			authorities.add(new SimpleGrantedAuthority(userRoleName));
			// ユーザー情報と権限情報を使用してUserDetailsImplオブジェクトを作成し、返す
			return new UserDetailsImpl(user, authorities);
		// 例外処理を行い、ユーザーが見つからない場合はメッセージ
		} catch (Exception e) {
			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
		}
	}
}
