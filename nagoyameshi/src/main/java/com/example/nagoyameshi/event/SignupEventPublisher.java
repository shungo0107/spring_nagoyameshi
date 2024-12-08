package com.example.nagoyameshi.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;


/*
 * Publisherクラスはコントローラなど、イベントを発生させたい処理（例：AuthControllerのsignup()メソッド）の中で呼び出して使います。
 * そこで、@ComponentアノテーションをつけてDIコンテナに登録し、呼び出すクラス（今回はコントローラ）に対して依存性の注入（DI）を行えるようにします。
 */
@Component
public class SignupEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;                
    }
 
    /*
     * イベントを発行するには、ApplicationEventPublisherインターフェースが提供するpublishEvent()メソッドを使います。
     * 引数には発行したいEventクラス（SignupEventクラス）のインスタンスを渡します。
     */
 
    public void publishSignupEvent(User user, String requestUrl) {
        applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
    }
}