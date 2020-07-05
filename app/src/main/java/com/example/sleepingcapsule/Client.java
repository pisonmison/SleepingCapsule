package com.example.sleepingcapsule;

import android.content.Context;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//implement json lib, okhttp, retrofit
//provide post request for different screens.
//get for:
//          seat , back, feet, and position -- DONE
//          stop chair,  -- DONE
//          color settings -- DONE
//          fav color pick -- DONE
//          color data from theme, when theme is picked -- MISSING
//          get themes with json -> use retrofit.

public class Client {


    public Context mContext;
    OkHttpClient client;
    public ArrayList<Themes> jsonListThemes;



public Client()
    {
    client = new OkHttpClient();
    jsonListThemes = new ArrayList<Themes>();

    }



    public void getThemesFromServerUsingRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.18.12.95:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        retrofit2.Call<List<Themes>> call = apiInterface.getPosts();

        call.enqueue(new retrofit2.Callback<List<Themes>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Themes>> call, retrofit2.Response<List<Themes>> response) {

                //check for false responses
                if(!response.isSuccessful()){
                    System.out.println("ERROR_1:"+ response.code());
                    return;
                }

                List<Themes> jsonThemes = response.body();

                for(Themes theme : jsonThemes){
                    System.out.println("JAMAN"); // print in console for testing
                }


            }

            @Override
            public void onFailure(retrofit2.Call<List<Themes>> call, Throwable t) {
                System.out.println("ERROR_2:"+ t.getMessage());
            }
        });



    }



    /**
     * Using Volley we parse the JSON themes  file from server and return the json at the end to use in other activities.
     *for(int i = 0; i < response.length(); i++) {
     *                         try {
     *                             JSONObject jsonTheme = response.getJSONObject(i);
     *
     *                             Themes theme = new Themes();
     *                             theme.setmTitle(jsonTheme.getString("title"));
     *                             theme.setmDescription(jsonTheme.getString("description"));
     *                             theme.setmImage(jsonTheme.getString("image"));
     *                             theme.setmMusic(jsonTheme.getString("music"));
     *                             theme.setmColor(jsonTheme.getString("color"));
     *                             theme.setmId(jsonTheme.getString("id"));
     *
     *
     *                             jsonListThemes.add(theme);
     */
public void getThemesFromServer(){

    String url = "http://10.18.2.165:5000/";

    JsonArrayRequest request = new JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
            //when request succesfull
            new com.android.volley.Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonTheme = response.getJSONObject(i);

                            Themes theme = new Themes();
                            theme.setmTitle(jsonTheme.getString("title"));
                            theme.setmDescription(jsonTheme.getString("description"));
                            theme.setmImage(jsonTheme.getString("image"));
                            theme.setmMusic(jsonTheme.getString("music"));
                            theme.setmColor(jsonTheme.getString("color"));
                            theme.setmId(jsonTheme.getInt("id"));

                            System.out.println(theme);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }




                }
            }, new com.android.volley.Response.ErrorListener() {
        //call on error
        @Override
        public void onErrorResponse(VolleyError error) {
         error.printStackTrace();
        }
    });

    SeatScreen.mQueue.add(request);

}






    /**
     * This is a simple get request for the dummy server handling seat and light.
     * @param command (seatpart) specifies the relative url endpoint you want to access z.b. "setanglebackrest"
     * @param angle specifies the angle of the corresponding seat part
     *              Dummy: "http://10.18.2.165:5000/"
     *
     *             • http://10.18.12.91:5000/setanglebackrest/<int:angle>
     *              • http://10.18.12.92:5000/setangleseating/<int:angle>
     *              http://10.18.12.92:5000/setanglefootrest/<int:angle>
     */
    public void seatGetRequest(String command,String id, int angle){

        String angleString = String.valueOf(angle);
        String endBit = id + ":5000/";
        String baseURL= "http://10.18.12A." + endBit; //change if needed
        String url = baseURL + command + "/" + angleString;

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("SENDING UNSUCSESSFULL");
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    System.out.println(myResponse);
                }
                else{
                    System.out.println("NOT SUCCESFULLL");
                }
            }
        });
    }

    /**
     * This methods gets the current data from light screen and sends it to server
     *
     * @param command server api command
     * @param r,g,b  color data as rgb int
     *
     *            Dummy Server: http://10.18.2.165:5000/....
     *
     *            Real Server:
     *           • http://10.18.12.93:5000/setlightseat/<int:color> (color: R * (256)^2 + G * 256 + B with R, G, B in [0:255])
     *           • http://10.18.12.93:5000/setlightinterior/<int:color> (color: R * (256)^2 + G * 256 + B with R, G, B in [0:255])
     *
     */

    //sends rgb color data to server
    public void colorGetRequest(String command, int r, int g, int b){

       int temp = r * ((256)*(256)) + g * 256 + b;

       //get color data

        String colorData = String.valueOf(temp);
        String baseURL= "http://10.18.12.93:5000A/"; //change if needed
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






    /**
     * Those 2 methods send the corresponding stop request for corresponding chair parts to server
     *                http://10.18.12.91:5000/setstopbackrest
     *                http://10.18.12.92:5000/setstopfootrest
     *               http://10.18.12.92:5000/setstopseating
     */

    public void stopChairGetRequest1(){




        String url = "http://10.18.12.91:5000A/setstopbackrest";

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
     * This method sends stop-requests for seating and footrest
     * @param command can be setstopseating | setstopfootrest
     */

    public void stopChairGetRequest2(String command){




        String url = "http://10.18.12.92:5000A/" + command;

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
