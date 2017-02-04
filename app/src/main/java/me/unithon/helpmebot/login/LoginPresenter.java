package me.unithon.helpmebot.login;



import me.unithon.helpmebot.net.presenter.BasePresenter;
import me.unithon.helpmebot.net.service.UserService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-31.
 */

public class LoginPresenter extends BasePresenter implements ILoginPresenter {

	private UserService userService;
	private ILoginView view;

	public LoginPresenter(ILoginView view){
		this.view = view;
		this.userService = new UserService();

	}


	@Override
	public Observable<String> signIn(String email, String pwd) {
		view.showLoadingBar();

		return userService
				.signIn(email,pwd)
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Observable<Void> sendToken(String accessToken, String refreshToken, String tokenType, String clientId) {
		return userService
				.sendToken(accessToken, refreshToken, tokenType, clientId)
				.observeOn(AndroidSchedulers.mainThread());
	}
}
