package me.unithon.helpmebot.net.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import me.unithon.helpmebot.net.serialization.BotResponse;
import me.unithon.helpmebot.vo.MyInfoDAO;
import me.unithon.helpmebot.vo.User;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kinamare on 2017-02-04.
 */


public class UserService extends BaseService {

	public static boolean DEBUG = false;

	private String userId;
	private String name;

	public UserService() {
		super(UserAPI.class);
	}

	@Override
	public UserAPI getAPI() {
		return (UserAPI) super.getAPI();
	}


	public Observable<User> signUp(User user) {


		return Observable.create(subscriber -> {

			getAPI()
					.signUp(user.getName(), user.getEmail(), user.getPassword())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							subscriber.onCompleted();
							subscriber.unsubscribe();

						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
						}

						@Override
						public void onNext(ResponseBody responseBody) {

							try {
								String result = parseParams(responseBody.string());
								if (result.equals("true")) {
									MyInfoDAO.getInstance().setUserId(userId);
									MyInfoDAO.getInstance().setName(name);
									MyInfoDAO.getInstance().saveAccountInfo(userId, user.getEmail(), user.getPassword(), user.getName(), "NULL");
									subscriber.onNext(user);

								} else {
									subscriber.onError(new Throwable());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						private String parseParams(String json) {
							JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
							String result = ja.get("result").getAsString();
							userId = ja.get("id").getAsString();
							name = ja.get("name").getAsString();

							return result;
						}

					});

		});
	}

	public Observable<BotResponse> setProfilePhotos(String profileImgUrl) {

		User userInfo = MyInfoDAO.getInstance().getMyUserInfo();
		userInfo.setPicture_url(profileImgUrl);

		BotRequest req = new BotRequest.Builder()
				.setParam(new Gson().toJson(userInfo)).build();

		return getAPI().setProfilePhotos(req)
				.subscribeOn(Schedulers.io());
	}


	public Observable<String> isDuplicateEmail(String email) {

		return Observable.create(subscriber -> {
			getAPI()
					.isDuplicateEmail(email)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							subscriber.onCompleted();
							subscriber.unsubscribe();
						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(ResponseBody responseBody) {
							try {
								String result = parseParams(responseBody.string());
								if (result.equals("true")) {
									subscriber.onNext(result);
								} else {
									subscriber.onError(new Throwable());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						private String parseParams(String json) {
							JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
							String param = ja.get("result").getAsString();
							return param;
						}

					});

		});
	}

	public Observable<String> signIn(String email, String pwd) {
		return Observable.create(subscriber -> {
			getAPI().signIn(email, pwd)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							subscriber.onCompleted();
							subscriber.unsubscribe();

						}

						@Override
						public void onError(Throwable e) {
							subscriber.onError(e);
						}

						@Override
						public void onNext(ResponseBody responseBody) {
							try {
								String result = parseParams(responseBody.string());
								if (result.equals("true")) {
									subscriber.onCompleted();

								} else {
									subscriber.onError(new Throwable());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						private String parseParams(String json) {
							JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
							String result = ja.get("result").getAsString();
							String name = ja.get("name").getAsString();
							String email = ja.get("email").getAsString();
							String id = ja.get("id").getAsString();

							MyInfoDAO.getInstance().loginAccountInfo(id, email, name, "NULL");
							return result;
						}


					});

		});

	}


	public interface UserAPI {

		@FormUrlEncoded
		@POST("/auth/signup")
		Observable<ResponseBody> signIn(@Field("email") String email, @Field("password") String password);

		@GET("/sign/duplitcation")
		Observable<ResponseBody> isDuplicateEmail(@Query("email") String email);

		@PUT("/sign/photos")
		Observable<BotResponse> setProfilePhotos(@Body BotRequest req);



		@FormUrlEncoded
		@POST("/sign/up")
		Observable<ResponseBody> signUp(@Field("name") String name, @Field("email") String email, @Field("password") String password);
	}
}
