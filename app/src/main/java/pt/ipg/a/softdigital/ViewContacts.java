package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewContacts extends AppCompatActivity {

    private RecyclerView SeeContactsRecyclerList;
    private DatabaseReference UsersContacts;
    private String pdfURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_contacts);

        UsersContacts = FirebaseDatabase.getInstance().getReference().child("User");

        SeeContactsRecyclerList = (RecyclerView) findViewById(R.id.see__contacts_recycler_list);
        SeeContactsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setTitle("Contactos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pdfURL = getIntent().getExtras().get("pdfurl").toString();

        Toast.makeText(this, "Url pdf: " + pdfURL, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(UsersContacts, User.class)
                .build();

        FirebaseRecyclerAdapter<User, SeeContactsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, SeeContactsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SeeContactsViewHolder holder, final int position, @NonNull User model) {

                        holder.contact_username_editText.setText(model.getUserName());
                        holder.contact_userEmail_editText.setText(model.getUserEmail());
                        holder.contact_userRegime_editText.setText(model.getUserRegime());


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String click_user_id = getRef(position).getKey();

                                Intent intent = new Intent(ViewContacts.this, SendToReceiver.class);
                                intent.putExtra("click_user_id", click_user_id);
                                intent.putExtra("pdfurl", pdfURL);
                                startActivity(intent);


                            }
                        });

                    }

                    @NonNull
                    @Override
                    public SeeContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        SeeContactsViewHolder viewHolder = new SeeContactsViewHolder(view);
                        return viewHolder;

                    }
                };

        SeeContactsRecyclerList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class SeeContactsViewHolder extends RecyclerView.ViewHolder{

        TextView contact_username_editText, contact_userEmail_editText, contact_userRegime_editText;

        public SeeContactsViewHolder(@NonNull View itemView){

            super(itemView);

            contact_username_editText = itemView.findViewById(R.id.contact_username_editText);
            contact_userEmail_editText = itemView.findViewById(R.id.contact_userEmail_editText);
            contact_userRegime_editText = itemView.findViewById(R.id.contact_userRegime_editView);

        }

    }
}
