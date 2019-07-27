package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendToReceiver extends AppCompatActivity  {

    private String receiverUserID, senderUserID;
    private String receiverPdfID;
    private String pdfURL;
    private String Current_State;

    private TextView contactUserName, pdf_name;
    private Button send_pdf_to_receiver_button;

    private DatabaseReference UserRef, ContactsRef;
    private DatabaseReference FilesRef;
    private DatabaseReference DocumentRequestRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_receiver);

        getSupportActionBar().setTitle(R.string.send_to_receiver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        senderUserID = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
        FilesRef = FirebaseDatabase.getInstance().getReference().child("Files");
        DocumentRequestRef = FirebaseDatabase.getInstance().getReference().child("Document Request");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

        receiverUserID = getIntent().getExtras().get("click_user_id").toString();
        Toast.makeText(this, "User ID: " + receiverUserID, Toast.LENGTH_SHORT).show();

        receiverPdfID = getIntent().getExtras().get("receiverPdfID").toString();
        Toast.makeText(this, "Pdf ID: " + receiverPdfID, Toast.LENGTH_SHORT).show();

        pdfURL = getIntent().getExtras().get("pdfurl").toString();
        Toast.makeText(this, "Url pdf: " + pdfURL, Toast.LENGTH_SHORT).show();

        contactUserName = (TextView) findViewById(R.id.contact_username_editText);
        pdf_name = (TextView) findViewById(R.id.document_name_editText);
        send_pdf_to_receiver_button = (Button) findViewById(R.id.send_pdf_to_receiver_button);

        Current_State = "new";

        RetrieveUserInfo();
        RetrievePdfInfo();
    }

    private void RetrieveUserInfo(){

        UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("userName"))){

                    String userName = dataSnapshot.child("userName").getValue().toString();

                    contactUserName.setText(userName);

                    ManageDocumentsRequest();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void RetrievePdfInfo(){

        FilesRef.child(receiverPdfID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){

                    String pdfName = dataSnapshot.child("name").getValue().toString();

                    pdf_name.setText(pdfName);

                    ManageDocumentsRequest();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void ManageDocumentsRequest(){

        DocumentRequestRef.child(senderUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(receiverUserID)){

                    String request_type = dataSnapshot.child(receiverUserID).child("request_type").getValue().toString();

                    if(dataSnapshot.hasChild(receiverPdfID)) {

                        String request_pdf_id = dataSnapshot.child(receiverPdfID).child("request_pdf_id").getValue().toString();

                        if (request_type.equals("send") && request_pdf_id.equals("send")) {

                            Current_State = "request_send";
                            //send_pdf_to_receiver_button.setText("Enviado");
                        }
                    }

                }
                else{

                    ContactsRef.child(senderUserID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(receiverUserID)){

                                        Current_State = "pendents";

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!senderUserID.equals(receiverUserID)){

            send_pdf_to_receiver_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    send_pdf_to_receiver_button.setEnabled(false);
                    Toast.makeText(SendToReceiver.this, "Mensagem enviada", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SendToReceiver.this, StatusDocumentsActivity.class);
                    startActivity(intent);

                    if(Current_State.equals("new")){

                        SendDocumentsRequest();

                    }
                    if(Current_State.equals("request_received")){

                        ReceivedDocumentRequest();

                    }

                }
            });

        }
        else {
            send_pdf_to_receiver_button.setVisibility(View.INVISIBLE);
        }

    }

    private void SendDocumentsRequest(){

        DocumentRequestRef.child(senderUserID).child(receiverUserID)
            .child("request_type").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                DocumentRequestRef.child(senderUserID).child(receiverUserID).child(receiverPdfID)
                        .child("request_pdf_id").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            DocumentRequestRef.child(receiverUserID).child(senderUserID)
                                    .child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        DocumentRequestRef.child(receiverUserID).child(senderUserID).child(receiverPdfID)
                                                .child("request_pdf_id").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                    send_pdf_to_receiver_button.setEnabled(true);
                                                    Current_State = "request_send";
                                                    send_pdf_to_receiver_button.setText("Enviado");

                                                }
                                            }
                                        });


                                    }

                                }
                            });
                        }
                    }

                });
            }
        });

    }

    private void ReceivedDocumentRequest(){

        ContactsRef.child(senderUserID).child(receiverUserID).child("Contacts").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            ContactsRef.child(receiverUserID).child(senderUserID).child("Contacts").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                DocumentRequestRef.child(senderUserID).child(receiverUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                DocumentRequestRef.child(receiverUserID).child(senderUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                send_pdf_to_receiver_button.setEnabled(true);
                                                                                Current_State = "pendents";

                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }

                                        }
                                    });
                        }

                    }
                });

    }
}
