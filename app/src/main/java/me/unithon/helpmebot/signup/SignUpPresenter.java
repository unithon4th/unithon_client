package me.unithon.helpmebot.signup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import me.unithon.helpmebot.R;
import me.unithon.helpmebot.net.presenter.BasePresenter;
import me.unithon.helpmebot.net.service.UserService;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2016-12-18.
 */

public class SignUpPresenter  extends BasePresenter implements ISignUpPresenter{

	private Activity activity;
	private Subscription subscription;
	private boolean isId;
	private boolean isPwd;
	private boolean isShow=true;
	private UserService userService;



	public SignUpPresenter(Activity activity){
		this.activity = activity;
		this.userService = new UserService();

	}

	@Override
	public void registerUser(String email, String pwd) {

		Intent intent = new Intent(activity,SignUpProfileActivity.class);
		intent.putExtra("email",email);
		intent.putExtra("pwd",pwd);
		activity.startActivity(intent);
		activity.finish();

	}

	@Override
	public void check_blank(EditText activity_signup_edt_id, EditText activity_signup_edt_pwd) {

		Observable<CharSequence> observable1 = RxTextView.textChanges(activity_signup_edt_id);
		observable1.map(charSequence -> charSequence.length() > 0).subscribe(new Subscriber<Boolean>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Boolean aBoolean) {
				isId=aBoolean;
				if(isId&&isPwd){
					//view.함수로 해야함함
					activity.findViewById(R.id.activity_signup_btn).setEnabled(true);
					activity.findViewById(R.id.activity_signup_btn).setBackgroundResource(R.color.colorPrimary);
				} else {
					activity.findViewById(R.id.activity_signup_btn).setEnabled(false);
					activity.findViewById(R.id.activity_signup_btn).setBackgroundColor(Color.parseColor("#508b999b"));
				}
			}
		});

		Observable<CharSequence> observable2 = RxTextView.textChanges(activity_signup_edt_pwd);
		observable2.map(charSequence -> charSequence.length() > 7).subscribe(new Subscriber<Boolean>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Boolean aBoolean) {
				isPwd=aBoolean;
				if(isId&&isPwd){
					if(isShow){
						((TextView)activity.findViewById(R.id.activity_signup_tv_show)).setText("Show");
						isShow=false;
					}
					activity.findViewById(R.id.activity_signup_tv_show).setVisibility(View.VISIBLE);
					activity.findViewById(R.id.activity_signup_btn).setEnabled(true);
					activity.findViewById(R.id.activity_signup_btn).setBackgroundResource(R.color.colorPrimary);
				} else {
					activity.findViewById(R.id.activity_signup_tv_show).setVisibility(View.GONE);
					activity.findViewById(R.id.activity_signup_btn).setEnabled(false);
					activity.findViewById(R.id.activity_signup_btn).setBackgroundColor(Color.parseColor("#508b999b"));
				}
			}
		});

	}

	@Override
	public Observable<String> checkDuplicateEmail(String email) {


		return userService
				.isDuplicateEmail(email)
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void resume(){
		super.resume();
	}


}
