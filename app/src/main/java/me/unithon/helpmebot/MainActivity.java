package me.unithon.helpmebot;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.unithon.helpmebot.vo.Message;

public class MainActivity extends AppCompatActivity {
	private ArrayList<Message> arrList = new ArrayList<>();
	ChatMessageAdapter adapter;
	private int count = 0;

	// Binding view
	@BindView(R.id.custom_toolbar)
	Toolbar mToolBar;

	@BindView(R.id.rv_chat)
	RecyclerView rvChat;

	@BindView(R.id.btn_send)
	Button btn_send;

	@OnClick(R.id.btn_send) void click(View v) {
		int p = count%4;
		arrList.add(new Message(p, "message"+(count++)));
		adapter.notifyDataSetChanged();
		rvChat.scrollToPosition(count-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		// actionbar
		{
			setSupportActionBar(mToolBar);
			final ActionBar actionBar = getSupportActionBar();
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
			actionBar.setDisplayHomeAsUpEnabled(true);

			actionBar.setDisplayShowHomeEnabled(true); // show or hide the default home button
			actionBar.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
			actionBar.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
		}

		// adapter
		{
			adapter = new ChatMessageAdapter(getApplicationContext(),arrList);
			LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
			layoutManager.setStackFromEnd(true);
			layoutManager.setSmoothScrollbarEnabled(true);
			rvChat.setAdapter(adapter);
			rvChat.setLayoutManager(layoutManager);
		}


	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
