package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_PDF_Files extends AppCompatActivity {

    ListView pdf_List_View;
    DatabaseReference databaseReference;
    List<UploadPdf> uploadPdfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__pdf__files);

        pdf_List_View = (ListView)findViewById(R.id.pdf_List_View);
        uploadPdfs = new ArrayList<>();

        viewAllFiles();

        pdf_List_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UploadPdf uploadPdf = uploadPdfs.get(position);

                Intent intent = new Intent(View_PDF_Files.this, Pdf_view.class);
                intent.putExtra("pdfurl", uploadPdf.getUrl());
                startActivity(intent);
                finish();

            }
        });

    }

    private void viewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Files");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    UploadPdf uploadPdf = postSnapshot.getValue(UploadPdf.class);
                    uploadPdfs.add(uploadPdf);

                }

                String[] uploads = new String[uploadPdfs.size()];

                for(int i = 0 ; i < uploads.length ; i++){

                    uploads[i] = uploadPdfs.get(i).getName();

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, uploads);
                pdf_List_View.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
