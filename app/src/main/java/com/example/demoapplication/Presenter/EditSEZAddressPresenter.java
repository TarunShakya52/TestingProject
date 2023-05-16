package com.example.demoapplication.Presenter;

import com.example.demoapplication.Contract.SaveSEZAddress_Contract;
import com.example.demoapplication.DataLoader.AddSEZDataLoader;
import com.example.demoapplication.DataLoader.EditSEZAddressDataLoader;
import com.example.demoapplication.request.AddSEZAdress_Request;
import com.example.demoapplication.response.AddSezAddressResponse;

public class EditSEZAddressPresenter implements SaveSEZAddress_Contract.Presenter,SaveSEZAddress_Contract.ResponseDataCallBack {

    private SaveSEZAddress_Contract.View view;
    private EditSEZAddressDataLoader editSEZAddressDataLoader;

    public EditSEZAddressPresenter(SaveSEZAddress_Contract.View view, EditSEZAddressDataLoader editSEZAddressDataLoader) {
        this.view = view;
        this.editSEZAddressDataLoader = editSEZAddressDataLoader;
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onContinue(String url, AddSEZAdress_Request address) {
        editSEZAddressDataLoader.editSEZAddress(this,url,address);
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
