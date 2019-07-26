package pt.ipg.a.softdigital;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendToReceiver extends AppCompatActivity {

    private String receiverUserID;
  //  private String pdfURL;

    private TextView contactUserName, pdf_name;
    private Button send_pdf_to_receiver_button;

    private DatabaseReference UserRef;
  //  private DatabaseReference FilesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_receiver);

        getSupportActionBar().setTitle(R.string.send_to_receiver);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
      //  FilesRef = FirebaseDatabase.getInstance().getReference().child("Files");

        receiverUserID = getIntent().getExtras().get("click_user_id").toString();
       // pdfURL = getIntent().getExtras().get("pdfurl").toString();


        Toast.makeText(this, "User ID: " + receiverUserID, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this, "Url pdf: " + pdfURL, Toast.LENGTH_SHORT).show();

        contactUserName = (TextView) findViewById(R.id.contact_username_editText);
        pdf_name = (TextView) findViewById(R.id.document_name_editText);
        send_pdf_to_receiver_button = (Button) findViewById(R.id.send_pdf_to_receiver_button);

        RetrieveUserInfo();
       // RetrievePdfInfo();
    }

    private void RetrieveUserInfo(){

        UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("userName"))){

                    String userName = dataSnapshot.child("userName").getValue().toString();

                    contactUserName.setText(userName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    private void RetrievePdfInfo(){
//
//        FilesRef.child(pdfURL).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
//
//                    String pdfName = dataSnapshot.child("name").getValue().toString();
//
//                    pdf_name.setText(pdfName);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}
