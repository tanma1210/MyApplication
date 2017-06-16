package com.example.user.myapplication;

/**
 * Created by user on 2017/06/12.
 */

public class Weather {

    class Text {
        String text;
        String publicTime;
    }

    class Location {
        String city;
        String area;
        String prefecture;
    }

    class IntTest {
        ImageSize image;
    }

    class ImageSize {
        int width;
        int height;
    }

    MainActivity.PinpointLocation[] pinpointLocations;
    Location location;
    IntTest copyright;
    Text description;

    // Getter と Setter で処理する
    private String publicTime;

    String getPublicTime() {
        return publicTime.substring(0, 10);
    }
    void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

}
