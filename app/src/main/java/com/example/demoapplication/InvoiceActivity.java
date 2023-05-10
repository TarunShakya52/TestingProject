package com.example.demoapplication;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapplication.api.GetRequest;
import com.example.demoapplication.api.PostRequest;
import com.example.demoapplication.request.AddSEZAdress_Request;
import com.example.demoapplication.response.DataInvoiceKit;
import com.example.demoapplication.response.KitInvoiceResponse;
import com.example.demoapplication.response.SezAddressGetResponse;
import com.example.tscdll.TSCUSBActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.impl.Utils;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
public class InvoiceActivity extends AppCompatActivity {

    private static final int DIVIDER_SIZE = 2;
    LinearLayout layout;
    com.itextpdf.layout.Document document;
    String pdfPath;
    File file;
    Uri uri;
    Boolean checkForHeaders = true;
    Boolean checkForHeaders2 = true;
    private static UsbDevice device;
    private static UsbManager mUsbManager;
    private static PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static boolean hasPermissionToCommunicate = false;
    private CardView cvShipping,cvBilling;
    MaterialButton btnPrintPDF;
    Bitmap bitmap;
    TSCUSBActivity tscusbActivity;
    Boolean shipping = false;
    Boolean billing = false;
    private static final int TSC_VENDOR_ID = 0x0BB4;
    private static final int TSC_PRODUCT_ID = 0x0303;
    String stitle,spos,sgst,sstatecode,saddresss,btitle,bpos,bgst,bstatecode,baddresss;
    private KitInvoiceResponse response;
    private String arrShipping[];
    private String arrBilling[];
    SharedPreferences prefs;
    private AddSEZAdress_Request shippingDetails;
    private AddSEZAdress_Request billingDetails;
    String jsonStringShipping,jsonStringBilling;
    private SezAddressGetResponse sezAddressGetResponse;
    Gson gson = new Gson();
    int table2Count,taxTableCount = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
//        layout = findViewById(R.id.layoutLinearView);

        prefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();
        editor.putString("gsonBillingAddress", null);
        editor.putString("gsonShippingAddress",null);
        editor.apply();



        getSezAddress();

        tscusbActivity = new TSCUSBActivity();


        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        cvBilling = findViewById(R.id.cv_billing);
        cvShipping = findViewById(R.id.cv_shipping);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPermissionIntent = PendingIntent.getBroadcast(this, 0,  new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
        } else {
            mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter);


