package pt.ipg.a.softdigital;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private String messageReceiverID, messageSenderID;
    private String pdfURL, receiverPdfID;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private DatabaseReference UserRef;
    private DatabaseReference FilesRef;

    private TextView contact_username_editText, document_name_editText;
    private Button send_pdf_to_receiver_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().setTitle(R.string.send_to_receiver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messageReceiverID = getIntent().getExtras().get("userIDs").toString();
        Toast.makeText(this, "user ID" + messageReceiverID, Toast.LENGTH_SHORT).show();

        pdfURL = getIntent().getExtras().get("pdfurl").toString();
        Toast.makeText(this, "Url pdf: " + pdfURL, Toast.LENGTH_SHORT).show();

        receiverPdfID = getIntent().getExtras().get("receiverPdfID").toString();
        Toast.makeText(this, "Pdf ID: " + receiverPdfID, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
        FilesRef = FirebaseDatabase.getInstance().getReference().child("Files");

        RootRef = FirebaseDatabase.getInstance().getReference();

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

        UserRef.child(messageReceiverID).addValueEventListener(new ValueEventListener() {
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

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){

                    String pdfName = dataSnapshot.child("name").getValue().toString();

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

//        String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
//        String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;
        String messageSenderRef = "Messages/" + messageSenderID ;
        String messageReceiverRef = "Messages/" + messageReceiverID ;

//        DatabaseReference userMessageKeyRef = RootRef.child("Messages").child(messageSenderRef).child(messageReceiverRef).push();

        DatabaseReference userMessageKeyRef = RootRef.child("Messages").child(messageSenderRef).push();

        String messagePushID = userMessageKeyRef.getKey();

        Map messageText = new HashMap();
        messageText.put("message", messageDocumentName);
        messageText.put("pdfurl", pdfURL);
        messageText.put("type", "text");
        messageText.put("from", messageSenderID);
        messageText.put("to", messageReceiverID);

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
        }
    }
}
