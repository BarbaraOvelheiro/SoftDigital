package pt.ipg.a.softdigital;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestsContactsActivity extends AppCompatActivity {

    private RecyclerView contactsRequestsList;

    private DatabaseReference ContactsRequestRef, UsersRef, ContactsRef;
    private FirebaseAuth mAuth;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_contacts);

        getSupportActionBar().setTitle(R.string.contact_request);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        contactsRequestsList = (RecyclerView) findViewById(R.id.contacts_requests_list);
        contactsRequestsList.setLayoutManager(new LinearLayoutManager(this));

        ContactsRequestRef = FirebaseDatabase.getInstance().getReference().child("Contacts Request");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("User");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(ContactsRequestRef.child(currentUserID), User.class).build();

        FirebaseRecyclerAdapter<User, RequestsContactsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, RequestsContactsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestsContactsViewHolder holder, int position, @NonNull User model) {

                        holder.itemView.findViewById(R.id.request_accept_button).setVisibility(View.VISIBLE);

                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();

                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()){

                                    String type = dataSnapshot.getValue().toString();

                                    if(type.equals("received")){

                                        UsersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if(dataSnapshot.hasChild("userName")){

                                                    final String requestUserName = dataSnapshot.child("userName").getValue().toString();

                                                    holder.contact_username_editText.setText(requestUserName);

                                                }
                                                final String requestUserEmail = dataSnapshot.child("userEmail").getValue().toString();
                                                final String requestUserRegime = dataSnapshot.child("userRegime").getValue().toString();

                                                holder.contact_userEmail_editText.setText(requestUserEmail);
                                                holder.contact_userRegime_editText.setText(requestUserRegime);

                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        ContactsRef.child(currentUserID).child(list_user_id).child("Contacts").setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    ContactsRef.child(list_user_id).child(currentUserID).child("Contacts").setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                ContactsRequestRef.child(currentUserID).child(list_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    ContactsRequestRef.child(list_user_id).child(currentUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if (task.isSuccessful()) {

                                                                                                                        Toast.makeText(RequestsContactsActivity.this, R.string.sucessful_add_contact, Toast.LENGTH_SHORT).show();

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
                                                            }
                                                        });
                                                        }
                                                });

                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public RequestsContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        RequestsContactsViewHolder viewHolder = new RequestsContactsViewHolder(view);
                        return viewHolder;

                    }
                };

        contactsRequestsList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class RequestsContactsViewHolder extends RecyclerView.ViewHolder{

        TextView contact_username_editText, contact_userEmail_editText, contact_userRegime_editText;
        Button AcceptButton;

        public RequestsContactsViewHolder(@NonNull View itemView){

            super(itemView);

            contact_username_editText = itemView.findViewById(R.id.contact_username_editText);
            contact_userEmail_editText = itemView.findViewById(R.id.send_userName_editText);
            contact_userRegime_editText = itemView.findViewById(R.id.contact_userRegime_editView);
            AcceptButton = itemView.findViewById(R.id.request_accept_button);

        }

    }
}
