package pt.ipg.a.softdigital;


import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Pdf_view extends AppCompatActivity {

    PDFView mPDFView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        mPDFView = (PDFView) findViewById(R.id.pdfView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vizualização");

        String pdfURL = getIntent().getStringExtra("pdfurl");
        try {
            URL url = new URL(pdfURL);
            new RetrievePDF().execute(pdfURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    class RetrievePDF extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL stringURL = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) stringURL.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            mPDFView.fromStream(inputStream).load();
        }
    }

}