package pt.ipg.a.softdigital;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SendToReceiver extends AppCompatActivity {

    private String receiverUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_receiver);

        receiverUserID = getIntent().getExtras().get("click_user_id").toString();

        Toast.makeText(this, "User ID: " + receiverUserID, Toast.LENGTH_SHORT).show();

    }
}
