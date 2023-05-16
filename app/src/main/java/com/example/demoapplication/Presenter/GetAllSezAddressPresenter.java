package com.example.demoapplication.Presenter;

import com.example.demoapplication.Contract.GetAllSEZAddressContract;
import com.example.demoapplication.Contract.InvoiceDetails_Contract;
import com.example.demoapplication.DataLoader.GetAllSEZAddressDataLoader;
import com.example.demoapplication.DataLoader.InvoiceDetailsDataLoader;
import com.example.demoapplication.response.SezAddressGetResponse;

public class GetAllSezAddressPresenter implements GetAllSEZAddressContract.Presenter,GetAllSEZAddressContract.ResponseDataCallBack {

    private GetAllSEZAddressContract.View view;
    private GetAllSEZAddressDataLoader getAllSEZAddressDataLoader;

    public GetAllSezAddressPresenter(GetAllSEZAddressContract.View view, GetAllSEZAddressDataLoader getAllSEZAddressDataLoader) {
        this.view = view;
        this.getAllSEZAddressDataLoader = getAllSEZAddressDataLoader;
    }


    @Override
    public void onCancel() {

    }

    @Override
    public void onContinue(String url) {
        getAllSEZAddressDataLoader.getAddress(this,url);

    }

    @Override
    public void onStopLoader() {

    }

    @Override
    public void onError(String message) {
        view.errorMsg(message);
    }

    @Override
    public void onSuccess(SezAddressGetResponse sezAddressGetResponse) {
        view.onSuccess(sezAddressGetResponse);
    }
}
