package me.unithon.helpmebot.login;

import rx.Observable;

/**
 * Created by kinamare on 2016-12-31.
 */

public interface ILoginPresenter {
	Observable<String> signIn(String email, String pwd);
	Observable<Void> sendToken(String accessToken,String refreshToken,String tokenType,String clientId);

}
