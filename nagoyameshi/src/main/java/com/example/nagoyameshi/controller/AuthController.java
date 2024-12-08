package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.event.ResetEventPublisher;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.form.ResetForm;
import com.example.nagoyameshi.form.ResetInputForm;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.service.UserService;
import com.example.nagoyameshi.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    private final UserService userService;    
    private final UserRepository userRepository;      
    private final SignupEventPublisher signupEventPublisher;
    private final ResetEventPublisher resetEventPublisher;
    private final VerificationTokenService verificationTokenService;

    
    public AuthController(UserService userService, 
    										UserRepository userRepository,
    										SignupEventPublisher signupEventPublisher,
    										ResetEventPublisher resetEventPublisher,
    										VerificationTokenService verificationTokenService){        
        this.userService = userService;
        this.userRepository = userRepository;
        this.signupEventPublisher = signupEventPublisher;
        this.resetEventPublisher = resetEventPublisher;
        this.verificationTokenService = verificationTokenService;
        
    }
    @GetMapping("/login")
    public String login() {        
        return "auth/login";
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {        
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }   
    
    /*
     * HttpServletRequestインターフェースを利用して動的にURLを取得します。
     * HttpServletRequestは、HTTPリクエストに関するさまざまな情報を提供するインターフェースです。
     * Spring Bootでは、コントローラのメソッドの引数でHttpServletRequestオブジェクトを受け取ることで、
     * そのHTTPリクエストに関するさまざまな情報を取得できるようになります。
     * 今回はgetRequestURL()メソッドを使い、リクエストURL（https://ドメイン名/signup）を取得します。
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, 
    									BindingResult bindingResult, 
    									RedirectAttributes redirectAttributes,
    									HttpServletRequest httpServletRequest) {      
        // メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }    
        
        // パスワードとパスワード（確認用）の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }        
        
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }
        
        User createdUser = userService.create(signupForm);
        String requestUrl = new String(httpServletRequest.getRequestURL());
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");        

        return "redirect:/";
    }   
    
    /*
     *  メール認証用URL。今回のように引数に@RequestParam(name = "token")を設定しておけば、
     *  「https://ドメイン名/signup/verify?token=トークン」の中の「トークン」の部分を取得できる
     */
    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, 
    		                           Model model) {
    	
    	VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        
        if (verificationToken != null) {
            User user = verificationToken.getUser();  
            userService.enableUser(user);
            String successMessage = "会員登録が完了しました。";
            model.addAttribute("successMessage", successMessage);            
        } else {
            String errorMessage = "トークンが無効です。";
            model.addAttribute("errorMessage", errorMessage);
        }
        
        return "auth/verify";         
    }    
    
    @GetMapping("/reset")
    public String passwordreset(Model model) {        
        model.addAttribute("ResetForm", new ResetForm());
        return "auth/reset";
    }  
    
    @PostMapping("/reset")
    public String resetVerify(@ModelAttribute @Validated ResetForm resetForm,
    											@RequestParam(name = "email", required = true) String email,
    											BindingResult bindingResult, 
    											RedirectAttributes redirectAttributes,
    											HttpServletRequest httpServletRequest) {      
    	
    	if(email.isEmpty()) {
    		return "auth/reset";
    	}
    	
    	if (!userService.isEmailRegistered(resetForm.getEmail())) {
    		FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "未登録のメールアドレスです。");
    		bindingResult.addError(fieldError);
    		return "auth/reset";
    	}    		
    	
		User updatedUser = userRepository.findByEmail(email);
		String requestUrl = new String(httpServletRequest.getRequestURL());
		resetEventPublisher.publishResetEvent(updatedUser, requestUrl);
		redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、パスワード再設定を完了してください。");        
		
		return "redirect:/";
		}  
    
    @GetMapping("/reset/verify")
    public String resetVerify(@RequestParam(name = "token") String token, 
    											Model model) {
    	
    	VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
    	User user = verificationToken.getUser();
        
        if (verificationToken != null) {
        	model.addAttribute("resetInputForm", new ResetInputForm());
        	model.addAttribute("userId", user.getId());
        	 return "auth/passwordinput";     
        } else {
            String errorMessage = "トークンが無効です。";
            model.addAttribute("errorMessage", errorMessage);
            return "auth/reset";     
        }
        
    } 
    
    @PostMapping("/reset/execute/{id}")
    public String resetExecute(@ModelAttribute @Validated ResetInputForm resetInputForm,
    												@PathVariable(name = "id") Integer userId,
    												RedirectAttributes redirectAttributes) {
    	
    	userService.updatePassword(userId,resetInputForm);
		redirectAttributes.addFlashAttribute("successMessage", "パスワード再設定が完了しました");        

		return "redirect:/";
    } 
}
