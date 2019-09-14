package pt.ipg.a.softdigital;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
public class NeedToSignDocumentosFragment extends Fragment {

    private static final String TAG = "NeedToSignDocumentosFragment";

    private View NeedToSignDocuments;

    private RecyclerView receiver_documents_list;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, MessagesRef, documentStatus, FilesRef;

    private String currentUserID;

    private static final String CHANNEL_ID = "doc_notification";
    private static final String CHANNEL_NAME = "Doc Notification";
    private static final String CHANNEL_DESC = "Doc Sign Notification";

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

        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
        documentStatus = FirebaseDatabase.getInstance().getReference().child("Document Status").child(currentUserID);
        FilesRef = FirebaseDatabase.getInstance().getReference().child("Files");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }


        return NeedToSignDocuments;

    }

    @Override
    public void onStart() {
        super.onStart();

        /************/
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DocumentStatus>()
                .setQuery(documentStatus, DocumentStatus.class).build();

        FirebaseRecyclerAdapter<DocumentStatus, NeedToSignDocumentsFragmentViewHolder> adapter
                = new FirebaseRecyclerAdapter<DocumentStatus, NeedToSignDocumentsFragmentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final NeedToSignDocumentsFragmentViewHolder holder, final int position, @NonNull final DocumentStatus model) {

                String messageID = model.getMessageID();
                String status = model.getStatus();
                Log.v(TAG, "status: " +status);
                if(status.equals("e_necessario_assinar")) {

                    if (messageID == null) {

                        String documentName = model.getDocumentID();
                        FilesRef = FirebaseDatabase.getInstance().getReference().child("Files").child(currentUserID).child(documentName);
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
                                                    String receiverPdfID = getRef(position).getKey();
                                                    String receiverStatusID = model.getStatusID();

                                                    Intent intent = new Intent(getActivity(), Pdf_view.class);
                                                    intent.putExtra("pdfurl", url);
                                                    intent.putExtra("receiverPdfID", receiverPdfID);
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

                    } else {
                        holder.fromtextView.setVisibility(View.VISIBLE);
                        holder.send_userName_editText.setVisibility(View.VISIBLE);
                        holder.linearLayoutView.setVisibility(View.VISIBLE);

                        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(messageID);
                        MessagesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("userFromID")) {
                                    String userName = dataSnapshot.child("userFromID").getValue().toString();

                                    UserRef = FirebaseDatabase.getInstance().getReference().child("User").child(userName);
                                    UserRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("userName")) {

                                                displayNotification();

                                                String receiverName = dataSnapshot.child("userName").getValue().toString();
                                                holder.send_userName_editText.setText(receiverName);

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
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
                                                    String receiverPdfID = getRef(position).getKey();
                                                    String receiverStatusID = model.getStatusID();

                                                    Intent intent = new Intent(getActivity(), Pdf_view.class);
                                                    intent.putExtra("pdfurl", url);
                                                    intent.putExtra("receiverPdfID", receiverPdfID);
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
                }else{
                    holder.linearLayoutView.setVisibility(View.INVISIBLE);
                }
            }

            @NonNull
            @Override
            public NeedToSignDocumentsFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_display_layout, viewGroup, false);
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

        TextView document_receive_name_editText, send_userName_editText, fromtextView;
        LinearLayout linearLayoutView;

        public NeedToSignDocumentsFragmentViewHolder(@NonNull View itemView) {
            super(itemView);

            document_receive_name_editText = itemView.findViewById(R.id.document_receive_name_editText);
            send_userName_editText = itemView.findViewById(R.id.send_userName_editText);
            linearLayoutView = itemView.findViewById(R.id.linearLayoutView);
            fromtextView = itemView.findViewById(R.id.fromtextView);
        }
    }

    private void displayNotification(){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.assinatura)
                .setContentTitle("Novo Documento")
                .setContentText("Recebeu um documento para assinatura")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(1, mBuilder.build());



    }

}
