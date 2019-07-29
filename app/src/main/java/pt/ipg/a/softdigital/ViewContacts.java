package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewContacts extends AppCompatActivity {

    private RecyclerView ContactsList;
    private DatabaseReference ContactsRef, UserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private String pdfURL;
    private String receiverPdfID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        ContactsList = (RecyclerView) findViewById(R.id.contacts_list);
        ContactsList.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setTitle("Contactos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUserID =mAuth.getCurrentUser().getUid();

        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("User");

        pdfURL = getIntent().getExtras().get("pdfurl").toString();
        Toast.makeText(this, "Url pdf: " + pdfURL, Toast.LENGTH_SHORT).show();

        receiverPdfID = getIntent().getExtras().get("receiverPdfID").toString();
        Toast.makeText(this, "Pdf ID: " + receiverPdfID, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<User>().setQuery(ContactsRef, User.class).build();

        FirebaseRecyclerAdapter<User, ContactsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, ContactsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, final int position, @NonNull User model) {

                        final String userIDs = getRef(position).getKey();

                        UserRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild("userName")){

                                    String username = dataSnapshot.child("userName").getValue().toString();
                                    String useremail = dataSnapshot.child("userEmail").getValue().toString();
                                    String userRegime = dataSnapshot.child("userRegime").getValue().toString();

                                    holder.contact_username_editText.setText(username);
                                    holder.contact_userEmail_editText.setText(useremail);
                                    holder.contact_userRegime_editText.setText(userRegime);

                                }else{
                                    String username = dataSnapshot.child("userName").getValue().toString();
                                    String useremail = dataSnapshot.child("userEmail").getValue().toString();
                                    String userRegime = dataSnapshot.child("userRegime").getValue().toString();

                                    holder.contact_username_editText.setText(username);
                                    holder.contact_userEmail_editText.setText(useremail);
                                    holder.contact_userRegime_editText.setText(userRegime);
                                }

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(ViewContacts.this, MessageActivity.class);
                                        intent.putExtra("userIDs", userIDs);
                                        intent.putExtra("pdfurl", pdfURL);
                                        intent.putExtra("receiverPdfID", receiverPdfID);
                                        startActivity(intent);

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                        return viewHolder;

                    }
                };

        ContactsList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView contact_username_editText, contact_userEmail_editText, contact_userRegime_editText;

        public ContactsViewHolder(@NonNull View itemView){

            super(itemView);

            contact_username_editText = itemView.findViewById(R.id.contact_username_editText);
            contact_userEmail_editText = itemView.findViewById(R.id.send_userName_editText);
            contact_userRegime_editText = itemView.findViewById(R.id.contact_userRegime_editView);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.find_contacts_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.add_contacts_menu){

            Intent intent = new Intent(this, FindContactsActivity.class);
            startActivity(intent);

        }
        if(item.getItemId() == R.id.contacts_requests_menu){

            Intent intent = new Intent(this, RequestsContactsActivity.class);
            startActivity(intent);

        }


        return true;
    }
}
