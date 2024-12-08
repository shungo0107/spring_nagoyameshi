package com.example.nagoyameshi.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.VerificationTokenService;

/*
 * Listenerクラスの役割は、SignupEventクラスからの通知を受け、メール認証用のメールを送信することです。
 */

/*
 *  @Componentアノテーションをつけ、ListenerクラスのインスタンスがDIコンテナに登録されるようにします。
 *  こうすることで、@EventListenerアノテーションがついたメソッドをSpring Boot側が自動的に検出し、イベント発生時に実行してくれます。
 */
@Component
public class SignupEventListener {
    private final VerificationTokenService verificationTokenService;    
    private final JavaMailSender javaMailSender;
    
    public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;        
        this.javaMailSender = mailSender;
    }

    // Listenerクラス内では、イベント発生時に実行したいメソッドに対して@EventListenerアノテーションをつけます。
    @EventListener
    private void onSignupEvent(SignupEvent signupEvent) {
        User user = signupEvent.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.create(user, token);
        
        // メール本文
        String recipientAddress = user.getEmail();
        String subject = "メール認証";
        String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
        String message = "以下のリンクをクリックして会員登録を完了してください。";
        
        /*
         * Spring Frameworkが提供するSimpleMailMessageクラスを使うことで、シンプルなメールメッセージをオブジェクトとして作成できます。
         * 今回使っているのは以下のメソッドです。
				・setTo()			：送信先のメールアドレスをセットする
				・setSubject()	：件名をセットする
				・setText()		：本文をセットする
			
         */
        
        SimpleMailMessage mailMessage = new SimpleMailMessage(); 
        mailMessage.setTo(recipientAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message + "\n" + confirmationUrl);

        // Java Mail Senderを利用すれば、簡単にメールの送信処理を実行できます。
        javaMailSender.send(mailMessage);
    }
}
