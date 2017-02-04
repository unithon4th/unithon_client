package me.unithon.helpmebot.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import me.unithon.helpmebot.R;
import me.unithon.helpmebot.setting.SettingActivity;
import me.unithon.helpmebot.util.SharePrefUtil;
import rx.Subscriber;

/**
 * Created by kinamare on 2017-02-05.
 */


public class MainActivity extends AppCompatActivity implements IMainView {
	ListView m_ListView;
	CustomAdapter m_Adapter;

	private ACProgressFlower dialog;

	@BindView(R.id.btn_send)
	Button btn_send;

	@BindView(R.id.edit_text)
	EditText edit_text;

	private IMainPresenter presenter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setToolbar();

		m_Adapter = new CustomAdapter();

		m_ListView = (ListView) findViewById(R.id.listView1);

		m_ListView.setAdapter(m_Adapter);

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String date = sdf.format(calendar.getTime());

		//1번 나
		//0번 서버
		//2 날짜
		m_Adapter.add(date, 2);

		sendToServer();


	}

	private void sendToServer() {
		btn_send.setOnClickListener(v -> {

			String text = edit_text.getText().toString();
			m_Adapter.add(text, 1);
			m_Adapter.notifyDataSetChanged();
			showLoadingBar();

			presenter = new MainPresenter();
			presenter.sendString("kozy@naver.com", text)
					.subscribe(new Subscriber<String>() {
						@Override
						public void onCompleted() {
							unsubscribe();

						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
						}

						@Override
						public void onNext(String s) {

							String name = SharePrefUtil.getSharedPreference("name");
							String money = SharePrefUtil.getSharedPreference("money");
							String query = SharePrefUtil.getSharedPreference("query");
							String speech = SharePrefUtil.getSharedPreference("speech");
							m_Adapter.add(speech, 0);
							m_Adapter.notifyDataSetChanged();
							hideLoadingBar();

							onCompleted();

						}
					});

		});


	}

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		EditText editText = (EditText) findViewById(R.id.search_edt);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		ImageView imageView3 = (ImageView) findViewById(R.id.custom_toolbar_right_iv);
		ImageView imageView2 = (ImageView) findViewById(R.id.custom_toolbar_right_iv2);
		ImageView imageView4 = (ImageView) findViewById(R.id.custom_toolbar_right_iv3);
		imageView2.setImageResource(R.drawable.search);
		imageView.setImageResource(R.drawable.setting_icon);
		textView.setTextColor(Color.WHITE);
		textView.setText("돈과 돈독해지는 TALK 돈톡!");
		setSupportActionBar(toolbar);

		imageView.setOnClickListener(v -> {
			startActivity(new Intent(this, SettingActivity.class));
		});

		imageView2.setOnClickListener(v -> {
			textView.setVisibility(View.GONE);
			editText.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.GONE);
			imageView2.setVisibility(View.GONE);
			imageView3.setImageResource(R.drawable.up_act);
			imageView4.setImageResource(R.drawable.down_act);


		});


	}

	void addData(String text, int type) {
		m_Adapter.add(text, type);
		m_Adapter.notifyDataSetChanged();
	}

	@Override
	public void showLoadingBar() {
		dialog = new ACProgressFlower.Builder(this)
				.direction(ACProgressConstant.DIRECT_CLOCKWISE)
				.themeColor(Color.WHITE)
				.fadeColor(Color.DKGRAY).build();
		dialog.show();
	}

	@Override
	public void hideLoadingBar() {
		if (dialog != null)
			dialog.dismiss();
	}

}