package com.example.demoapplication.Contract;

import com.example.demoapplication.request.AddSEZAdress_Request;
import com.example.demoapplication.response.AddSezAddressResponse;
import com.example.demoapplication.response.KitInvoiceResponse;

public interface SaveSEZAddress_Contract {

    interface View{
        void onCancelClick();
        void startLoader();
        void hideLoader();
        void errorMsg(String msg);
        void onSuccess(AddSezAddressResponse addSezAddressResponse);
    }

    interface Presenter{
        void onCancel();
        void onContinue(String url, AddSEZAdress_Request address);


    }
    interface ResponseDataCallBack{
        void onStopLoader();
        void onError(String message);
        void onSuccess(AddSezAddressResponse addSezAddressResponse);
    }
}
