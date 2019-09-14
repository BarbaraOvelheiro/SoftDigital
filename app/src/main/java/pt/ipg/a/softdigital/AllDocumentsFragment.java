package pt.ipg.a.softdigital;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class AllDocumentsFragment extends Fragment {

    private static final String TAG = "AllDocumentsFragment";

    private View AllDocumentsView;
    private RecyclerView AllDocumentsList;

    private DatabaseReference FilesRef, documentStatus, MessagesRef;

    FirebaseAuth mAuth;
    private String currentUserID;


    public AllDocumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AllDocumentsView = inflater.inflate(R.layout.fragment_all_documents, container, false);

        AllDocumentsList = (RecyclerView) AllDocumentsView.findViewById(R.id.all_documents_list);
        AllDocumentsList.setLayoutManager(new LinearLayoutManager(getContext()));

        FilesRef = FirebaseDatabase.getInstance().getReference().child("Files");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        documentStatus = FirebaseDatabase.getInstance().getReference().child("Document Status").child(currentUserID);
        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserID);

        return AllDocumentsView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<DocumentStatus>()
                .setQuery(documentStatus, DocumentStatus.class)
                .build();

        FirebaseRecyclerAdapter<DocumentStatus, AllDocumentsViewHolder> adapter
                = new FirebaseRecyclerAdapter<DocumentStatus, AllDocumentsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AllDocumentsViewHolder holder, final int position, @NonNull final DocumentStatus model) {

                String messageID = model.getMessageID();

                if (messageID == null){

                    final String documentID = model.getDocumentID();
                    FilesRef = FirebaseDatabase.getInstance().getReference().child("Files").child(currentUserID).child(documentID);
                    FilesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("documentName")) {
                                String docuName = dataSnapshot.child("documentName").getValue().toString();
                                holder.document_receive_name_editText.setText(docuName);
                                Log.v(TAG, "docuname:  " + docuName);
                            }
                            if(dataSnapshot.hasChild("documentUrl")){
                                final String url = dataSnapshot.child("documentUrl").getValue().toString();
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String receiverStatusID = model.getStatusID();

                                                Intent intent = new Intent(getActivity(), Pdf_view.class);
                                                intent.putExtra("pdfurl", url);
                                                intent.putExtra("receiverPdfID", documentID);
                                                intent.putExtra("receiverStatusID", receiverStatusID);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    holder.fromtextView.setVisibility(View.VISIBLE);
                    holder.send_userName_editText.setVisibility(View.VISIBLE);
                    holder.linearLayoutView.setVisibility(View.VISIBLE);

                    MessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(messageID);
                    MessagesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("documentName")){
                                String docuName = dataSnapshot.child("documentName").getValue().toString();
                                holder.document_receive_name_editText.setText(docuName);
                            }
                            if(dataSnapshot.hasChild("documentUrl")){
                                final String url = dataSnapshot.child("documentUrl").getValue().toString();
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String receiverStatusID = model.getStatusID();
                                                String documentID = model.getDocumentID();

                                                Intent intent = new Intent(getActivity(), Pdf_view.class);
                                                intent.putExtra("pdfurl", url);
                                                intent.putExtra("receiverPdfID", documentID);
                                                intent.putExtra("receiverStatusID", receiverStatusID);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                String status = model.getStatus();
                if (status.equals("e_necessario_assinar")){
                    holder.fromtextView.setVisibility(View.VISIBLE);
                    holder.send_userName_editText.setVisibility(View.VISIBLE);
                    holder.fromtextView.setText("Estado: ");
                    holder.send_userName_editText.setText("é necessário assinar");
                }else if(status.equals("a_aguardar_por_outros")){
                    holder.fromtextView.setVisibility(View.VISIBLE);
                    holder.send_userName_editText.setVisibility(View.VISIBLE);
                    holder.fromtextView.setText("Estado: ");
                    holder.send_userName_editText.setText("a aguardar por outros");
                }

            }

            @NonNull
            @Override
            public AllDocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_display_layout, viewGroup, false);
                AllDocumentsViewHolder viewHolder = new AllDocumentsViewHolder(view);
                return viewHolder;

            }
        };

        AllDocumentsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AllDocumentsViewHolder extends RecyclerView.ViewHolder{

        TextView document_receive_name_editText, send_userName_editText, fromtextView;
        LinearLayout linearLayoutView;


        public AllDocumentsViewHolder(@NonNull View itemView) {
            super(itemView);

            document_receive_name_editText = itemView.findViewById(R.id.document_receive_name_editText);
            send_userName_editText = itemView.findViewById(R.id.send_userName_editText);
            linearLayoutView = itemView.findViewById(R.id.linearLayoutView);
            fromtextView = itemView.findViewById(R.id.fromtextView);
        }
    }
}
