package pt.ipg.a.softdigital;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView status_for_signing_textView, status_waiting_for_signature_textView;

    private DatabaseReference documentStatus, files;
    FirebaseAuth mAuth;
    private String currentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.add_file_button).setOnClickListener(this);
        findViewById(R.id.documents_button).setOnClickListener(this);
        findViewById(R.id.sign_and_send_button2).setOnClickListener(this);

        status_for_signing_textView = (TextView) findViewById(R.id.status_for_signing_textView);
        status_waiting_for_signature_textView = (TextView) findViewById(R.id.status_waiting_for_signature_textView);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        files = FirebaseDatabase.getInstance().getReference().child("Files");

        final DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("status");

        statusRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String status = dataSnapshot.getValue().toString();

                int sum = 0;
                Log.v("MainActivity", "O QUE MERDA SAI AQUI: " + status);
                if(dataSnapshot.exists()) {


                    sum = (int) dataSnapshot.getChildrenCount();

                    status_waiting_for_signature_textView.setText(String.valueOf(sum));

                }else{

                    status_waiting_for_signature_textView.setText("0");

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        documentStatus = FirebaseDatabase.getInstance().getReference().child("Document Status").child(currentUserID);

        documentStatus.addValueEventListener(new ValueEventListener() { // Conta os documentos inseridos que estão por assinar
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

        if (i == R.id.sign_and_send_button2){
            Intent intent = new Intent(this, StatusDocumentsActivity.class);
            startActivity(intent);
        }

        if(i == R.id.documents_button){
            Intent intent = new Intent(this, StatusDocumentsActivity.class);
            startActivity(intent);
        }


    }


}
