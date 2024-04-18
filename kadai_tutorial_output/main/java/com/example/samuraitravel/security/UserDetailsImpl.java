package com.example.samuraitravel.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.samuraitravel.entity.User;

public class UserDetailsImpl implements UserDetails {
	private final User user;
	private final Collection<GrantedAuthority> authorities;
	
	public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}
	
	public User getUser() {
		return user;
	}
	
	// @OverrideでUserDetailsインターフェースに定義されている抽象メソッドを上書きする
	// ハッシュ化済みのPWを返す
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	// ログイン時に利用するユーザー名（メアド）を返す
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	// ロールのコレクションを返す
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	// アカウントやPWの期限、アカウントのロックといった機能は今回つけないため必ずtrueを返す
	
	// アカウントが期限切れでなければtrueを返す
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	// ユーザーがロックされていなければtrueを亜エス
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	// ユーザーのPWが期限切れでなければtrueを返す
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	// メール認証機能を作成するためユーザーの有効性チェックはする
	
	// ユーザーが有効であればtrueを返す
	@Override
	public boolean isEnabled() {
		return user.getEnabled();
	}
}
