package com.example.demoapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapplication.Adapter.CheckpointAdapter;
import com.example.demoapplication.Adapter.InvoiceData_Adapter;
import com.example.tscdll.TSCActivity;
import com.example.tscdll.TSCUSBActivity;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class InvoiceAnother_activity extends AppCompatActivity {

    ArrayList<String> arrlist = new ArrayList<>();
    TableLayout tableLayout;
    String[][] data;
    Button btnPrint;
    ConstraintLayout layout;
    String pdfPath;
    File file;
    RecyclerView recyclerView;
    View view;
    private static final int TSC_VENDOR_ID = 4611;
    private static final int TSC_PRODUCT_ID = 6161;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static UsbManager mUsbManager;
    private static PendingIntent mPermissionIntent;
    private static UsbDevice device;
    TSCUSBActivity tscusbActivity;
    InvoiceData_Adapter invoiceData_adapter;
    private static boolean hasPermissionToCommunicate = false;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @SuppressLint({"MissingInflatedId", "ResourceType", "InflateParams"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_another);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_invoice_another, null);


        recyclerView = findViewById(R.id.recyclerView3);

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


        data = new String[][]{
                {"Cell 1", "Cell 2", "Cell 3"},
                {"Cell 4", "Cell 5", "Cell 6"},
                {"Cell 7", "Cell 8", "Cell 9"}
        };

        btnPrint = findViewById(R.id.btnPrint);


        tableLayout = findViewById(R.id.tableLayout);


        arrlist.add("Rasna");
        arrlist.add("Gold");
        arrlist.add("Chai");
        arrlist.add("Wheat");

        for (int i = 0; i <arrlist.size(); i++) {
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setBackgroundResource(R.drawable.cell_border);
            textView.setText(arrlist.get(i));
            tableRow.addView(textView);
            tableLayout.addView(tableRow);
        }

        tableLayout = findViewById(R.id.tableLayout);

//         Iterate through the array and create a new TableRow for each item
//
//         Iterate through the data array and create a new TableRow for each row
        for (int i = 0; i < data.length; i++) {
            TableRow tableRow = new TableRow(this);

            // Iterate through the columns in the current row and create a new TextView for each cell
            for (int j = 0; j < data[i].length; j++) {
                TextView textView = new TextView(this);
                textView.setText(data[i][j]);
                textView.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                ));
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(getDrawable(R.drawable.cell_border));
                tableRow.addView(textView);
            }

            // Add the current row to the TableLayout
            tableLayout.addView(tableRow);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPermissionIntent = PendingIntent.getBroadcast(this, 0,  new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
        } else {
            mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if (device != null) {
            mUsbManager.requestPermission(device, mPermissionIntent);
        }

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (device != null){
                if (mUsbManager.hasPermission(device))
                {
                    tscusbActivity.openport(mUsbManager, device);

                    tscusbActivity.setup(70, 50, 4, 4, 0, 0,0);

                    tscusbActivity.sendcommand("PDF-IMAGE\n");

//                    tscusbActivity.sendcommand("CLS\r\n");
//                    tscusbActivity.sendcommand("PRINT " + "number" + "\r\n");
                    pdfPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();

//                    file = new File(pdfPath,"firstpdf (1)(1).pdf");
                    file = new File(pdfPath,"Invoice.pdf");
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
                    Toast.makeText(InvoiceAnother_activity.this, "Permission not given", Toast.LENGTH_SHORT).show();
                }
            }else {
                    Toast.makeText(InvoiceAnother_activity.this,"Device not connected",Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        assert recyclerView != null;
        recyclerView.setLayoutManager(mLayoutManager);
        invoiceData_adapter = new InvoiceData_Adapter(this);
        recyclerView.setAdapter(invoiceData_adapter);

        MyAsyncTask task = new MyAsyncTask();

        // Execute the AsyncTask and pass the view as an argument
        task.execute(view);
    }



    private byte[] getPrintData(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] data = new byte[width * height / 8];

        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = bitmap.getPixel(x, y);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);

                if (gray > 128) {
                    data[i / 8] |= (byte) (0x80 >> (i % 8));
                }

                i++;
            }
        }

        return data;
    }


    private void convertToPdf(View view) {
        view.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void run() {
                try {
                    // Create a PDF document
                    PDDocument document = new PDDocument();

                    // Create a page in the document
                    PDPage page = new PDPage();
                    document.addPage(page);

                    // Create a content stream for the page
                    PDPageContentStream contentStream = new PDPageContentStream(document, page);

                    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

                    // Convert the view to a bitmap
                    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    view.draw(canvas);

                    // Create an image object from the bitmap
                    PDImageXObject image = LosslessFactory.createFromImage(document, bitmap);

                    // Add the image to the page
                    contentStream.drawImage(image, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

                    // Close the content stream
                    contentStream.close();

                    // Save the document to a file
                    pdfPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
                    file = new File(pdfPath,"Invoice.pdf");
                    document.save(file);

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME, "Invoice.pdf");
                    values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    Uri uri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    }

                    try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                        document.save(outputStream);
                        Toast.makeText(getApplicationContext(), "PDF file saved", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed to save PDF file", Toast.LENGTH_SHORT).show();
                    }
                    // Close the document
                    document.close();
                } catch (IOException e) {
                    Log.e("createPdfError",e.getMessage());
                }
            }
        });
    }

