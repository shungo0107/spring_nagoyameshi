package com.example.nagoyameshi.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.VerificationTokenService;

@Component
public class ResetEventListener {
    private final VerificationTokenService verificationTokenService;    
    private final JavaMailSender javaMailSender;
    
    public ResetEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;        
        this.javaMailSender = mailSender;
    }
    
    @EventListener
    private void onResetEvent(ResetEvent resetEvent) {
        User user = resetEvent.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.update(user, token);
        
        // メール本文
        String recipientAddress = user.getEmail();
        String subject = "パスワード再設定認証";
        String confirmationUrl = resetEvent.getRequestUrl() + "/verify?token=" + token;
        String message = "以下のリンクをクリックしてパスワードを再設定してください。";
        
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
