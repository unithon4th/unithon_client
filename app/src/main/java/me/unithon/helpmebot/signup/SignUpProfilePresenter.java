package me.unithon.helpmebot.signup;


import java.io.File;

import me.unithon.helpmebot.net.presenter.BasePresenter;
import me.unithon.helpmebot.net.serialization.BotResponse;
import me.unithon.helpmebot.net.service.FileService;
import me.unithon.helpmebot.net.service.UserService;
import me.unithon.helpmebot.util.FileUtil;
import me.unithon.helpmebot.vo.User;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-17.
 */

public class SignUpProfilePresenter extends BasePresenter implements ISignUpProfilePresenter {

	private ISignUpProfileView view;
	private UserService userService;
	private FileService fileService;

	public SignUpProfilePresenter(ISignUpProfileView view) {
		this.view = view;
		this.userService = new UserService();
		this.fileService = new FileService();

	}


	@Override
	public Observable<User> signUp(String email, String pwd, String name) {
		view.showLoadingBar();

		User user = generateUser(email, pwd, name);

		return userService
				.signUp(user)
				.observeOn(AndroidSchedulers.mainThread());
	}

	private User generateUser(String email, String pwd, String name) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(pwd);
		user.setName(name);
		return user;
	}


	@Override
	public Observable<String> uploadProfileImg(File file) {

		return fileService
				.uploadProfileImg(FileUtil.makeMultiPartBody(file))
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Observable<Void> setProfileImg(String profileImgUrl) {
		return Observable.create((Observable.OnSubscribe<Void>) subscriber -> {
			userService
					.setProfilePhotos(profileImgUrl)
					.subscribe(new Subscriber<BotResponse>() {
						@Override
						public void onCompleted() {
							subscriber.onCompleted();
						}

						@Override
						public void onError(Throwable e) {
							subscriber.onError(e);
						}

						@Override
						public void onNext(BotResponse rolerResponse) {

						}
					});

		}).observeOn(AndroidSchedulers.mainThread());

	}


	@Override
	public Observable<String> loadProfileImg(String email) {

		return Observable.create(subscriber -> {
			addSubscription(fileService
					.loadProfileImg(email)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Observer<ResponseBody>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(ResponseBody responseBody) {

						}
					}));
		});
	}

}
