package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView status_waiting_for_signature_textView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.add_file_button).setOnClickListener(this);
        findViewById(R.id.documents_button).setOnClickListener(this);

        status_waiting_for_signature_textView = (TextView) findViewById(R.id.status_waiting_for_signature_textView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Files");

        databaseReference.addValueEventListener(new ValueEventListener() { // Conta os documentos inseridos que estão por assinar
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int sum = 0;

                if(dataSnapshot.exists()) {

                    sum = (int) dataSnapshot.getChildrenCount();

                    status_waiting_for_signature_textView.setText(String.valueOf(sum));

                }else{

                    status_waiting_for_signature_textView.setText("0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.add_file_button){
            Intent intent = new Intent(this, FileUpload.class);
            startActivity(intent);
        }

        if(i == R.id.documents_button){
            Intent intent = new Intent(this, View_PDF_Files.class);
            startActivity(intent);
        }


    }
}
