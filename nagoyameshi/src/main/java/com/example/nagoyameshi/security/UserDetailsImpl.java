package com.example.nagoyameshi.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagoyameshi.entity.User;

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
    
    // ハッシュ化済みのパスワードを返す
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    // ログイン時に利用するユーザー名（メールアドレス）を返す
    @Override
    public String getUsername() {
        return user.getEmail();
    }
    
    // ロールのコレクションを返す
    /* <? extends A>という書き方について
     * 　getAuthorities()メソッドでは戻り値の型に<? extends A>という見慣れない書き方をしていますが、これは「Aまたはそのサブタイプすべて」という意味です。
     * 　　サブタイプ＝ある型から派生した型のこと。スーパークラス（親クラス）を継承したサブクラス（子クラス）や、
     * 　　　　　　　インターフェースを実装したクラスがこれに当てはまる
     * 　つまり、Collection<? extends GrantedAuthority>は、GrantedAuthorityインターフェースを実装したクラス、
     * 　またはそのサブタイプすべてのオブジェクトのコレクションを表します。
     * 　なおGrantedAuthorityは、ユーザーに割り当てられたロール（権限）を表すインターフェースです。Spring Securityが提供しています。
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    // アカウントが期限切れでなければtrueを返す
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    // ユーザーがロックされていなければtrueを返す
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }    
    
    // ユーザーのパスワードが期限切れでなければtrueを返す
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    // ユーザーが有効であればtrueを返す
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
