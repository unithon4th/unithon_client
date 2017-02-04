package me.unithon.helpmebot.net.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.unithon.helpmebot.net.serialization.BotResponse;
import me.unithon.helpmebot.net.serialization.BotResponseDeserializer;
import me.unithon.helpmebot.net.service.cookies.AddCookiesInterceptor;
import me.unithon.helpmebot.net.service.cookies.ReceivedCookiesInterceptor;
import me.unithon.helpmebot.util.MyApplication;
import me.unithon.helpmebot.util.NetUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kinamare on 2017-02-04.
 */


public class BaseService<T> {


	public static final String BASE_URL = "http://52.79.117.255";

	private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
		Response originalResponse = chain.proceed(chain.request());
		if (NetUtil.isNetworkAvailable(MyApplication.getInstance().getContext())) {
			int maxAge = 5; // read from cache for 5 sec
			return originalResponse.newBuilder()
					.header("Cache-Control", "public, max-age=" + maxAge)
					.build();
		} else {
			int maxStale = 60 * 60 * 24 * 3; // tolerate 3-day stale
			return originalResponse.newBuilder()
					.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
					.build();

		}

	};
	Retrofit retrofit;
	T service;
	OkHttpClient.Builder httpClient;
	int counter = 0;

	public BaseService(final Class<T> clazz) {

		GsonBuilder gsonBuilder = new GsonBuilder();

		// Adding custom deserializers
		gsonBuilder.registerTypeAdapter(BotResponse.class, new BotResponseDeserializer());
		Gson myGson = gsonBuilder.create();

		AddCookiesInterceptor addCookiesInterceptor = new AddCookiesInterceptor(MyApplication.getInstance().getContext());
		ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor(MyApplication.getInstance().getContext());

		OkHttpClient httpClient = new OkHttpClient.Builder()
				.addNetworkInterceptor(addCookiesInterceptor)
				.addInterceptor(receivedCookiesInterceptor)
				.build();


		retrofit = new Retrofit.Builder()
				.client(httpClient)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(myGson))
				.baseUrl(BASE_URL)
				.build();

		service = retrofit.create(clazz);
	}

	protected static String getStatusResult(String json) {
		try {

			JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
			String result = ja.get("result").getAsString();

			return result;
		} catch (Exception e) {
			System.out.print(e);
		}
		return "false";
	}



	private void setTokenInterceptor(String authToken) {
		httpClient.addInterceptor(chain -> {

			Request original = chain.request();

			Request.Builder reqBuilder = original.newBuilder()
					.header("Authorization", TokenTYPE.BEARER.type + " " + authToken)
					.method(original.method(), original.body());

			Request req = reqBuilder.build();

			return chain.proceed(req);
		});
	}





	private enum TokenTYPE {
		BEARER("Bearer");
		private String type;

		TokenTYPE(String type) {
			this.type = type;
		}
	}

	public T getAPI() {
		return service;
	}

}

