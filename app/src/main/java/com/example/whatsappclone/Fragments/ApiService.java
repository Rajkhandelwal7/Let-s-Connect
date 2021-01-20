package com.example.whatsappclone.Fragments;

import com.example.whatsappclone.Notifications.MyResponse;
import com.example.whatsappclone.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAvR4qIrc:APA91bGjOcx7-Qv2tgxF0t4VbGnGbhaxWqbIo7Imaq2W1u61LDis3uJ4-rfWyEbM_WgiUpOaVDY0ZNsBt1rdzA26RR5CduSHCMkv59wJoaKoV5pXJ6wlxxAWWR9CHTJt4SZKgO4Ep51N"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
