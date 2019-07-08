package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference().child("User"); // Declarar a referência do firebase

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // declare and initialize auth

    // UI references
    private EditText mUserNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPassworView;
//    private View mProgressView;
//    private View mLoginFormView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // views
//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
        mUserNameView = findViewById(R.id.UserName);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mConfirmPassworView = findViewById(R.id.ConfirmPassword);

        // botões
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);

        user = new User();

    }

    // on start check user
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    /**
     * Cria uma conta enviando o email e password
     * Se a nova conta foi criada, significa que o utilizador fez login. O método getCurrentUser para ir buscar os dados da conta do utilizador
     *
     * @param email
     * @param password
     */

    private void createAccount(String email, String password) {

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // create user with email
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String UserName = mUserNameView.getText().toString();
                            String UserEmail = mEmailView.getText().toString();

                           User information = new User(UserName, UserEmail);

                           FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   // Sign in success, update UI with the signed-in user's information
                                   Toast.makeText(RegisterActivity.this, getString(R.string.register_sucess), Toast.LENGTH_SHORT).show();
                                   sendEmailVerification();
                               }
                           });

                         //   FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, getString(R.string.register_failed), Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        hideProgressDialog(); // START EXCLUDE

                    }
                });
    }


    private void sendEmailVerification() {

        // send email verification
        final FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // EXCLUDE
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.email_verification_sucess) + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.email_verification_failed), Toast.LENGTH_SHORT).show();
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

        String UserName = mUserNameView.getText().toString();
        if(TextUtils.isEmpty(UserName)){
            mUserNameView.setError(getString(R.string.error_field_required));
            valid = false;
        } else{
            mUserNameView.setError(null);
        }

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

        String ConfirmPassword = mConfirmPassworView.getText().toString();
        if (TextUtils.isEmpty(ConfirmPassword)) {
            mConfirmPassworView.setError(getString(R.string.error_field_required));
            valid = false;
        } else {
            mConfirmPassworView.setError(null);
        }

        if(!password.equals(ConfirmPassword)){
            mConfirmPassworView.setError(getString(R.string.error_doesntmach_password));
            mPasswordView.getText().clear();
            valid = false;
        }else{
            mConfirmPassworView.setError(null);
        }

        return valid;
    }

//    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
//        if (user != null) {
//            //  mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
//
//            //findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
//            findViewById(R.id.email_login_form).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_sign_in_button).setVisibility(View.VISIBLE);
//
//        } else {
//
//            //   mDetailTextView.setText(null);
//
//            // findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_login_form).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_sign_in_button).setVisibility(View.GONE);
//        }
//    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.email_create_account_button) {

            createAccount(mEmailView.getText().toString(), mPasswordView.getText().toString());


        }
        if(i == R.id.email_sign_in_button){

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
