package com.example.demoapplication.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostRequest extends AsyncTask<String, Void, String> {
        JSONObject jsonObject = new JSONObject();

        private MyAsyncTaskListener mListener;

        Context context ;

        public PostRequest(Context context) {
            this.context = context;
        }

        final public void setListener(MyAsyncTaskListener listener) {

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


                RequestBody body = RequestBody.create(JSON, params[1]);
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(body)
//                        .addHeader("userKey", params[2])
                        .addHeader("accept","*/*")
                        .addHeader("content-type", "application/json")
                        .addHeader("Authorization", "Bearer "+params[2])
                        .build();
                response = client.newCall(request).execute();
                if (response.code() == 403) {
                    result = "fail";
                }else if(response.code() == 401)
                {
                    Log.e("Response","Unauthorized Request. Access to API is forbidden.");
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

