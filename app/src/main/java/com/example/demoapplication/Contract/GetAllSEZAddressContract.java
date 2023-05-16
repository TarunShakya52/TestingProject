package com.example.demoapplication.Contract;

import com.example.demoapplication.response.SezAddressGetResponse;

public interface GetAllSEZAddressContract {
    interface View{
        void onCancelClick();
        void startLoader();
        void hideLoader();
        void errorMsg(String msg);
        void onSuccess(SezAddressGetResponse sezAddressGetResponse);
    }

    interface Presenter{
        void onCancel();
        void onContinue(String url);


    }
    interface ResponseDataCallBack{
        void onStopLoader();
        void onError(String message);
        void onSuccess(SezAddressGetResponse sezAddressGetResponse);
    }
}
