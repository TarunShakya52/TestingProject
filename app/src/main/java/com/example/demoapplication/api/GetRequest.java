package com.example.demoapplication.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.demoapplication.response.DeviceInfoResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetRequest  extends AsyncTask<String, Void, String> {
    JSONObject jsonObject = new JSONObject();

    private MyAsyncTaskkGetListener mListener;
    Context context;

    public GetRequest(Context context) {
        this.context = context;
    }

    final public void setListener(MyAsyncTaskkGetListener listener) {
        mListener = listener;
    }

    @Override
    final protected void onPreExecute() {
        // common stuff
        if (mListener != null)
            mListener.onPreExecuteConcluded();
    }

    @Override
    final protected String doInBackground(String... params) {
        // do stuff, common to both activities in here

        Response response = null;
        String result = null;
        try {
            MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();


            String token;

            if (params[3] != null) {
                token = params[3];
            }
            RequestBody body = RequestBody.create(JSON, params[1]);
            Request request = new Request.Builder()
                    .url(params[0])
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer "+params[3])
                    .build();
            Log.d("Authorization", "=" + params[3]);
            Log.d("request--", String.valueOf(request.url()));
            response = client.newCall(request).execute();
            if (response.code() == 403) {
                result = "fail";
            } else if (response.code() == 401) {
                result = Objects.requireNonNull(response.body()).string();
                Log.e("errorbody",result);

            } else if (response.code() == 200){
                result = response.body().string();
//                result = "Success";
            }else {
                Log.e("checkbody", String.valueOf(response));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    @Override
    final protected void onPostExecute(String result) {
        // common stuff

        if (mListener != null)
            mListener.onPostExecuteConcluded(result);
    }


    public interface MyAsyncTaskkGetListener {
        void onPreExecuteConcluded();

        void onPostExecuteConcluded(String result);
    }
}
