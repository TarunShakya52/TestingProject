package com.example.demoapplication.Contract;

import com.example.demoapplication.response.KitInvoiceResponse;

import retrofit2.http.Url;

public interface InvoiceDetails_Contract {

    interface View{
        void onCancelClick();
        void startLoader();
        void hideLoader();
        void errorMsg(String msg);
        void onSuccess(KitInvoiceResponse kitInvoiceResponse);
    }

    interface Presenter{
        void onCancel();
        void onContinue(String url);


    }
    interface ResponseDataCallBack{
        void onStopLoader();
        void onError(String message);
        void onSuccess(KitInvoiceResponse loginResponse);
    }

}
