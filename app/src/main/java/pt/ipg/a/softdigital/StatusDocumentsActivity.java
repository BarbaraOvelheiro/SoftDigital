package pt.ipg.a.softdigital;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class StatusDocumentsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_documents);

        getSupportActionBar().setTitle(R.string.files);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
