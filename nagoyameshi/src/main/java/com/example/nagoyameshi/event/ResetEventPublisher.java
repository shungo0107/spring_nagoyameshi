package com.example.nagoyameshi.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;

@Component
public class ResetEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public ResetEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;                
    }
 
    /*
     * イベントを発行するには、ApplicationEventPublisherインターフェースが提供するpublishEvent()メソッドを使います。
     * 引数には発行したいEventクラス（SignupEventクラス）のインスタンスを渡します。
     */
 
    public void publishResetEvent(User user, String requestUrl) {
        applicationEventPublisher.publishEvent(new ResetEvent(this, user, requestUrl));
    }
}
