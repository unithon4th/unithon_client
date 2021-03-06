package me.unithon.helpmebot.net.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import me.unithon.helpmebot.net.serialization.BotResponse;
import me.unithon.helpmebot.vo.MyInfoDAO;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kinamare on 2016-12-29.
 */

public class FileService extends BaseService {

	public FileService() {
		super(FileAPI.class);
	}

	@Override
	public FileAPI getAPI() {
		return (FileAPI) super.getAPI();
	}


	public Observable<String> uploadProfileImg(MultipartBody.Part file) {

		return Observable.create(subscriber -> {

			if (file == null) {
				subscriber.onNext("");
				subscriber.onCompleted();
			} else {
				getAPI()
						.uploadProfileImg(file,MyInfoDAO.getInstance().getEmail())
						.subscribeOn(Schedulers.io())
						.retry((integer, throwable) -> {
							if(integer < 3) {
								return true;
							}
							return throwable instanceof  IllegalStateException;
						})
						.subscribe(new Subscriber<ResponseBody>() {
							@Override
							public void onCompleted() {
								subscriber.onCompleted();
							}

							@Override
							public void onError(Throwable e) {
								subscriber.onNext("");
							}

							@Override
							public void onNext(ResponseBody responseBody) {
								try {
									String json = responseBody.string();
									if (getStatusResult(json) == "true") {
										MyInfoDAO.getInstance().setPicUrl(parseResult(json));
										subscriber.onNext(parseResult(json));
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}



							private String parseResult(String json) {
								JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
								String result = ja.get("imageUrl").getAsString();
								return result;
							}

						});
			}


		});
	}

	public Observable<ResponseBody> loadProfileImg(String email){

		return getAPI()
				.loadProfileImg(email)
				.subscribeOn(Schedulers.io());

	}



	private interface FileAPI {
		@Multipart
		@POST("/files")
		Observable<BotResponse> uploadFile(
				@Query("userId") String userId,
				@Query("fileType") String fileType,
				@Part MultipartBody.Part file);

		@GET("/sign/upload/{email}")
		Observable<ResponseBody> loadProfileImg(@Path("email") String email);


		@Multipart
		@POST("sign/upload/{email}")
		Observable<ResponseBody> uploadProfileImg(
				@Part MultipartBody.Part file,
				@Path("email") String email);

	}

}

