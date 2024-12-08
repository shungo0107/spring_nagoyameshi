package com.example.nagoyameshi.event;

import org.springframework.context.ApplicationEvent;

import com.example.nagoyameshi.entity.User;

import lombok.Getter;

@Getter
public class ResetEvent extends ApplicationEvent  {
    private User user;
    private String requestUrl;        

    public ResetEvent(Object source, User user, String requestUrl) {
    	/*
    	 * スーパークラス（親クラス）のコンストラクタを呼び出し、イベントのソース（発生源）を渡します。
    	 * イベントのソースとは、具体的にはPublisherクラスのインスタンスのことです。
    	 */
        super(source);
        
        this.user = user;        
        this.requestUrl = requestUrl;
    }
}
