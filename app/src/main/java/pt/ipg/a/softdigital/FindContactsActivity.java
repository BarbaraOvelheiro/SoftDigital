package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindContactsActivity extends AppCompatActivity {

    private RecyclerView FindContactsList;
    private DatabaseReference UsersRef, ContactsRequestRef;

    private String senderUserID, current_state;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_contacts);

        FindContactsList = (RecyclerView) findViewById(R.id.find_contacts_list);
        FindContactsList.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_contacts);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("User");
        ContactsRequestRef = FirebaseDatabase.getInstance().getReference().child("Contacts Request");

        mAuth = FirebaseAuth.getInstance();
        senderUserID = mAuth.getCurrentUser().getUid();

        current_state = "new";

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(UsersRef, User.class)
                        .build();

        FirebaseRecyclerAdapter<User, FindContactsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, FindContactsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FindContactsViewHolder holder, final int position, @NonNull User model) {

                        holder.contact_username_editText.setText(model.getUserName());
                        holder.contact_userEmail_editText.setText(model.getUserEmail());
                        holder.contact_userRegime_editText.setText(model.getUserRegime());


                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final String receiverUserID = getRef(position).getKey();

                                    if(!senderUserID.equals(receiverUserID)){

                                        holder.contact_username_editText.setEnabled(false);
                                        holder.contact_userEmail_editText.setEnabled(false);
                                        holder.contact_userRegime_editText.setEnabled(false);

                                        if(current_state.equals("new")){

                                            ContactsRequestRef.child(senderUserID).child(receiverUserID)
                                                    .child("request_type").setValue("send")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){

                                                                ContactsRequestRef.child(receiverUserID).child(senderUserID)
                                                                        .child("request_type").setValue("received")
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                if(task.isSuccessful()){
                                                                                    holder.contact_username_editText.setEnabled(true);
                                                                                    holder.contact_userEmail_editText.setEnabled(true);
                                                                                    holder.contact_userRegime_editText.setEnabled(true);
                                                                                    current_state = "request_send";

                                                                                    Toast.makeText(FindContactsActivity.this, R.string.add_contact_send_message, Toast.LENGTH_SHORT).show();
                                                                                }

                                                                            }
                                                                        });

                                                            }

                                                        }
                                                    });

                                        }

                                    }


                                }
                            });

                    }

                    @NonNull
                    @Override
                    public FindContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        FindContactsViewHolder viewHolder = new FindContactsViewHolder(view);
                        return viewHolder;

                    }
                };

        FindContactsList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class FindContactsViewHolder extends RecyclerView.ViewHolder{

        TextView contact_username_editText, contact_userEmail_editText, contact_userRegime_editText;

        public FindContactsViewHolder(@NonNull View itemView){

            super(itemView);

            contact_username_editText = itemView.findViewById(R.id.contact_username_editText);
            contact_userEmail_editText = itemView.findViewById(R.id.send_userName_editText);
            contact_userRegime_editText = itemView.findViewById(R.id.contact_userRegime_editView);

        }

    }


}
