package me.unithon.helpmebot.main;

import java.util.List;

import rx.Observable;

/**
 * Created by kinamare on 2017-02-04.
 */

public interface IMainPresenter {
	Observable<Void> sendString(String userId, String chatText);
	Observable<Void> withdrawMoney(String userId, String accountNumber,int amount);
	Observable<List<String>> getList();
}
