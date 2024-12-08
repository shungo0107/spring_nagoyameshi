package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.PaidInputForm;
import com.example.nagoyameshi.form.ResetInputForm;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class UserService {
     private final UserRepository userRepository;
     private final RoleRepository roleRepository;
     private final PasswordEncoder passwordEncoder;
     
     public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
         this.userRepository = userRepository;
         this.roleRepository = roleRepository;        
         this.passwordEncoder = passwordEncoder;
     }    
     
     // 会員情報を新規登録する
     @Transactional
     public User create(SignupForm signupForm) {
         User user = new User();
         Role role = roleRepository.findByName("ROLE_GENERAL");
         
         user.setName(signupForm.getName());
         user.setFurigana(signupForm.getFurigana());
         user.setEmail(signupForm.getEmail());
         user.setPaidFlg("0");
         user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
         user.setRole(role);
         user.setEnabled(false);        
         
         return userRepository.save(user);
     } 
     
     // 会員情報を更新する
     @Transactional
     public void update(UserEditForm userEditForm) {
         User user = userRepository.getReferenceById(userEditForm.getId());
         
         user.setName(userEditForm.getName());
         user.setFurigana(userEditForm.getFurigana());
         user.setEmail(userEditForm.getEmail());      
         
         userRepository.save(user);
     }   
     
     // パスワードを再設定する
     @Transactional
     public void updatePassword(Integer id ,ResetInputForm resetInputForm) {
         User user = userRepository.getReferenceById(id);
         user.setPassword(passwordEncoder.encode(resetInputForm.getPassword()));
         userRepository.save(user);

     } 
     
     // 有料会員情報を新規登録する
     @Transactional
     public void createCredit(Integer id , PaidInputForm paidInputForm) {
     	
     	User user = userRepository.getReferenceById(id);
     	Role role = roleRepository.findByName("ROLE_PAID");
     	
     	user.setCreditFirstName(paidInputForm.getCreditFirstName());
     	user.setCreditLastName(paidInputForm.getCreditLastName());
     	user.setCreditNumber(paidInputForm.getCreditNumber());
     	user.setCreditYear(paidInputForm.getCreditYear());
     	user.setCreditMonth(paidInputForm.getCreditMonth());
     	user.setCreditSecureCode(paidInputForm.getCreditSecurityCode());
     	user.setRole(role);
     	
     	userRepository.save(user);
     	
     }
     
     // メールアドレスが登録済みかどうかをチェックする
     public boolean isEmailRegistered(String email) {
         User user = userRepository.findByEmail(email);  
         return user != null;
     }  
     
     // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
     public boolean isSamePassword(String password, String passwordConfirmation) {
         return password.equals(passwordConfirmation);
     }   
     
     // [メール認証]　ユーザーを有効にする
     @Transactional
     public void enableUser(User user) {
         user.setEnabled(true); 
         userRepository.save(user);
     }   
     
     // メールアドレスが変更されたかどうかをチェックする
     public boolean isEmailChanged(UserEditForm userEditForm) {
         User currentUser = userRepository.getReferenceById(userEditForm.getId());
         return !userEditForm.getEmail().equals(currentUser.getEmail());      
     }  
     

}
