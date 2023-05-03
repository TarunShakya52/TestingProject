package com.example.demoapplication;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintJobInfo;
import android.print.PrintManager;
import android.print.PrinterInfo;
import android.printservice.PrinterDiscoverySession;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.demoapplication.Adapter.PdfPrintDocumentAdapter;
import com.example.tscdll.TSCUSBActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
public class InvoiceActivity extends AppCompatActivity {

    private static final int DIVIDER_SIZE = 2;
    LinearLayout layout;
    com.itextpdf.layout.Document document;
    String pdfPath;
    File file;
    Uri uri;
//    Boolean checkForHeaders = true;
    Boolean checkForHeaders2 = true;
    private static UsbDevice device;
    private static UsbManager mUsbManager;
    private static PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static boolean hasPermissionToCommunicate = false;

    MaterialButton btnPrintPDF;
    Bitmap bitmap;
    TSCUSBActivity tscusbActivity;
    private static final int TSC_VENDOR_ID = 0x0BB4;
    private static final int TSC_PRODUCT_ID = 0x0303;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
//        layout = findViewById(R.id.layoutLinearView);

        tscusbActivity = new TSCUSBActivity();


        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

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
        try{
            createPDF();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

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



    private void createPDF() throws FileNotFoundException {
        pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        file = new File(pdfPath,"myPDF_PDF.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        document = new  com.itextpdf.layout.Document(pdfDocument,PageSize.A4);
        uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", file);
        float columnWidthHeading[] = {820};
        Table table1 = new Table(columnWidthHeading);
        Table tableShippingAddress = new Table(columnWidthHeading);
        Table tableDeliveryAddress = new Table(columnWidthHeading);
        table1.setMarginTop(40.0f);
        tableShippingAddress.setMarginTop(40.0f);
        tableDeliveryAddress.setMarginTop(40.0f);
        addColumnHeaders(table1);
        addColumnHeadersDelivery(tableDeliveryAddress);
        addColumnHeadersShipping(tableShippingAddress);
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

        float columnWidthHeading2[] = {100,300,100,100,100,120,100};
        Table table2 = new Table(columnWidthHeading2);
        table2.setMarginTop(40.0f);
        addColumnHeaders2(table2);

        for (int i=0 ; i<20 ; i++){

            table2.addCell(new Cell().add(new Paragraph("1").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("Namkeen").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("2342354").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("2").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("20").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("10").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("34t54y6uu").setTextAlignment(TextAlignment.CENTER)).setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(10f));

            table2.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("3.14").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("2.63").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("5.56").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("24").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("45.2").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
            table2.addCell(new Cell().add(new Paragraph("23").setTextAlignment(TextAlignment.CENTER)).setFontSize(10f));
        }
        document.add(table2);

        document.close();
        showdoc();
    }

    private void addColumnHeadersDelivery(Table tableDeliveryAddress) {
        Cell cell = new Cell();
        Cell shippingCell = new Cell();
        tableDeliveryAddress.addCell(shippingCell.setPadding(10f).setBackgroundColor(new DeviceRgb(107, 76, 82)));
        tableDeliveryAddress.addCell(cell.setPadding(10f));
        Paragraph p1 = new Paragraph("Invoice No : DFB/MH/21/22/006");
        Paragraph p2 = new Paragraph("Date: 30-03-2022");
        Paragraph p3 = new Paragraph("Daalchini Foods & Beverages Pvt.Ltd").setBold().setFontSize(15).setMarginTop(10);
        Paragraph p4 = new Paragraph("Plot no 505,Udyog Vihar,Phase-V,");
        Paragraph p5 = new Paragraph("Sector-19,Near Shankar Chowk,");
        Paragraph p6 = new Paragraph("Gurugram-122002");
        Paragraph p7 = new Paragraph("GSTIN - 052HGGF324F");

        shippingCell.add(new Paragraph("Delivery Address").setBold().setFontSize(17).setPadding(5f).setFontColor(new DeviceRgb(255,255,255)));
        cell.add(p1);
        cell.add(p2);
        cell.add(p3);
        cell.add(p4);
        cell.add(p5);
        cell.add(p6);
        cell.add(p7);
    }

    private void addColumnHeadersShipping(Table tableShippingAddress) {

        Cell cell = new Cell();
        Cell shippingCell = new Cell();
        tableShippingAddress.addCell(shippingCell.setPadding(10f).setBackgroundColor(new DeviceRgb(107, 76, 82)));
        tableShippingAddress.addCell(cell.setPadding(10f));
        Paragraph p1 = new Paragraph("Invoice No : DFB/MH/21/22/006");
        Paragraph p2 = new Paragraph("Date: 30-03-2022");
        Paragraph p3 = new Paragraph("Daalchini Foods & Beverages Pvt.Ltd").setBold().setFontSize(15).setMarginTop(10);
        Paragraph p4 = new Paragraph("Plot no 505,Udyog Vihar,Phase-V,");
        Paragraph p5 = new Paragraph("Sector-19,Near Shankar Chowk,");
        Paragraph p6 = new Paragraph("Gurugram-122002");
        Paragraph p7 = new Paragraph("GSTIN - 052HGGF324F");

        shippingCell.add(new Paragraph("Shipping Address").setBold().setFontSize(17).setPadding(5f).setFontColor(new DeviceRgb(255,255,255)));
        cell.add(p1);
        cell.add(p2);
        cell.add(p3);
        cell.add(p4);
        cell.add(p5);
        cell.add(p6);
        cell.add(p7);
    }

    private void addColumnHeaders2(Table table2) {
        if (checkForHeaders2)
        {
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
            checkForHeaders2 = false;
        }
    }

    private void addColumnHeaders(Table table1) {
//        if (checkForHeaders)
//        {

            Cell cell = new Cell();
            table1.addCell(cell.setPadding(10f));
            Paragraph p1 = new Paragraph("Invoice No : DFB/MH/21/22/006");
            Paragraph p2 = new Paragraph("Date: 30-03-2022");
            Paragraph p3 = new Paragraph("Daalchini Foods & Beverages Pvt.Ltd").setBold().setFontSize(15).setMarginTop(10);
            Paragraph p4 = new Paragraph("Plot no 505,Udyog Vihar,Phase-V,");
            Paragraph p5 = new Paragraph("Sector-19,Near Shankar Chowk,");
            Paragraph p6 = new Paragraph("Gurugram-122002");
            Paragraph p7 = new Paragraph("GSTIN - 052HGGF324F");


            cell.add(p1);
            cell.add(p2);
            cell.add(p3);
            cell.add(p4);
            cell.add(p5);
            cell.add(p6);
            cell.add(p7);
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
}