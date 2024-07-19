package com.example.samuraitravel.security;




import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.samuraitravel.entity.User;


public class UserDetailsImp1 implements UserDetails {
    private final User user;
    private final Collection<GrantedAuthority> authorities;
   
    
    
    public UserDetailsImp1(User user, Collection<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    
    
    public User getUser() {
        return user;
    }
    
    // ハッシュ化済みのパスワードを返す
    @Override
    public String getPassword() {
    	System.out.println("Getting Password: " + user.getPassword());
    	
        return user.getPassword();
    }
    
    // ログイン時に利用するユーザー名（メールアドレス）を返す
    @Override
    public String getUsername() {
    	System.out.println("Getting Email: " + user.getEmail());
    	
        return user.getEmail();
    }
    
    // ロールのコレクションを返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	 System.out.println("Getting Authorities: " + authorities);
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
    	  System.out.println("Is Enabled: " + user.getEnabled());
    	
        return user.getEnabled();
    }
    
    
}

//public class UserDetailsImp1 implements UserDetails {
//
//	private final User user;
//	private final Collection<GrantedAuthority>authorities;
//	
//	//UserDetailsServiceImp1からインスタンス化して呼び出される。
//	public UserDetailsImp1(User user,Collection<GrantedAuthority> authorities)
//	{
//		this.user=user;
//		this.authorities=authorities;
//	}
//	
//	
//	public User userGet() {
//		return user;
//		
//	}
//	
//	// このuserdeatilsインターフェースのメソッドからオーバライドしたメソッドはどこで実行されているか？
//	
//	@Override
//	public String getPassword()
//	{
//		return user.getPassword();
//	}
//	@Override
//	public String getUsername() {
//		return user.getEmail();
//	}
//	
//	
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities(){
//		return authorities;
//	}
//	
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//	
//	
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//	
//	
//	@Override
//	public boolean isCredentialsNonExpired()
//	{
//		return true;
//	}
//	
//	@Override
//	public boolean isEnabled()
//	{
//		return user.getEnabled();
//	}
//	
//	
//
//	
//}


