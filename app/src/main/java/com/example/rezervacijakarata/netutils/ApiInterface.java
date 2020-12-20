package com.example.rezervacijakarata.netutils;

import com.example.rezervacijakarata.datamodels.BookResponse;
import com.example.rezervacijakarata.datamodels.DetailedEvent;
import com.example.rezervacijakarata.datamodels.Event;
import com.example.rezervacijakarata.datamodels.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("auth")
    Call<User> postUser(@Query("email") String email);

    @GET("users/{user_id}/events")
    Call<List<Event>> getEvents(@Path("user_id") int userId);

    @GET("users/{user_id}/events/{event_id}")
    Call<DetailedEvent> getEventDetails(@Path("user_id") int userId, @Path("event_id") int eventId);

    @POST("users/{user_id}/events/{event_id}/book")
    Call<List<BookResponse>> bookTicket(@Path("user_id") int userId, @Path("event_id") int eventId, @Query("tickets") String numberOfTickets);

    @POST("users/{user_id}/events/{event_id}/unbook")
    Call<BookResponse> unbookTicket(@Path("user_id") int userId, @Path("event_id") int eventId);

}
