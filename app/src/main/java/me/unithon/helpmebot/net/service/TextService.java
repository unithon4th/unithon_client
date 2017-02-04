package me.unithon.helpmebot.net.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.unithon.helpmebot.util.SharePrefUtil;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kinamare on 2017-02-04.
 */

public class TextService extends BaseService {

	public TextService() {
		super(TextAPI.class);
	}

	@Override
	public TextAPI getAPI() {
		return (TextAPI) super.getAPI();
	}

	public Observable<Void> sendText(String userId, String chatText) {
		return Observable.create(subscriber -> {
			getAPI().sendText(userId, chatText)
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {
							unsubscribe();
						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
						}

						@Override
						public void onNext(ResponseBody responseBody) {

							try {
								String result = parseParams(responseBody.string());
								if (result.equals("success")) {
									subscriber.onNext(null);

								} else {
									subscriber.onError(new Throwable());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						private String parseParams(String json) {

							JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
							String result = ja.get("res").getAsString();

							String action = ja.getAsJsonObject("data").getAsJsonPrimitive("action").getAsString();

							String query = ja.getAsJsonObject("data").getAsJsonPrimitive("resolvedQuery").getAsString();

							String speech = ja.getAsJsonObject("data").getAsJsonPrimitive("speech").getAsString();

//
//							Boolean checkMoney = ja.get("data").getAsJsonObject().get("parameters")
//									.getAsJsonObject().get("money").isJsonNull();
//							if(checkMoney){
//								String money = ja.get("data").getAsJsonObject().get("parameters")
//										.getAsJsonObject().get("money").getAsString();
//								SharePrefUtil.putSharedPreference("money", money);
//							}
//
//							Boolean checkName =  ja.get("data").getAsJsonObject().get("parameters")
//									.getAsJsonObject().get("name").isJsonNull();
//							if(checkName){
//								String name = ja.get("data").getAsJsonObject().get("parameters")
//										.getAsJsonObject().get("name").getAsString();
//								SharePrefUtil.putSharedPreference("name", name);
//							}

							SharePrefUtil.putSharedPreference("action", action);
							SharePrefUtil.putSharedPreference("query", query);
							SharePrefUtil.putSharedPreference("speech", speech);


							return result;
						}


					});
		});
	}

	public interface TextAPI {
		@FormUrlEncoded
		@POST("/chat/add")
		Observable<ResponseBody> sendText(@Field("userId") String userId, @Field("chatText") String
				chatText);

	}
}
