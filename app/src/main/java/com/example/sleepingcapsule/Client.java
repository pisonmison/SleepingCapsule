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
     *              • http://10.18.12.91:5000/setanglebackrest/<int:angle>
     *              • http://10.18.12.92:5000/setangleseating/<int:angle>
     *              http://10.18.12.92:5000/setanglefootrest/<int:angle>
     */
    public void seatGetRequest(String command, int angle){

        String angleString = String.valueOf(angle);

        String baseURL= "http://10.18.2.165:5000/"; //change if needed
        String url = baseURL + command + "/" + angleString;

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

    /**
     * This methods gets the current data from light screen and sends it to server
     *
     * @param command server api command
     * @param colordata color data as rgb int
     *           • http://10.18.12.93:5000/setlightseat/<int:color> (color: R * (256)^2 + G * 256 + B with R, G, B in [0:255])
     *           • http://10.18.12.93:5000/setlightinterior/<int:color> (color: R * (256)^2 + G * 256 + B with R, G, B in [0:255])
     *
     */
    //sends rgb color data to server
    public void colorGetRequest(String command, int colordata){

       //get color data

        String colorData = "";
        String baseURL= "http://10.18.2.165:5000/"; //change if needed
        String url = baseURL + command + "/" + colorData;

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

    //sends command to stop the chair to server

    public void stopChairGetRequest(String command, int angle){

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
