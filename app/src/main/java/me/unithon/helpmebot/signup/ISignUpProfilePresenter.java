package me.unithon.helpmebot.signup;


import java.io.File;

import me.unithon.helpmebot.vo.User;
import rx.Observable;

/**
 * Created by kinamare on 2016-12-17.
 */

public interface ISignUpProfilePresenter {
	Observable<User> signUp(String email, String pwd, String account,String bank);

	Observable<String> uploadProfileImg(File file);

	Observable<Void> setProfileImg(String profileImgUrl);

	Observable<String> loadProfileImg(String email);
}