        UsbAccessory[] accessoryList = mUsbManager.getAccessoryList();
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Log.d("Detect ", deviceList.size()+" USB device(s) found");
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext())
        {
            device = deviceIterator.next();
            if(device.getVendorId() == 4611)
            {
                Log.e("errocgsd",device.getDeviceName());
                break;
            }
        }

        btnPrintPDF = findViewById(R.id.btnPrintPDF);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPermissionIntent = PendingIntent.getBroadcast(this, 0,  new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
        } else {
            mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if (device != null) {
            mUsbManager.requestPermission(device, mPermissionIntent);
        }

        btnPrintPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (device != null){
                    if (mUsbManager.hasPermission(device))
                    {
                        tscusbActivity.openport(mUsbManager, device);

                        tscusbActivity.setup(70, 50, 4, 4, 0, 0,0);

                        tscusbActivity.sendcommand("PDF-IMAGE\n");

//                    tscusbActivity.sendcommand("CLS\r\n");
//                    tscusbActivity.sendcommand("PRINT " + "number" + "\r\n");

                        pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                        file = new File(pdfPath,"myPDF_PDF.pdf");
                        Log.e("filePath",file.getPath());

//                    tscusbActivity.printPDFbyFile(file,30,20,90);

                        try (InputStream is = new FileInputStream(file)) {
                            tscusbActivity.printPDFbyFile(file,0,0,70);
                            tscusbActivity.sendcommand("PRINT\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        tscusbActivity.closeport();
                    }else {
                        Toast.makeText(InvoiceActivity.this, "Permission not given", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(InvoiceActivity.this,"Device not connected",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void kitInvoiceApi() {

        PostRequest aTask = new PostRequest(this);
        aTask.setListener(new PostRequest.MyAsyncTaskListener() {
            @Override
            public void onPreExecuteConcluded() {
                //Loader Will come here
            }

            @Override
            public void onPostExecuteConcluded(String result) {

                Log.d("ResultCheck", "=" + result);
                try {
                    if (result != null) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                        Gson gson = gsonBuilder.create();
                        response = gson.fromJson(result, KitInvoiceResponse.class);
                        try{
//                            checkIntent();
                            createPDF(response.getData(), shippingDetails, billingDetails);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }

            }
        });
        String token = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJiNTA4OWExMi1mYjFhLTQzYWItODljNS0xZGRhOTlmMjU4NDciLCJpZCI6MjExLCJtb2JpbGUiOiI4MDg4NDAzNDMyIiwicm9sZSI6IkFETUlOIiwicGVybWlzc2lvbnMiOlsidm1fYWxsb3dlZCIsInByb2R1Y3RfYWxsb3dlZCIsIm9yZGVyc19hbGxvd2VkIiwidXNlcl9hbGxvd2VkIiwidm1fc2V0dGluZ3MiLCJ2bV9zYWxlcyIsIm5vdGlmaWNhdGlvbl9lbmFibGVkIiwiYWJzb2x1dGVfc2FsZXMiLCJ2ZW5kb3JfYWxsb3dlZCIsIndhbGxldF9oaXN0b3J5Iiwidmlld19icmFuZHMiLCJjcmVhdGVfYnJhbmQiLCJ2aWV3X2Jhc2UiLCJ2aWV3X3ZhcmlhbnRzIiwibWFwX3ZlbmRvciIsInZpZXdfcHJvZHVjdHMiLCJjcmVhdGVfcHJvZHVjdHMiLCJjcmVhdGVfYmFzZSIsImNyZWF0ZV92YXJpYW50IiwiY3JlYXRlX3ZlbmRvciIsInZpZXdfdmVuZG9yIiwidmlld191c2VycyIsIm1hcF91c2VyIiwiYXR0ZW5kYW5jZSIsImtpdHRpbmdfcmVmaWxsaW5nIiwiYXVkaXRfbWFjaGluZSIsInN0b2NraW5nIiwic3BhcmVfcGFydHMiLCJlX2RhYWxjaGluaSIsInNsb3RfdXRpbGl6YXRpb24iLCJoZWFsdGhfYWxlcnQiLCJ0cm91Ymxlc2hvb3RfZ3VpZGUiLCJyYWlzZV90aWNrZXQiLCJzZWxmX2Fzc2Vzc21lbnQiLCJyZWNjZSIsInZtX2luc3RhbGxhdGlvbiIsInZpZXdfdGFnIiwiYWN0aXZhdGVfdGFnIiwibWVhbHNfbWVudSIsInZtX3BhcmFtZXRlcnMiLCJzdXBwb3J0X25vdGlmaWNhdGlvbiIsInNsb3RfcmVwYWlyIiwicXVpY2tfdW5ibG9jayIsInVuYmxvY2tfYWxsX3Nsb3RzIiwic2xvdF9yZXBvcnQiLCJwYXJ0bmVyX3JlY2hhcmdlIiwib3JkZXJfYXBwcm92ZSIsImJ1bGtfcHVyY2hhc2UiLCJtb2JpbGl0eV9yZWZpbGwiLCJtYXBfYnBfdXNlciIsInZpZXdfZnJhbmNoaXNlZSIsImNyZWF0ZV9mcmFuY2hpc2VlIiwidmlld19iYW5rIiwiY3JlYXRlX2JhbmsiLCJ2aWV3X3BsYXRmb3JtX2NoYXJnZSIsIm1hcF9wbGF0Zm9ybV9jaGFyZ2UiLCJjcmVhdGVfcGxhdGZvcm1fY2hhcmdlIiwibW9iaWxpdHlfY2hlY2tpbiIsImNyZWF0ZV9jb3Vwb24iLCJ2aWV3X2NvdXBvbiIsImJ1eV9hdF92cCIsIm1hcF9tYWNoaW5lcyIsInZpZXdfYnBfdXNlciIsInZtX2NyZWF0ZSIsInZtX3VwZGF0ZSIsInZtX3ZpZXciLCJjb2hvcnRfY3JlYXRlIiwiY29ob3J0X3ZpZXciLCJjb2hvcnRfbWFwX3VzZXIiLCJjb2hvcnRfbWFwX21hY2hpbmUiLCJiYW5uZXJfY3JlYXRlIiwiYXBwcm92ZV9hdHRlbmRhbmNlIiwic2xvdF9vcGVyYXRpb24iLCJyZWZ1bmRfcmV2Il0sImlhdCI6MTY4MzYzMzIxMywiZXhwIjoxNjgzNzE5NjEzfQ.T99JHUBtENBuDLFMlu7wqJHy1vzm_aT6d11Z3K6FwTRYntnAJrMipuTyb7ra7nQD";
        String url = "https://api-stage.daalchini.co.in/partner/api/v2/warehouse/1/invoice/814";
        aTask.execute(url,"",token);
    }

//    private Bitmap LoadBitmap(View v, int width, int height){
//        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        v.draw(canvas);
//        return bitmap;
//    }

//    private void createPDFforPrint(){
//
//        //Fourth Arttempt
//
//        PrintManager printManager=(PrintManager) this.getSystemService(Context.PRINT_SERVICE);
//        try
//        {
//            PrintDocumentAdapter printAdapter = new PdfPrintDocumentAdapter(InvoiceActivity.this,file.toString());
//            printManager.print("Document", printAdapter,new PrintAttributes.Builder()
//                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
//                    .build());
//        } catch (Exception e) {
//       Log.e("errorPrint",e.getMessage());}
//    }



    private void createPDF(DataInvoiceKit response, AddSEZAdress_Request shippingDetails, AddSEZAdress_Request billingDetails) throws FileNotFoundException {
        pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        file = new File(pdfPath,"myPDF_PDF.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        document = new  com.itextpdf.layout.Document(pdfDocument,PageSize.A4);
        uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", file);
        float[] columnWidthHeading = {820};
        Table table1 = new Table(columnWidthHeading);
        Table tableShippingAddress = new Table(columnWidthHeading);
        Table tableDeliveryAddress = new Table(columnWidthHeading);


        tableShippingAddress.setMarginTop(40.0f);
        tableDeliveryAddress.setMarginTop(40.0f);
        tableDeliveryAddress.setMarginBottom(140.0f);
        addColumnHeaders(table1);

//        jsonStringShipping = prefs.getString("gsonShippingAddress", null);
//        jsonStringBilling =  prefs.getString("gsonBillingAddress", null);
        if (jsonStringShipping != null) {
//            shippingDetails = gson.fromJson(jsonStringShipping, AddSEZAdress_Request.class);
//            Log.e("shippingDetails", shippingDetails.getTitle() + " " + shippingDetails.getAddress() + " " + shippingDetails.getSupply_place() + " " + shippingDetails.getState_code() + " " + shippingDetails.getGstin());
            addColumnHeadersShipping(tableShippingAddress);
        }
        if (jsonStringBilling != null){
//            billingDetails = gson.fromJson(jsonStringBilling, AddSEZAdress_Request.class);
//            Log.e("billingInfo",billingDetails.getTitle()+" "+billingDetails.getAddress()+" "+billingDetails.getSupply_place()+" "+billingDetails.getState_code()+" "+billingDetails.getGstin());
            addColumnHeadersDelivery(tableDeliveryAddress);
        }
//
//        for (int i=0 ; i<3 ; i++){
//            table1.addCell(new Cell().add(new Paragraph("1")));
//            table1.addCell(new Cell().add(new Paragraph("Namkeen")));
//            table1.addCell(new Cell().add(new Paragraph("2342354")));
//            table1.addCell(new Cell().add(new Paragraph("2")));
//            table1.addCell(new Cell().add(new Paragraph("20")));
//            table1.addCell(new Cell().add(new Paragraph("10")));
//            table1.addCell(new Cell().add(new Paragraph("34t54y6uu")));
//            table1.addCell(new Cell().add(new Paragraph("")));
//
//        }
        document.add(table1);
        document.add(tableShippingAddress);
        document.add(tableDeliveryAddress);

        float[] columnWidthHeading2 = {100f, 300f, 100f, 100f, 100f, 120f, 100f};
        Table table2 = new Table(columnWidthHeading2);
//        float[] columnWidthHeading3 = {80f, 300f, 100f, 100f, 100f, 120f, 100f};
//        Table table2Names = new Table(columnWidthHeading3);
//        addColumnHeaders2(table2Names);
        int count = 0;
        for (int i=0 ; i<response.getItemDetails().getItems().size() ; i++){

            while (count < 1) {
                table2.addCell(new Cell().add(new Paragraph("S.No").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("Description of Goods").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("HSN").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("QTY").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("MRP").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("Unit Price").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("GST").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));

                table2.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("Taxable Value").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("IGST").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("CGST").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("SGST").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("CESS").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
                count++;
            }

            table2.addCell(new Cell().add(new Paragraph(String.valueOf(table2Count++)).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(response.getItemDetails().getItems().get(i).getDescription()).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(response.getItemDetails().getItems().get(i).getHsnCode()).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getQuantity())).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getMrp())).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getUnitPrice())).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getIgstValue())).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));

            table2.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getTaxableValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getIgstValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getCgstValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getSgstValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getCessValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getItems().get(i).getItemValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));


            if (i == response.getItemDetails().getItems().size() -1){
                table2.addCell(new Cell().add(new Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("").setFontSize(10f)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table2.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getTotals().getQuantity())).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                table2.addCell(new Cell().add(new Paragraph("").setFontSize(10f)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table2.addCell(new Cell().add(new Paragraph("").setFontSize(10f)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table2.addCell(new Cell().add(new Paragraph("").setFontSize(10f)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

                table2.addCell(new Cell().add(new Paragraph("").setFontSize(10f)));
                table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getTotals().getTaxableValue())).setTextAlignment(TextAlignment.CENTER).setFontSize(10f)));
                table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getTotals().getIgstValue())).setTextAlignment(TextAlignment.CENTER).setFontSize(10f)));
                table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getTotals().getCgstValue())).setTextAlignment(TextAlignment.CENTER).setFontSize(10f)));
                table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getTotals().getSgstValue())).setTextAlignment(TextAlignment.CENTER).setFontSize(10f)));
                table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getTotals().getCessValue())).setTextAlignment(TextAlignment.CENTER).setFontSize(10f)));
                table2.addCell(new Cell().add(new Paragraph(String.valueOf(response.getItemDetails().getTotals().getItemValue())).setTextAlignment(TextAlignment.CENTER).setFontSize(10f)));
            }
        }

