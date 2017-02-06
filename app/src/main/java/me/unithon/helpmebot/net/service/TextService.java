package me.unithon.helpmebot.net.service;

import com.google.api.client.json.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.unithon.helpmebot.util.SharePrefUtil;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
							String result = ja.get("res").getAsString();

							String action = ja.getAsJsonObject("data").getAsJsonPrimitive("action").getAsString();

							String query = ja.getAsJsonObject("data").getAsJsonPrimitive("resolvedQuery").getAsString();

							String speech = ja.getAsJsonObject("data").getAsJsonPrimitive("speech").getAsString();


							String checkMoney = ja.get("data").getAsJsonObject().get("parameters")
									.getAsJsonObject().get("money").getAsString();

							if (!checkMoney.isEmpty()) {
								String money = ja.get("data").getAsJsonObject().get("parameters")
										.getAsJsonObject().get("money").getAsString();
								SharePrefUtil.putSharedPreference("money", money);
							}

							String checkName = ja.get("data").getAsJsonObject().get("parameters")
									.getAsJsonObject().get("name").getAsString();
							if (!checkName.isEmpty()) {
								String name = ja.get("data").getAsJsonObject().get("parameters")
										.getAsJsonObject().get("name").getAsString();
								SharePrefUtil.putSharedPreference("name", name);
							}

							SharePrefUtil.putSharedPreference("action", action);
							SharePrefUtil.putSharedPreference("query", query);
							SharePrefUtil.putSharedPreference("speech", speech);


							return result;
						}


					});
		});
	}

	public Observable<Void> withdrawMoney(String userId, String accountNumber, int amount) {

		return Observable.create(subscriber -> {

			getAPI().withdrawMoney("620-229307-041", "223-25334-213", 10000, "신한카드", "2017-02-03T21:00:53.110Z")
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {

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
							String result = ja.get("res").getAsString();

							int price = ja.getAsJsonObject("data").getAsJsonPrimitive("totalAmount").getAsInt();
							SharePrefUtil.putSharedPreference("price", String.valueOf(price));


							return result;
						}

					});
		});
	}

	public Observable<List<String>> getList() {


		return Observable.create(subscriber -> {

			getAPI().getList("620-229307-041")
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ResponseBody>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
						}

						@Override
						public void onNext(ResponseBody responseBody) {

							try {
								List<String> list= parseResult(responseBody.string());
								if (list.get(list.size()-1).equals("success")) {
									subscriber.onNext(list);
									onCompleted();

								} else {
									subscriber.onError(new Throwable());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}


						}


						private List<String> parseResult(String json) {
							List<String> list = new ArrayList<>();
							JsonObject object = (JsonObject) new JsonParser().parse(json);
							String result = object.get("res").getAsJsonPrimitive().getAsString();

							JsonObject obj = (JsonObject) new JsonParser().parse(json);
							JsonObject items = (JsonObject) obj.get("data");
							JsonArray jsonArray = (JsonArray) items.get("records");

							for (int loop = 0; loop < jsonArray.size(); loop++) {
								JsonObject jsonObject = (JsonObject) jsonArray.get(loop);
								String id = jsonObject.getAsJsonPrimitive("_id").getAsString();
								String timestamp = jsonObject.getAsJsonPrimitive("timestamp").getAsString();
								String amount = jsonObject.getAsJsonPrimitive("amount").getAsString();
//								String toId = jsonObject.getAsJsonPrimitive("toId").getAsString();
								String fromId = jsonObject.getAsJsonPrimitive("fromId").getAsString();
								String recordId = jsonObject.getAsJsonPrimitive("recordId").getAsString();

								list.add("보내는 사람 토큰 "+id +" 이체 했던 시간 "+ timestamp +" 이체 금액 "+ amount
										+" 전송한사람 "+ " toI d " +" 출금한 사람 "+fromId +" 출금한 사람 토큰 "+ recordId);

							}
								list.add(result);
							return list;
						}

					});
		});

	}

	public interface TextAPI {
		@FormUrlEncoded
		@POST("/chat/add")
		Observable<ResponseBody> sendText(@Field("userId") String userId, @Field("chatText") String
				chatText);

		@FormUrlEncoded
		@POST("/bank/withdraw")
		Observable<ResponseBody> withdrawMoney(@Field("userId") String userId
				, @Field("told") String accountNumber, @Field("amount") int amount, @Field("name") String bankName
				, @Field("date") String date);


		@GET("/bank/read")
		Observable<ResponseBody> getList(@Query("userId") String userid);


	}
}
