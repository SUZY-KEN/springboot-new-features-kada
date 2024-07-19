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

@Service

public class UserDetailsServiceImp1 implements UserDetailsService {
    private final UserRepository userRepository;    
    
    public UserDetailsServiceImp1(UserRepository userRepository) {
        this.userRepository = userRepository;        
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {  
        try {
            User user = userRepository.findByEmail(email);
            String userRoleName = user.getRole().getName();
            Collection<GrantedAuthority> authorities = new ArrayList<>();         
            authorities.add(new SimpleGrantedAuthority(userRoleName));
            
            if(user!=null)
              {
              	System.out.println("ユーザー情報："+user+"\n"+"userRoleName:"+userRoleName+"\n"+"authorities:"+authorities);
              }
              
            
            return new UserDetailsImp1(user, authorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
        }
    }   
}



//@Service
//public class UserDetailsServiceImp1 implements UserDetailsService {
//	
//    private final UserRepository userRepository;    
//    
//    public UserDetailsServiceImp1(UserRepository userRepository) {
//        this.userRepository = userRepository;  
//        System.out.println("service 通った");
//    }
//    
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {  
//        try {
//        	
//        	
//            User user = userRepository.findByEmail(email);
//            
//            if(user!=null)
//            {
//            	System.out.println("ユーザー情報："+user);
//            }
//            
//            
//            String userRoleName = user.getRole().getName();
//            Collection<GrantedAuthority> authorities = new ArrayList<>(); 
//            
//            authorities.add(new SimpleGrantedAuthority(userRoleName));
//           
//           
//            return new UserDetailsImp1(user, authorities);
//        } catch (Exception e) {
//      	 logger.error("User not found for email: {}", email, e);
//        	 
//            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
//            
//        }
//    } 
//    
//}
















//public class UserDetailsServiceImp1 implements UserDetailsService {
//
//	private final UserRepository userRepository;
//	
//	public UserDetailsServiceImp1(UserRepository userRepository)
//	{
//		this.userRepository=userRepository;
//	}
//	
//	@Override
//	public UserDetails loadUserByUsername(String email) 
//	throws UsernameNotFoundException{
//		try {
//			
//			//userエンティティオブジェクトにリポジトリインターフェースのメソッドを使って、引数emailの値と一致するレコードのすべてのフィールドを格納
//			User user=userRepository.findByEmail(email);
//			//上記の各フィールドのRoleをゲットし、Roleエンティティにはいり、nameのフィールドを取ってくる。
//			String userRoleName=user.getRole().getName();
//			
//			Collection<GrantedAuthority>authorities=new ArrayList<>();
//			
//			authorities.add(new SimpleGrantedAuthority(userRoleName));
//			//UserDetailsImp1に上記で取得したフィールドを入れてインスタンス化することで認証に入る。
//			return new UserDetailsImp1(user, authorities);
//		}catch(Exception e)
//		{
//			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
//		}
//	}
//}
