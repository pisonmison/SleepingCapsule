package com.example.sleepingcapsule;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import retrofit2.Retrofit;

public class Client {
    OkHttpClient client;
    //implement json lib
    //provide post request for different screens.
    //post for:
    //          seat , back, feet, and position -- DONE
    //          stop chair,
    //          color settings + brighhtness
    //          fav color pick
    //          color data from theme, when theme is picked

public Client()
    {
    client = new OkHttpClient();
    }
    /**
     * This is a simple get request for the dummy server handling seat and light.
     * @param command (seatpart) specifies the relative url endpoint you want to access z.b. "setanglebackrest"
     * @param angle specifies the angle of the corresponding seat part 
     */
    public void simpleGetRequest(String command, int angle){

        String angleString = String.valueOf(angle);


        String url = "http://10.18.2.165:5000/" + command + "/" + angleString;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    System.out.println(myResponse);
                }
            }
        });
    }



    public void colorRequest(String command, int angle){

        String angleString = String.valueOf(angle);


        String url = "http://10.18.2.165:5000/" + command + "/" + angleString;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    System.out.println(myResponse);
                }
            }
        });
    }


}
