package me.unithon.helpmebot.main;

import rx.Observable;

/**
 * Created by kinamare on 2017-02-04.
 */

public interface IMainPresenter {
	Observable<Void> sendString(String userId, String chatText);
}
