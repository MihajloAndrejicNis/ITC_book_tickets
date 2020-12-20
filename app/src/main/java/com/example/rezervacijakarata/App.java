package com.example.rezervacijakarata;

import android.app.Application;

import com.example.rezervacijakarata.datamodels.DetailedEvent;
import com.example.rezervacijakarata.datamodels.Event;

public class App extends Application {

    private static DetailedEvent detailedEvent;
    private static int userId;

    public static DetailedEvent getDetailedEvent() {
        return detailedEvent;
    }

    public static void setDetailedEvent(DetailedEvent detailedEvent) {
        App.detailedEvent = detailedEvent;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        App.userId = userId;
    }
}
