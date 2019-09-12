package pt.ipg.a.softdigital;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NeedToSignDocumentosFragment extends Fragment {

    private static final String TAG = "NeedToSignDocumentosFragment";

    private View NeedToSignDocuments;

    private RecyclerView receiver_documents_list;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, MessagesRef;

    private String currentUserID;


    public NeedToSignDocumentosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NeedToSignDocuments =  inflater.inflate(R.layout.fragment_need_to_sign_documentos, container, false);

        receiver_documents_list = (RecyclerView) NeedToSignDocuments.findViewById(R.id.receiver_documents_list);
        receiver_documents_list.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("User");

        return NeedToSignDocuments;

    }

    @Override
    public void onStart() {
        super.onStart();

        /************/
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Messages>()
                .setQuery(MessagesRef, Messages.class).build();

        FirebaseRecyclerAdapter<Messages, NeedToSignDocumentsFragmentViewHolder> adapter
                = new FirebaseRecyclerAdapter<Messages, NeedToSignDocumentsFragmentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final NeedToSignDocumentsFragmentViewHolder holder, final int position, @NonNull final Messages model) {

                String fromUserID = model.getFrom();

                holder.document_receive_name_editText.setText(model.getMessage());

                UserRef = FirebaseDatabase.getInstance().getReference().child("User").child(fromUserID);

                UserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("userName")) {

                            String receiverName = dataSnapshot.child("userName").getValue().toString();
                            holder.send_userName_editText.setText(receiverName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // String click_document_id = getRef(position).getKey();
                                String url = model.getPdfurl();
                                String receiverPdfID = getRef(position).getKey();

                                Intent intent = new Intent(getActivity(), Pdf_view.class);
                                intent.putExtra("pdfurl", url);
                                intent.putExtra("receiverPdfID", receiverPdfID);
                                startActivity(intent);
                            }
                        });

                    }
                });

                if(fromUserID.equals(currentUserID)) {
                    holder.linearLayoutView.setVisibility(View.INVISIBLE);
                }else {
                    holder.linearLayoutView.setVisibility(View.VISIBLE);
                }

//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                                String url = model.getPdfurl();
//                                Intent intent = new Intent(getActivity(), Pdf_view.class);
//                                intent.putExtra("pdfurl",url);
//                                startActivity(intent);
//
//
//                            }
//                        });
//
//                    }
//                });
            }

            @NonNull
            @Override
            public NeedToSignDocumentsFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_receiver_layout, viewGroup, false);
                NeedToSignDocumentsFragmentViewHolder viewHolder = new NeedToSignDocumentsFragmentViewHolder(view);

                mAuth = FirebaseAuth.getInstance();

                return viewHolder;

            }
        };
        receiver_documents_list.setAdapter(adapter);
        adapter.startListening();
        /*************/
    }

    public static class NeedToSignDocumentsFragmentViewHolder extends RecyclerView.ViewHolder{

        TextView document_receive_name_editText, send_userName_editText;
        LinearLayout linearLayoutView;

        public NeedToSignDocumentsFragmentViewHolder(@NonNull View itemView) {
            super(itemView);

            document_receive_name_editText = itemView.findViewById(R.id.document_receive_name_editText);
            send_userName_editText = itemView.findViewById(R.id.send_userName_editText);
            linearLayoutView = itemView.findViewById(R.id.linearLayoutView);
        }
    }

}