//        document.add(table2Names);
        document.add(table2);

        float[] columnWithforTax = {205,205,205,205};
        Table taxTable = new Table(columnWithforTax);

        taxTable.setMarginTop(40.0f);
//        addColumnHeadersTax(taxTable);
        for (int j = 0 ; j<response.getTaxDetails().getTaxes().size(); j++){
//            if (checkForHeaders){
            for (int i =0; i<=0;i++){
                taxTable.addCell(new Cell().add(new Paragraph("ID").setTextAlignment(TextAlignment.CENTER)));
                taxTable.addCell(new Cell().add(new Paragraph("I GST").setTextAlignment(TextAlignment.CENTER)));
                taxTable.addCell(new Cell().add(new Paragraph("Cg St").setTextAlignment(TextAlignment.CENTER)));
                taxTable.addCell(new Cell().add(new Paragraph("Sg St").setTextAlignment(TextAlignment.CENTER)));
            }
//                checkForHeaders = false;
//            }
            taxTable.addCell(new Cell().add(new Paragraph(String.valueOf(taxTableCount++)).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            taxTable.addCell(new Cell().add(new Paragraph(String.valueOf(response.getTaxDetails().getTaxes().get(j).getIgstValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            taxTable.addCell(new Cell().add(new Paragraph(String.valueOf(response.getTaxDetails().getTaxes().get(j).getCgstValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            taxTable.addCell(new Cell().add(new Paragraph(String.valueOf(response.getTaxDetails().getTaxes().get(j).getSgstValue())).setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));

            if (j == response.getTaxDetails().getTaxes().size() -1){
                taxTable.addCell(new Cell().add(new Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                taxTable.addCell(new Cell().add(new Paragraph(String.valueOf(response.getTaxDetails().getTotals().getIgstValue())).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                taxTable.addCell(new Cell().add(new Paragraph(String.valueOf(response.getTaxDetails().getTotals().getCgstValue())).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
                taxTable.addCell(new Cell().add(new Paragraph(String.valueOf(response.getTaxDetails().getTotals().getSgstValue())).setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));

            }
        }
        document.add(taxTable);

        document.close();
        showdoc();
    }

    private void addColumnHeadersTax(Table taxTable) {

    }

    private void addColumnHeadersDelivery(Table tableDeliveryAddress) {
        Cell cell = new Cell();
        Cell shippingCell = new Cell();
        tableDeliveryAddress.addCell(shippingCell.setPadding(10f).setBackgroundColor(new DeviceRgb(107, 76, 82)));
        tableDeliveryAddress.addCell(cell.setPadding(10f));
        Paragraph p1 = new Paragraph("Invoice No : "+response.getData().getInvoiceNumber());
        Paragraph p2 = new Paragraph("Date: "+response.getData().getDate());
        Paragraph p3 = new Paragraph(billingDetails.getTitle()).setBold().setFontSize(15).setMarginTop(10);
        Paragraph p4 = new Paragraph(billingDetails.getAddress());
        Paragraph p5 = new Paragraph(billingDetails.getSupply_place()+" - "+billingDetails.getState_code());
        Paragraph p6 = new Paragraph("GSTIN - "+billingDetails.getGstin());

        shippingCell.add(new Paragraph("Delivery Address").setBold().setFontSize(17).setPadding(5f).setFontColor(new DeviceRgb(255,255,255)));
        cell.add(p1);
        cell.add(p2);
        cell.add(p3);
        cell.add(p4);
        cell.add(p5);
        cell.add(p6);
    }

    private void addColumnHeadersShipping(Table tableShippingAddress) {
        Cell cell = new Cell();
        Cell shippingCell = new Cell();
        tableShippingAddress.addCell(shippingCell.setPadding(10f).setBackgroundColor(new DeviceRgb(107, 76, 82)));
        tableShippingAddress.addCell(cell.setPadding(10f));
        Paragraph p1 = new Paragraph("Invoice No : "+response.getData().getInvoiceNumber());
        Paragraph p2 = new Paragraph("Date: "+response.getData().getDate());
        Paragraph p3 = new Paragraph(shippingDetails.getTitle()).setBold().setFontSize(15).setMarginTop(10);
        Paragraph p4 = new Paragraph(shippingDetails.getAddress());
        Paragraph p5 = new Paragraph(shippingDetails.getSupply_place()+ "-" +shippingDetails.getState_code());
        Paragraph p6 = new Paragraph("GSTIN - "+shippingDetails.getGstin());


//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.buss);
//
//// Create an Image object from the bitmap
//// Convert the Bitmap object to a byte[] array
//        byte[] imageData = getBytesFromBitmap(bitmap);
//
//// Create an Image object from the byte[] array
//        Image image = new Image(ImageDataFactory.create(imageData));
//        image.setWidth(20);
//        image.setHeight(15);

        shippingCell.setVerticalAlignment(VerticalAlignment.BOTTOM).add(new Paragraph("Shipping Address").setBold().setFontSize(17).setPadding(5.0f).setFontColor(new DeviceRgb(255,255,255)));
        cell.add(p1);
        cell.add(p2);
        cell.add(p3);
        cell.add(p4);
        cell.add(p5);
        cell.add(p6);
    }

    private void addColumnHeaders2(Table table2Names) {
//        if (checkForHeaders2)
//        {
            table2Names.addCell(new Cell().add(new Paragraph("S.No").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("Description of Goods").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("HSN").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("QTY").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("MRP").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("Unit Price").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("GST").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));

            table2Names.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("Taxable Value").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("IGST").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("CGST").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("SGST").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("CESS").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2Names.addCell(new Cell().add(new Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
//            checkForHeaders2 = false;
//        }
    }

    private void addColumnHeaders(Table table1) {
//        if (checkForHeaders)
//        {

            Cell cell = new Cell();
            table1.addCell(cell.setPadding(10f));
            Paragraph p1 = new Paragraph("Invoice No : "+response.getData().getInvoiceNumber());
            Paragraph p2 = new Paragraph("Date: "+response.getData().getDate());
            Paragraph p3 = new Paragraph(sezAddressGetResponse.getData().getWarehouseLocation().getTitle()).setBold().setFontSize(15).setMarginTop(10);
            Paragraph p4 = new Paragraph(sezAddressGetResponse.getData().getWarehouseLocation().getAddress());
            Paragraph p5 = new Paragraph("GSTIN - "+sezAddressGetResponse.getData().getWarehouseLocation().getGstin());


            cell.add(p1);
            cell.add(p2);
            cell.add(p3);
            cell.add(p4);
            cell.add(p5);
//        table1.addCell(new Cell().add(new Paragraph("Description of Goods")));
//        table1.addCell(new Cell().add(new Paragraph("HSN")));
//        table1.addCell(new Cell().add(new Paragraph("QTY")));
//        table1.addCell(new Cell().add(new Paragraph("MRP")));
//        table1.addCell(new Cell().add(new Paragraph("Unit Price")));
//        table1.addCell(new Cell().add(new Paragraph("GST")));
//        table1.addCell(new Cell().add(new Paragraph("")));
//        checkForHeaders = false;
//        }
    }

    private void showdoc() {
        PDFView pdfView = findViewById(R.id.pdf_view);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            pdfView.fromUri(uri).load();
        } else {
            // Handle file not found
        }
    }

//    private void printPDF() {
//        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
//        PrintDocumentAdapter printAdapter = new PdfPrintAdapter(file);
//
//// Find the TSC printer
//        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
//        PrinterDiscoverySession session = printManager.createSession();
//        PrinterDiscoverySession discoverySession = printManager.createSession();
//        discoverySession.setOnPrinterDiscoveryListener(new PrinterDiscoverySession.OnPrinterDiscoveryListener() {
//            @Override
//            public void onPrintersAdded(List<PrinterInfo> printers) {
//                for (PrinterInfo printer : printers) {
//                    if (printer.getName().equals("TSC Printer")) {
//                        // Create a print job with the TSC printer and PDF file
//                        PrintJob printJob = printer.createPrintJob("Job Name", printAdapter, new PrintAttributes.Builder().build());
//                        // Send the print job to the TSC printer
//                        printJob.print();
//                        break;
//                    }
//                }
//            }
//        });
//
//        discoverySession.start();
//    }


    @Override
    protected void onResume() {
        super.onResume();

        jsonStringShipping = prefs.getString("gsonShippingAddress", null);
        jsonStringBilling =  prefs.getString("gsonBillingAddress", null);
        if (jsonStringShipping != null) {
            shippingDetails = gson.fromJson(jsonStringShipping, AddSEZAdress_Request.class);
            Log.e("shippingDetailssss", shippingDetails.getTitle() + " " + shippingDetails.getAddress() + " " + shippingDetails.getSupply_place() + " " + shippingDetails.getState_code() + " " + shippingDetails.getGstin());
        }

        if (jsonStringBilling != null) {
            billingDetails = gson.fromJson(jsonStringBilling, AddSEZAdress_Request.class);
            Log.e("billingDetailssss", billingDetails.getTitle() + " " + billingDetails.getAddress() + " " + billingDetails.getSupply_place() + " " + billingDetails.getState_code() + " " + billingDetails.getGstin());
        }
        kitInvoiceApi();

        cvShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvoiceActivity.this,SEZ_ADDRESS_ACTIVITY.class);
                intent.putExtra("shipping",true);
                startActivity(intent);
            }
        });

        cvBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvoiceActivity.this,SEZ_ADDRESS_ACTIVITY.class);
                intent.putExtra("billing",true);
                startActivity(intent);
            }
        });
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            hasPermissionToCommunicate = true;
                        }
                    }
                }
            }
        }
    };

    public void checkIntent(){
        Intent intent = getIntent();
        shipping = intent.getBooleanExtra("shipping",false);
        billing = intent.getBooleanExtra("billing",false);
        Log.e("shipping+billing",shipping +" "+billing);
        if (shipping && !billing){
            try {
                prefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
                String jsonString = prefs.getString("gsonShippingAddress", null);
                Log.e("jst", jsonString);
                if (jsonString != null) {
                    Gson gson = new Gson();
                    shippingDetails = gson.fromJson(jsonString, AddSEZAdress_Request.class);
                    Log.e("shippingDetails", shippingDetails.getTitle() + " " + shippingDetails.getAddress() + " " + shippingDetails.getSupply_place() + " " + shippingDetails.getState_code() + " " + shippingDetails.getGstin());
                }
            }catch (Exception e){
                Log.e("jsterror",e.getMessage());

            }
        }
        else if(!shipping && billing){
            prefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
            String jsonString = prefs.getString("gsonBillingAddress", null);
            if (jsonString != null) {
                Gson gson = new Gson();
                billingDetails = gson.fromJson(jsonString, AddSEZAdress_Request.class);
                Log.e("billingInfo",billingDetails.getTitle()+" "+billingDetails.getAddress()+" "+billingDetails.getSupply_place()+" "+billingDetails.getState_code()+" "+billingDetails.getGstin());

            }
        }else {
            Toast.makeText(InvoiceActivity.this,"Please Select Shipping and Billing Address",Toast.LENGTH_SHORT).show();
        }
    }

    private void getSezAddress() {

        GetRequest aTask = new GetRequest(this);
        aTask.setListener(new GetRequest.MyAsyncTaskkGetListener() {
            @Override
            public void onPreExecuteConcluded() {
                //Loader Will come here
            }

            @Override
            public void onPostExecuteConcluded(String result) {

                Log.d("ResultCheck", "=" + result);
                try {
                    if (result != null) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                        Gson gson = gsonBuilder.create();
                        sezAddressGetResponse = gson.fromJson(result, SezAddressGetResponse.class);
                        kitInvoiceApi();
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }

            }
        });

        String token = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJiNTA4OWExMi1mYjFhLTQzYWItODljNS0xZGRhOTlmMjU4NDciLCJpZCI6MjExLCJtb2JpbGUiOiI4MDg4NDAzNDMyIiwicm9sZSI6IkFETUlOIiwicGVybWlzc2lvbnMiOlsidm1fYWxsb3dlZCIsInByb2R1Y3RfYWxsb3dlZCIsIm9yZGVyc19hbGxvd2VkIiwidXNlcl9hbGxvd2VkIiwidm1fc2V0dGluZ3MiLCJ2bV9zYWxlcyIsIm5vdGlmaWNhdGlvbl9lbmFibGVkIiwiYWJzb2x1dGVfc2FsZXMiLCJ2ZW5kb3JfYWxsb3dlZCIsIndhbGxldF9oaXN0b3J5Iiwidmlld19icmFuZHMiLCJjcmVhdGVfYnJhbmQiLCJ2aWV3X2Jhc2UiLCJ2aWV3X3ZhcmlhbnRzIiwibWFwX3ZlbmRvciIsInZpZXdfcHJvZHVjdHMiLCJjcmVhdGVfcHJvZHVjdHMiLCJjcmVhdGVfYmFzZSIsImNyZWF0ZV92YXJpYW50IiwiY3JlYXRlX3ZlbmRvciIsInZpZXdfdmVuZG9yIiwidmlld191c2VycyIsIm1hcF91c2VyIiwiYXR0ZW5kYW5jZSIsImtpdHRpbmdfcmVmaWxsaW5nIiwiYXVkaXRfbWFjaGluZSIsInN0b2NraW5nIiwic3BhcmVfcGFydHMiLCJlX2RhYWxjaGluaSIsInNsb3RfdXRpbGl6YXRpb24iLCJoZWFsdGhfYWxlcnQiLCJ0cm91Ymxlc2hvb3RfZ3VpZGUiLCJyYWlzZV90aWNrZXQiLCJzZWxmX2Fzc2Vzc21lbnQiLCJyZWNjZSIsInZtX2luc3RhbGxhdGlvbiIsInZpZXdfdGFnIiwiYWN0aXZhdGVfdGFnIiwibWVhbHNfbWVudSIsInZtX3BhcmFtZXRlcnMiLCJzdXBwb3J0X25vdGlmaWNhdGlvbiIsInNsb3RfcmVwYWlyIiwicXVpY2tfdW5ibG9jayIsInVuYmxvY2tfYWxsX3Nsb3RzIiwic2xvdF9yZXBvcnQiLCJwYXJ0bmVyX3JlY2hhcmdlIiwib3JkZXJfYXBwcm92ZSIsImJ1bGtfcHVyY2hhc2UiLCJtb2JpbGl0eV9yZWZpbGwiLCJtYXBfYnBfdXNlciIsInZpZXdfZnJhbmNoaXNlZSIsImNyZWF0ZV9mcmFuY2hpc2VlIiwidmlld19iYW5rIiwiY3JlYXRlX2JhbmsiLCJ2aWV3X3BsYXRmb3JtX2NoYXJnZSIsIm1hcF9wbGF0Zm9ybV9jaGFyZ2UiLCJjcmVhdGVfcGxhdGZvcm1fY2hhcmdlIiwibW9iaWxpdHlfY2hlY2tpbiIsImNyZWF0ZV9jb3Vwb24iLCJ2aWV3X2NvdXBvbiIsImJ1eV9hdF92cCIsIm1hcF9tYWNoaW5lcyIsInZpZXdfYnBfdXNlciIsInZtX2NyZWF0ZSIsInZtX3VwZGF0ZSIsInZtX3ZpZXciLCJjb2hvcnRfY3JlYXRlIiwiY29ob3J0X3ZpZXciLCJjb2hvcnRfbWFwX3VzZXIiLCJjb2hvcnRfbWFwX21hY2hpbmUiLCJiYW5uZXJfY3JlYXRlIiwiYXBwcm92ZV9hdHRlbmRhbmNlIiwic2xvdF9vcGVyYXRpb24iLCJyZWZ1bmRfcmV2Il0sImlhdCI6MTY4MzYzMzIxMywiZXhwIjoxNjgzNzE5NjEzfQ.T99JHUBtENBuDLFMlu7wqJHy1vzm_aT6d11Z3K6FwTRYntnAJrMipuTyb7ra7nQD";
        String url = "https://api-stage.daalchini.co.in/partner/api/v2/warehouse/1/sez-location";
        aTask.execute(url,"","",token);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}