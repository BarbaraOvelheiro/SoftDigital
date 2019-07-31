//package pt.ipg.a.softdigital;
//
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class RequestsDocumentsFragment extends Fragment {
//
//    private View RequestsDocumentsFragmentView;
//
//    private RecyclerView documents_request_list;
//
//    private DatabaseReference DocumentRequestRef, UserRef, currentPdfID;
//    private FirebaseAuth mAuth;
//    private String currentUserID;
//
//
//    public RequestsDocumentsFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        RequestsDocumentsFragmentView = inflater.inflate(R.layout.fragment_requests_documents, container, false);
//
//        mAuth = FirebaseAuth.getInstance();
//        currentUserID = mAuth.getCurrentUser().getUid();
//
//        DocumentRequestRef = FirebaseDatabase.getInstance().getReference().child("Document Request");
//        UserRef = FirebaseDatabase.getInstance().getReference().child("User");
//        currentPdfID = FirebaseDatabase.getInstance().getReference().child("Document Request").child(currentUserID);
//
//        documents_request_list = (RecyclerView) RequestsDocumentsFragmentView.findViewById(R.id.documents_request_list);
//        documents_request_list.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        return RequestsDocumentsFragmentView;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerOptions<User> options =
//                new FirebaseRecyclerOptions.Builder<User>()
//                .setQuery(DocumentRequestRef.child(currentUserID), User.class)
//                .build();
//
//        FirebaseRecyclerAdapter<User, RequestsDocumentsViewHolder> adapter =
//                new FirebaseRecyclerAdapter<User, RequestsDocumentsViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull final RequestsDocumentsViewHolder holder, int position, @NonNull User model) {
//
//                        final String list_user_id = getRef(position).getKey();
//
//                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();
//
//                        getTypeRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                if (dataSnapshot.exists()){
//
//                                    String type = dataSnapshot.getValue().toString();
//
//                                    if(type.equals("received")){
//
//                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                if(dataSnapshot.hasChild("userName")){
//
//                                                    final String requestUserName = dataSnapshot.child("userName").getValue().toString();
//
//                                                    holder.send_userName_editText.setText(requestUserName);
//                                                }else{
//                                                    final String requestUserName = dataSnapshot.child("userName").getValue().toString();
//
//                                                    holder.send_userName_editText.setText(requestUserName);
//                                                }
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//
//                                    }
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//                        DatabaseReference getPdfIdRef = getRef(position).child("request_pdf_id").getRef();
//
//                        getPdfIdRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                if (dataSnapshot.exists()){
//
//                                    String type = dataSnapshot.getValue().toString();
//
//                                    if(type.equals("received")){
//
//                                        DocumentRequestRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                if(dataSnapshot.hasChild("name")){
//
//                                                    final String requestDocName = dataSnapshot.child("name").getValue().toString();
//
//                                                    holder.document_receive_name_editText.setText(requestDocName);
//                                                }else{
//                                                    final String requestDocName = dataSnapshot.child("name").getValue().toString();
//
//                                                    holder.document_receive_name_editText.setText(requestDocName);
//                                                }
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//
//                                    }
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public RequestsDocumentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_receiver_layout, viewGroup, false);
//                        RequestsDocumentsViewHolder holder = new RequestsDocumentsViewHolder(view);
//                        return holder;
//
//                    }
//                };
//
//        documents_request_list.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//    public static class RequestsDocumentsViewHolder extends RecyclerView.ViewHolder{
//
//        TextView document_receive_name_editText, send_userName_editText;
//
//        public RequestsDocumentsViewHolder(@NonNull View itemView) {
//
//            super(itemView);
//
//            document_receive_name_editText = itemView.findViewById(R.id.document_receive_name_editText);
//            send_userName_editText = itemView.findViewById(R.id.send_userName_editText);
//
//        }
//    }
//}
