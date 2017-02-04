package me.unithon.helpmebot.main;

import me.unithon.helpmebot.net.presenter.BasePresenter;
import me.unithon.helpmebot.net.service.TextService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by kinamare on 2017-02-04.
 */

public class MainPresenter extends BasePresenter implements IMainPresenter {

	private TextService textService;

	public MainPresenter(){
		textService = new TextService();
	}


	@Override
	public Observable<String> sendString(String userId, String chatText) {

		return textService
				.sendText(userId,chatText)
				.observeOn(AndroidSchedulers.mainThread());
	}

}
