package pt.ipg.a.softdigital;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // declare and Initialize Firebase Auth

    // UI references
    private EditText mEmailView;
    private EditText mPasswordView;
//    private View mProgressView;
//    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // views
//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        // botões
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);

    }

    //  on start check user
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * Entrar com email e password
     *
     * @param email
     * @param password
     */

    private void signIn(String email, final String password) {

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // sign in with email and password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Entrar com sucesso", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, FingerPrintAuth.class);
                            startActivity(intent);
                            finish();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Validação dos campos
     *
     * @return
     */

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            findViewById(R.id.email_login_form).setVisibility(View.VISIBLE);
            findViewById(R.id.email_sign_in_button).setVisibility(View.VISIBLE);

        } else {

            findViewById(R.id.email_login_form).setVisibility(View.VISIBLE);
            findViewById(R.id.email_sign_in_button).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.email_create_account_button) {

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();

        } else if (i == R.id.email_sign_in_button) {

            signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());

        }

    }

}

