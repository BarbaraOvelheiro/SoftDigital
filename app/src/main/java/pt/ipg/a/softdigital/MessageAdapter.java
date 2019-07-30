package pt.ipg.a.softdigital;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final int MSG_TYPE_NEEDTOSIGN = 0;
    private static final int MSG_TYPE_WAIT = 1;
    String nameUser;
    private String currentUserID;

    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;

    private static final String TAG = "MessageAdapter";


    public MessageAdapter(FragmentActivity activity, List<Messages> userMessagesList){

        this.userMessagesList = userMessagesList;

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView document_receive_name_editText, send_userName_editText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            document_receive_name_editText = itemView.findViewById(R.id.document_receive_name_editText);
            send_userName_editText = itemView.findViewById(R.id.send_userName_editText);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == MSG_TYPE_NEEDTOSIGN) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_receiver_layout, viewGroup, false);
            return new MessageViewHolder(view);
        }else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_receiver_layout, viewGroup, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int i) {

        String message = userMessagesList.get(i).getMessage();
        String from = userMessagesList.get(i).getFrom();

        messageViewHolder.send_userName_editText.setText(from);
        messageViewHolder.document_receive_name_editText.setText(message);

    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    @Override
    public int getItemViewType(int position) {

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        if(userMessagesList.get(position).getFrom().equals(currentUserID)){
            return MSG_TYPE_NEEDTOSIGN;
        }else{
            return MSG_TYPE_WAIT;
        }
    }
}
