package com.example.demoapplication.DataLoader;

import android.content.Context;
import android.util.Log;

import com.example.demoapplication.Contract.SaveSEZAddress_Contract;
import com.example.demoapplication.api.PatchRequest;
import com.example.demoapplication.api.PostRequest;
import com.example.demoapplication.request.AddSEZAdress_Request;
import com.example.demoapplication.response.AddSezAddressResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditSEZAddressDataLoader {

    private final Context context;

    private Gson gson;

    public EditSEZAddressDataLoader(Context context){
        this.context = context;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    public void editSEZAddress(SaveSEZAddress_Contract.ResponseDataCallBack responseDataCallBack, String url, AddSEZAdress_Request address) {
        try {
            saveEditSEZAddress(context, responseDataCallBack,url,address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveEditSEZAddress(Context context, SaveSEZAddress_Contract.ResponseDataCallBack responseDataCallBack, String url, AddSEZAdress_Request address) {
        PatchRequest aTask = new PatchRequest(context);
        aTask.setListener(new PatchRequest.MyAsyncTaskListener() {
            @Override
            public void onPreExecuteConcluded() {
                //Loader Will come here
            }

            @Override
            public void onPostExecuteConcluded(String result) {
                try {
                    responseDataCallBack.onStopLoader();
                    if (result != null) {
                        AddSezAddressResponse addSezAddressResponse = gson.fromJson(result, AddSezAddressResponse.class);
                        if (addSezAddressResponse.getStatus().getMessage().equalsIgnoreCase("Success")) {
                            responseDataCallBack.onSuccess(addSezAddressResponse);
                        } else {
                            responseDataCallBack.onError(addSezAddressResponse.getStatus().getMessage());
                        }
                    } else {
                        responseDataCallBack.onError("Some Error Occur");
                    }
                } catch (Exception e) {
                    responseDataCallBack.onStopLoader();
                    responseDataCallBack.onError(e.getMessage());
                    Log.e("checkApi",e.getMessage());
                }

            }
        });
        String jsonInString = gson.toJson(address);
        String token = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIwNGE5MWJhZS04NzcyLTQ4MmMtODc2YS1jOTYwN2NlMzhhZmIiLCJpZCI6MjExLCJtb2JpbGUiOiI4MDg4NDAzNDMyIiwicm9sZSI6IkFETUlOIiwicGVybWlzc2lvbnMiOlsidm1fYWxsb3dlZCIsInByb2R1Y3RfYWxsb3dlZCIsIm9yZGVyc19hbGxvd2VkIiwidXNlcl9hbGxvd2VkIiwidm1fc2V0dGluZ3MiLCJ2bV9zYWxlcyIsIm5vdGlmaWNhdGlvbl9lbmFibGVkIiwiYWJzb2x1dGVfc2FsZXMiLCJ2ZW5kb3JfYWxsb3dlZCIsIndhbGxldF9oaXN0b3J5Iiwidmlld19icmFuZHMiLCJjcmVhdGVfYnJhbmQiLCJ2aWV3X2Jhc2UiLCJ2aWV3X3ZhcmlhbnRzIiwibWFwX3ZlbmRvciIsInZpZXdfcHJvZHVjdHMiLCJjcmVhdGVfcHJvZHVjdHMiLCJjcmVhdGVfYmFzZSIsImNyZWF0ZV92YXJpYW50IiwiY3JlYXRlX3ZlbmRvciIsInZpZXdfdmVuZG9yIiwidmlld191c2VycyIsIm1hcF91c2VyIiwiYXR0ZW5kYW5jZSIsImtpdHRpbmdfcmVmaWxsaW5nIiwiYXVkaXRfbWFjaGluZSIsInN0b2NraW5nIiwic3BhcmVfcGFydHMiLCJlX2RhYWxjaGluaSIsInNsb3RfdXRpbGl6YXRpb24iLCJoZWFsdGhfYWxlcnQiLCJ0cm91Ymxlc2hvb3RfZ3VpZGUiLCJyYWlzZV90aWNrZXQiLCJzZWxmX2Fzc2Vzc21lbnQiLCJyZWNjZSIsInZtX2luc3RhbGxhdGlvbiIsInZpZXdfdGFnIiwiYWN0aXZhdGVfdGFnIiwibWVhbHNfbWVudSIsInZtX3BhcmFtZXRlcnMiLCJzdXBwb3J0X25vdGlmaWNhdGlvbiIsInNsb3RfcmVwYWlyIiwicXVpY2tfdW5ibG9jayIsInVuYmxvY2tfYWxsX3Nsb3RzIiwic2xvdF9yZXBvcnQiLCJwYXJ0bmVyX3JlY2hhcmdlIiwib3JkZXJfYXBwcm92ZSIsImJ1bGtfcHVyY2hhc2UiLCJtb2JpbGl0eV9yZWZpbGwiLCJtYXBfYnBfdXNlciIsInZpZXdfZnJhbmNoaXNlZSIsImNyZWF0ZV9mcmFuY2hpc2VlIiwidmlld19iYW5rIiwiY3JlYXRlX2JhbmsiLCJ2aWV3X3BsYXRmb3JtX2NoYXJnZSIsIm1hcF9wbGF0Zm9ybV9jaGFyZ2UiLCJjcmVhdGVfcGxhdGZvcm1fY2hhcmdlIiwibW9iaWxpdHlfY2hlY2tpbiIsImNyZWF0ZV9jb3Vwb24iLCJ2aWV3X2NvdXBvbiIsImJ1eV9hdF92cCIsIm1hcF9tYWNoaW5lcyIsInZpZXdfYnBfdXNlciIsInZtX2NyZWF0ZSIsInZtX3VwZGF0ZSIsInZtX3ZpZXciLCJjb2hvcnRfY3JlYXRlIiwiY29ob3J0X3ZpZXciLCJjb2hvcnRfbWFwX3VzZXIiLCJjb2hvcnRfbWFwX21hY2hpbmUiLCJiYW5uZXJfY3JlYXRlIiwiYXBwcm92ZV9hdHRlbmRhbmNlIiwic2xvdF9vcGVyYXRpb24iLCJyZWZ1bmRfcmV2b2tlIiwic2xvdF9yZXBvcnRfaW52ZW50b3J5IiwicmVmaWxsX2xvc3Nfb3ZlcnJpZGUiXSwiaWF0IjoxNjg0MjIwOTY1LCJleHAiOjE2ODQzMDczNjV9.qVRQJ3VSEUd3drXSN5V91DGqaMIXimblkLjFL7FBON33peFNuhf6fKMGVMjiA7z9";
        aTask.execute(url,jsonInString,"",token);

    }




}
