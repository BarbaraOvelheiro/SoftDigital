package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private String userReceiverID, currentUserID;
    private String pdfURL, receiverPdfID, receiverStatusID;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private DatabaseReference UserRef;
    private DatabaseReference FilesRef, documentStatus;

    private TextView contact_username_editText, document_name_editText;
    private Button send_pdf_to_receiver_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().setTitle(R.string.send_to_receiver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userReceiverID = getIntent().getExtras().get("userIDs").toString();
        Toast.makeText(this, "user ID" + userReceiverID, Toast.LENGTH_SHORT).show();

        pdfURL = getIntent().getExtras().get("pdfurl").toString();
        Toast.makeText(this, "Url pdf: " + pdfURL, Toast.LENGTH_SHORT).show();

        receiverPdfID = getIntent().getExtras().get("receiverPdfID").toString();
        Toast.makeText(this, "Pdf ID: " + receiverPdfID, Toast.LENGTH_SHORT).show();

        receiverStatusID = getIntent().getExtras().get("receiverStatusID").toString();
        Log.v("MessageActivity", "receiverStatusID: " + receiverStatusID);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
        FilesRef = FirebaseDatabase.getInstance().getReference().child("Files").child(currentUserID);
        RootRef = FirebaseDatabase.getInstance().getReference();
        documentStatus = FirebaseDatabase.getInstance().getReference().child("Document Status");

        contact_username_editText = (TextView) findViewById(R.id.contact_username_editText);
        document_name_editText = (TextView) findViewById(R.id.document_name_editText);

        send_pdf_to_receiver_button = (Button) findViewById(R.id.send_pdf_to_receiver_button);
        send_pdf_to_receiver_button.setOnClickListener(this);

        RetrieveUserInfo();
        RetrievePdfInfo();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
    }

    private void RetrieveUserInfo(){

        UserRef.child(userReceiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("userName"))){

                    String userName = dataSnapshot.child("userName").getValue().toString();

                    contact_username_editText.setText(userName);

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

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("documentName"))){

                    String pdfName = dataSnapshot.child("documentName").getValue().toString();

                    document_name_editText.setText(pdfName);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendMessage(){

        String messageDocumentName = document_name_editText.getText().toString();

        final String messageSenderRef = "Messages/" + currentUserID;
        final String messageReceiverRef = "Messages/" + userReceiverID;

        DatabaseReference userMessageKeyRef = RootRef.child("Messages").child(messageSenderRef).push();
        String messagePushID = userMessageKeyRef.getKey();

        String statusIN = "e_necessario_assinar";
        final String statusID = documentStatus.push().getKey();
        DocumentStatus infoStatus = new DocumentStatus(statusIN, statusID, receiverPdfID, userReceiverID, messagePushID);

        documentStatus.child(userReceiverID).child(statusID)
                    .setValue(infoStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.v("MessageActivity", "e_necessario_assinar inserido com sucesso");
                    }

                }
                    });


        String statusOP = "a_aguardar_por_outros";
        DocumentStatus infStatus = new DocumentStatus(statusOP,receiverStatusID,receiverPdfID,currentUserID, messagePushID);

        documentStatus.child(currentUserID).child(receiverStatusID).setValue(infStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.v("MessageActivity", "a_aguardar_por_outros inserido com sucesso");
                    }
                }
            });

        Map messageText = new HashMap();
        messageText.put("messageID", messagePushID);
        messageText.put("documentName", messageDocumentName);
        messageText.put("documentUrl", pdfURL);
        messageText.put("userFromID", currentUserID);
        messageText.put("userTOID", userReceiverID);
        messageText.put("statusID", receiverStatusID);
        messageText.put("documentID", receiverPdfID);

        Map messageDetails = new HashMap();
        messageDetails.put(messageSenderRef + "/" + messagePushID, messageText);
        messageDetails.put(messageReceiverRef + "/" + messagePushID, messageText);

        RootRef.updateChildren(messageDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful()){

                    Toast.makeText(MessageActivity.this, R.string.sucessful_send_message, Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(MessageActivity.this, R.string.error_send_message, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.send_pdf_to_receiver_button){
            SendMessage();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


}
