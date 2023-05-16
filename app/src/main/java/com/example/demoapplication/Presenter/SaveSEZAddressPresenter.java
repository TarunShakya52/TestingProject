package com.example.demoapplication.Presenter;

import com.example.demoapplication.Contract.InvoiceDetails_Contract;
import com.example.demoapplication.Contract.SaveSEZAddress_Contract;
import com.example.demoapplication.DataLoader.AddSEZDataLoader;
import com.example.demoapplication.DataLoader.InvoiceDetailsDataLoader;
import com.example.demoapplication.request.AddSEZAdress_Request;
import com.example.demoapplication.response.AddSezAddressResponse;

public class SaveSEZAddressPresenter implements SaveSEZAddress_Contract.Presenter,SaveSEZAddress_Contract.ResponseDataCallBack {

    private SaveSEZAddress_Contract.View view;
    private AddSEZDataLoader saveSEZAddressDataLoader;

    public SaveSEZAddressPresenter(SaveSEZAddress_Contract.View view, AddSEZDataLoader saveSEZAddressDataLoader) {
        this.view = view;
        this.saveSEZAddressDataLoader = saveSEZAddressDataLoader;
    }


    @Override
    public void onCancel() {

    }

    @Override
    public void onContinue(String url, AddSEZAdress_Request address) {
        saveSEZAddressDataLoader.addSEZaddress(this,url,address);

    }

    @Override
    public void onStopLoader() {

    }

    @Override
    public void onError(String message) {
        view.errorMsg(message);
    }

    @Override
    public void onSuccess(AddSezAddressResponse addSezAddressResponse) {
        view.onSuccess(addSezAddressResponse);
    }
}
