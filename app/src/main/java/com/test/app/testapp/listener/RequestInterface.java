package com.test.app.testapp.listener;

import com.test.app.testapp.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("users/")
    Observable<List<User>> register();
}