//
//    private void convertToPdf() throws IOException {
//
//
//        PdfDocument document = new PdfDocument();
//
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size in points (1/72 inch)
//
//// Add a new page to the document with the specified page info
//        PdfDocument.Page page = document.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//
//
//        // Create a custom view that inflates the XML layout you want to draw
//        CustomView customView = new CustomView(this, R.layout.activity_invoice);
//
//// Draw the custom view onto the canvas
//        customView.draw(canvas);
//
//// Finish the page
//        document.finishPage(page);
//
//
//// Finish the PDF document and save it to disk
//
//        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        File pdfFile = new File(downloadsDir, "myfile.pdf");
//        FileOutputStream outputStream = new FileOutputStream(pdfFile);
//        document.writeTo(outputStream);
//        document.close();
//        outputStream.close();
//    }

//    private void convertToPdf() throws IOException {
//        PdfDocument document = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size in points (1/72 inch)
//
//        PdfDocument.Page page = document.startPage(pageInfo);
//
////        CustomView customViewP = new CustomView(this, R.layout.activity_invoice_another);
//        LinearLayout customView = findViewById(R.id.layoutPrint);
//
//        customView.measure(View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY));
//        customView.layout(0, 0, customView.getWidth(), customView.getHeight());
//        page.getCanvas().save();
//        page.getCanvas().translate(0, 0);
//        customView.draw(page.getCanvas());
//        page.getCanvas().restore();
//
//        document.finishPage(page);
//
//
//        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        File pdfFile = new File(downloadsDir, "myfile.pdf");
//        FileOutputStream outputStream = new FileOutputStream(pdfFile);
//        document.writeTo(outputStream);
//        document.close();
//        outputStream.close();
//    }

    // Method to retrieve the connected TSC printer's IP address
    private String getPrinterIpAddress() {
        String printerIpAddress = null;

        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

        for (UsbDevice device : deviceList.values()) {
            if (device.getVendorId() == TSC_VENDOR_ID) {
                UsbDeviceConnection connection = usbManager.openDevice(device);
                if (connection != null) {
                    byte[] buffer = new byte[64];
                    int length = connection.controlTransfer(0xc0, 0xdf, 0, 0, buffer, buffer.length, 100);
                    connection.close();
                    if (length >= 2) {
                        printerIpAddress = new String(buffer, 2, length - 2);
                    } else {
                        Toast.makeText(InvoiceAnother_activity.this,String.valueOf(length),Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }

        return printerIpAddress;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, save the PDF file
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    convertToPdf(view);
                }
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Write permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSION_REQUEST_CODE);
            return;
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                convertToPdf(view);
            }
        }
    }

    public class CustomView extends View {

        private View mView;

        public CustomView(Context context, int layoutId) {
            super(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = inflater.inflate(layoutId, null);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            mView.draw(canvas);
            canvas.restore();
        }
    }

    private class MyAsyncTask extends AsyncTask<View, Void, Boolean> {

        @Override
        protected Boolean doInBackground(View... views) {
            View view = views[0];

            convertToPdf(view);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(InvoiceAnother_activity.this, "PDF saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(InvoiceAnother_activity.this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }
}