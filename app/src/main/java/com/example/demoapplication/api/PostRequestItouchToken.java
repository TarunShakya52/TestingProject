package com.example.demoapplication.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.FormUrlEncoded;

public class PostRequestItouchToken extends AsyncTask<String, Void, String> {
    JSONObject jsonObject = new JSONObject();

    private PostRequestItouchToken.MyAsyncTaskListener mListener;

    Context context ;

    public PostRequestItouchToken(Context context) {
        this.context = context;
    }


    final public void setListener(PostRequestItouchToken.MyAsyncTaskListener listener) {

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

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("grant_type", "client_credentials")
                    .addFormDataPart("client_id","33OkryzDZsLh5RcSvJ6dPWNMBgUUDyOLIZlb3POtWOcenQQ1hBGVzO-8-CPLgT8AzopagDIMb_JdtichASQbPw==")
                    .addFormDataPart("client_secret","lrFxI-iSEg8tV9-1yeugARhtM_DGzNiuspVKfRlk_xvOfDBNHiuTuwF8Bur9fRUuAVCiGLIs90j2vlQd5AjSPb03UdsUEJpF")
                    .build();

            Request request = new Request.Builder()
                    .url(params[0])
                    .post(requestBody)
                    .addHeader("accept", "application/json")
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .build();
            response = client.newCall(request).execute();
            if (response.code() == 403) {
                result = "fail";
            }else if(response.code() == 401)
            {
                result = "unauthoeized";
            }else {
                result = response.body().string();
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


    public interface MyAsyncTaskListener {
        void onPreExecuteConcluded();

        void onPostExecuteConcluded(String result);
    }

}
