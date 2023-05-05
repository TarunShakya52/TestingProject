package com.example.demoapplication;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PdfPrintAdapter extends PrintDocumentAdapter {
        private File pdfFile;
        private int pageCount;

        public PdfPrintAdapter(File pdfFile) {
            this.pdfFile = pdfFile;
            this.pageCount = getPDFPageCount(pdfFile);
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }

            // Create a print document info
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("pdf_file.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(pageCount);

            PrintDocumentInfo info = builder.build();

            // Notify the system that the layout is complete and provide the document info
            callback.onLayoutFinished(info, true);
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            try {
                // Load the PDF file
                PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

                // Iterate through the requested pages and render them to the output stream
                for (int i = 0; i < pages.length; i++) {
                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                        renderer.close();
                        return;
                    }

                    PdfRenderer.Page page = renderer.openPage(pages[i].getStart());

                    // Create a bitmap for the page
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);

                    // Render the page to the bitmap
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

                    // Write the bitmap to the output stream
                    OutputStream out = new FileOutputStream(destination.getFileDescriptor());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();

                    page.close();
                    bitmap.recycle();

                    // Notify the system that the page has been written
                    callback.onWriteFinished(new PageRange[]{pages[i]});
                }

                renderer.close();

                // Notify the system that the write operation is complete
                callback.onWriteFinished(null);
            } catch (Exception e) {
                // Notify the system of an error during the write operation
                callback.onWriteFailed(e.toString());
            }
        }

        private int getPDFPageCount(File pdfFile) {
            int pageCount = 0;

            try {
                PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
                pageCount = renderer.getPageCount();
                renderer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return pageCount;
        }
    }

