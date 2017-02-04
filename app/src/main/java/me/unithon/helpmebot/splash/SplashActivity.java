package me.unithon.helpmebot.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.unithon.helpmebot.R;
import me.unithon.helpmebot.login.LogInActivity;
import me.unithon.helpmebot.main.MainActivity;
import me.unithon.helpmebot.util.NetUtil;
import me.unithon.helpmebot.util.SharePrefUtil;

public class SplashActivity extends Activity {

	private static final String TAG = SplashActivity.class.getSimpleName();

	@BindView(R.id.splash_title)
	TextView splash_title;
	@BindView(R.id.splash_subTitle)
	TextView splash_subTitle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ButterKnife.bind(this);

		splash_title.setVisibility(View.INVISIBLE);
		splash_subTitle.setVisibility(View.INVISIBLE);

		if (NetUtil.isNetworkAvailable(getApplicationContext())) {
			Intent intent = new Intent(this, LogInActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			boolean isLoggedIn = SharePrefUtil.getBooleanSharedPreference("isLoggedIn");
			if (isLoggedIn) {

				new Handler().postDelayed(() -> {
					Intent loggedInIntent = new Intent(this, MainActivity.class);
					loggedInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(loggedInIntent);
					finish();
				}, 3000);


			} else {

				new Handler().postDelayed(() -> {
					splash_title.setVisibility(View.VISIBLE);
				}, 1000);

				new Handler().postDelayed(() -> {
					splash_subTitle.setVisibility(View.VISIBLE);
				}, 2000);
				new Handler().postDelayed(() -> {

					startActivity(intent);
					finish();
				}, 3000);
			}


		} else {
			Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해보세요", Toast.LENGTH_LONG).show();
			super.onPause();
		}


//		Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				goToLoginActivity();
//			}
//		}, 1200);
	}

//	private void goToLoginActivity() {
//		Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(intent);
//	}


}
