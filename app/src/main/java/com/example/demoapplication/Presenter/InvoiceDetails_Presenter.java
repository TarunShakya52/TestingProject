package com.example.demoapplication.Presenter;

import com.example.demoapplication.Contract.InvoiceDetails_Contract;
import com.example.demoapplication.DataLoader.InvoiceDetailsDataLoader;
import com.example.demoapplication.response.KitInvoiceResponse;

public class InvoiceDetails_Presenter implements InvoiceDetails_Contract.Presenter,InvoiceDetails_Contract.ResponseDataCallBack{

    private InvoiceDetails_Contract.View view;
    private InvoiceDetailsDataLoader invoiceDetailsDataLoader;

    public InvoiceDetails_Presenter(InvoiceDetails_Contract.View view, InvoiceDetailsDataLoader invoiceDetailsDataLoader) {
        this.view = view;
        this.invoiceDetailsDataLoader = invoiceDetailsDataLoader;
    }



    @Override
    public void onCancel() {

    }

    @Override
    public void onContinue(String url) {

        invoiceDetailsDataLoader.invoiceDetails(this,url);
    }

    @Override
    public void onStopLoader() {

    }

    @Override
    public void onError(String message) {
        view.errorMsg(message);
    }

    @Override
    public void onSuccess(KitInvoiceResponse loginResponse) {
        view.onSuccess(loginResponse);
    }
}
