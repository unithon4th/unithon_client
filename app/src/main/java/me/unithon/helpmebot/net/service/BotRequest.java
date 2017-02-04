package me.unithon.helpmebot.net.service;

import me.unithon.helpmebot.vo.MyInfoDAO;

/**
 * Created by kinamare on 2017-02-04.
 */

public class BotRequest {


	private int deviceId;
	private String action;
	private String param;
	private String userId;

	private BotRequest(Builder builder) {
		this.deviceId = builder.deviceId;
		this.action = builder.action;
		this.param = builder.param;
		this.userId = builder.userId;
	}



	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserIdT() {
		return userId;
	}

	public void setUserIdT(String userId) {
		this.userId = userId;
	}

	public static class Builder {
		private int userinfoId;
		private int deviceId;
		private String action;
		private String param;
		private String userId;

		public Builder() {
			this.userId = MyInfoDAO.getInstance().getMyUserInfo().getId();
			this.deviceId = MyInfoDAO.getInstance().getDeviceId();
		}

		public Builder setParam(String param) {
			this.param = param;
			return this;
		}

		public Builder setAction(String action) {
			this.action = action;
			return this;
		}

		public BotRequest build() {
			return new BotRequest(this);
		}


	}


}

