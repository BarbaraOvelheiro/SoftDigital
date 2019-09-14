package pt.ipg.a.softdigital;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllDocumentsFragment extends Fragment {

    private View AllDocumentsView;
    private RecyclerView AllDocumentsList;

    private DatabaseReference FilesRef;

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

        return AllDocumentsView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<UploadPdf>()
                .setQuery(FilesRef.child(currentUserID), UploadPdf.class)
                .build();

        FirebaseRecyclerAdapter<UploadPdf, AllDocumentsViewHolder> adapter
                = new FirebaseRecyclerAdapter<UploadPdf, AllDocumentsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AllDocumentsViewHolder holder, final int position, @NonNull final UploadPdf model) {

                holder.document_receive_name_editText.setText(model.getDocumentName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                               // String click_document_id = getRef(position).getKey();
                                String url = model.getDocumentUrl();
                                String receiverPdfID = getRef(position).getKey();

                                Intent intent = new Intent(getActivity(), Pdf_view.class);
                                intent.putExtra("pdfurl",url);
                                intent.putExtra("receiverPdfID", receiverPdfID);
                                startActivity(intent);


                            }
                        });

                    }
                });

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
