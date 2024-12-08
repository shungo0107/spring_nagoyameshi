package com.example.nagoyameshi.event;

import org.springframework.context.ApplicationEvent;

import com.example.nagoyameshi.entity.User;

import lombok.Getter;


/*
 * 	Listenerクラスにイベントが発生したことを知らせるクラス。また、イベントに関する情報（会員登録したユーザーなど）も保持できる。
 * 一般的にはApplicationEventクラスを継承して作成する
*/

/*
 * Eventクラスはイベントに関する情報を保持するので、外部（具体的にはListenerクラス）からそれらの情報を取得できるように
 * ゲッターを定義します。
 * Lombokが提供する@Getterアノテーションをクラスにつけることで、ゲッターのみが自動的に定義されます。
 */
@Getter
public class SignupEvent extends ApplicationEvent {
    private User user;
    private String requestUrl;        

    public SignupEvent(Object source, User user, String requestUrl) {
    	/*
    	 * スーパークラス（親クラス）のコンストラクタを呼び出し、イベントのソース（発生源）を渡します。
    	 * イベントのソースとは、具体的にはPublisherクラスのインスタンスのことです。
    	 */
        super(source);
        
        this.user = user;        
        this.requestUrl = requestUrl;
    }
}
