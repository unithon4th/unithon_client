package me.unithon.helpmebot.net.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kinamare on 2017-02-04.
 */


public class BasePresenter implements IBasePresenter {

	protected CompositeSubscription subscriptions;
	protected boolean active;

	public BasePresenter() {
		subscriptions = new CompositeSubscription();
		active = true;
	}


	protected void addSubscription(Subscription subscription) {

		if (subscriptions != null) {
			subscriptions.add(subscription);
		}

	}

	@Override
	public void destroy() {
		active = false;
		if (subscriptions != null) {
			subscriptions.unsubscribe();
		}
	}

	@Override
	public void resume() {
		subscriptions = new CompositeSubscription();
		active = true;
	}
}
