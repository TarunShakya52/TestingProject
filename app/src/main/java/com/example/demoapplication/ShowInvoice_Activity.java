package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.demoapplication.Contract.InvoiceDetails_Contract;
import com.example.demoapplication.DataLoader.InvoiceDetailsDataLoader;
import com.example.demoapplication.Presenter.InvoiceDetails_Presenter;
import com.example.demoapplication.api.PostRequest;
import com.example.demoapplication.response.KitInvoiceResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;

public class ShowInvoice_Activity extends AppCompatActivity  implements InvoiceDetails_Contract.View {

    private Button button;
    KitInvoiceResponse response;

    InvoiceDetails_Presenter invoiceDetails_presenter;
    InvoiceDetailsDataLoader invoiceDetailsDataLoader;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invoice);

        button = findViewById(R.id.button7);

        invoiceDetailsDataLoader = new InvoiceDetailsDataLoader(this);
        invoiceDetails_presenter = new InvoiceDetails_Presenter(this,invoiceDetailsDataLoader);

        String url = "https://api-stage.daalchini.co.in/partner/api/v2/warehouse/1/invoice/868";





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceDetails_presenter.onContinue(url);
            }
        });
    }

//    private void kitInvoiceApi() {
//
//        PostRequest aTask = new PostRequest(this);
//        aTask.setListener(new PostRequest.MyAsyncTaskListener() {
//            @Override
//            public void onPreExecuteConcluded() {
//                //Loader Will come here
//            }
//
//            @Override
//            public void onPostExecuteConcluded(String result) {
//
//                Log.d("ResultCheckinvoice", "=" + result);
//                try {
//                    if (result != null) {
//                        GsonBuilder gsonBuilder = new GsonBuilder();
//                        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
//                        Gson gson = gsonBuilder.create();
//                        response = gson.fromJson(result, KitInvoiceResponse.class);
//                        Intent i = new Intent(ShowInvoice_Activity.this,InvoiceActivity.class);
//                        i.putExtra("invoiceDetail",response.getData());
//                        startActivity(i);
//                    }
//                } catch (Exception e) {
//                    Log.e("checkerroe",e.getMessage());
//
//
//                }
//
//            }
//        });
//        String token ="eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzYTRkMGZlYy0xZTUyLTRiOWYtOTFmZS0yZWJiMTdmMDQ1YzUiLCJpZCI6MjExLCJtb2JpbGUiOiI4MDg4NDAzNDMyIiwicm9sZSI6IkFETUlOIiwicGVybWlzc2lvbnMiOlsidm1fYWxsb3dlZCIsInByb2R1Y3RfYWxsb3dlZCIsIm9yZGVyc19hbGxvd2VkIiwidXNlcl9hbGxvd2VkIiwidm1fc2V0dGluZ3MiLCJ2bV9zYWxlcyIsIm5vdGlmaWNhdGlvbl9lbmFibGVkIiwiYWJzb2x1dGVfc2FsZXMiLCJ2ZW5kb3JfYWxsb3dlZCIsIndhbGxldF9oaXN0b3J5Iiwidmlld19icmFuZHMiLCJjcmVhdGVfYnJhbmQiLCJ2aWV3X2Jhc2UiLCJ2aWV3X3ZhcmlhbnRzIiwibWFwX3ZlbmRvciIsInZpZXdfcHJvZHVjdHMiLCJjcmVhdGVfcHJvZHVjdHMiLCJjcmVhdGVfYmFzZSIsImNyZWF0ZV92YXJpYW50IiwiY3JlYXRlX3ZlbmRvciIsInZpZXdfdmVuZG9yIiwidmlld191c2VycyIsIm1hcF91c2VyIiwiYXR0ZW5kYW5jZSIsImtpdHRpbmdfcmVmaWxsaW5nIiwiYXVkaXRfbWFjaGluZSIsInN0b2NraW5nIiwic3BhcmVfcGFydHMiLCJlX2RhYWxjaGluaSIsInNsb3RfdXRpbGl6YXRpb24iLCJoZWFsdGhfYWxlcnQiLCJ0cm91Ymxlc2hvb3RfZ3VpZGUiLCJyYWlzZV90aWNrZXQiLCJzZWxmX2Fzc2Vzc21lbnQiLCJyZWNjZSIsInZtX2luc3RhbGxhdGlvbiIsInZpZXdfdGFnIiwiYWN0aXZhdGVfdGFnIiwibWVhbHNfbWVudSIsInZtX3BhcmFtZXRlcnMiLCJzdXBwb3J0X25vdGlmaWNhdGlvbiIsInNsb3RfcmVwYWlyIiwicXVpY2tfdW5ibG9jayIsInVuYmxvY2tfYWxsX3Nsb3RzIiwic2xvdF9yZXBvcnQiLCJwYXJ0bmVyX3JlY2hhcmdlIiwib3JkZXJfYXBwcm92ZSIsImJ1bGtfcHVyY2hhc2UiLCJtb2JpbGl0eV9yZWZpbGwiLCJtYXBfYnBfdXNlciIsInZpZXdfZnJhbmNoaXNlZSIsImNyZWF0ZV9mcmFuY2hpc2VlIiwidmlld19iYW5rIiwiY3JlYXRlX2JhbmsiLCJ2aWV3X3BsYXRmb3JtX2NoYXJnZSIsIm1hcF9wbGF0Zm9ybV9jaGFyZ2UiLCJjcmVhdGVfcGxhdGZvcm1fY2hhcmdlIiwibW9iaWxpdHlfY2hlY2tpbiIsImNyZWF0ZV9jb3Vwb24iLCJ2aWV3X2NvdXBvbiIsImJ1eV9hdF92cCIsIm1hcF9tYWNoaW5lcyIsInZpZXdfYnBfdXNlciIsInZtX2NyZWF0ZSIsInZtX3VwZGF0ZSIsInZtX3ZpZXciLCJjb2hvcnRfY3JlYXRlIiwiY29ob3J0X3ZpZXciLCJjb2hvcnRfbWFwX3VzZXIiLCJjb2hvcnRfbWFwX21hY2hpbmUiLCJiYW5uZXJfY3JlYXRlIiwiYXBwcm92ZV9hdHRlbmRhbmNlIiwic2xvdF9vcGVyYXRpb24iLCJyZWZ1bmRfcmV2Il0sImlhdCI6MTY4NDEyNjQ1NiwiZXhwIjoxNjg0MjEyODU2fQ.b1jwjyuzmIXxheG7I9dq9cyUO3icZez_1BlO_47j1F6lDkVRb1aKPokDHIO0Llx_";
//        String url = "https://api-stage.daalchini.co.in/partner/api/v2/warehouse/1/invoice/868";
//        aTask.execute(url,"",token);
//
//    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void startLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void errorMsg(String msg) {
        Toast.makeText(ShowInvoice_Activity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(KitInvoiceResponse kitInvoiceResponse) {
        Intent i = new Intent(ShowInvoice_Activity.this,InvoiceActivity.class);
        i.putExtra("invoiceDetail",kitInvoiceResponse.getData());
        startActivity(i);
    }
}