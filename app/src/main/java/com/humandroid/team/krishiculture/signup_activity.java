package com.humandroid.team.krishiculture;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup_activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "KrishiCulture";
    EditText email,mobile,password,name;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);
        mAuth = FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.email);
        mobile=(EditText)findViewById(R.id.mobile);
        password=(EditText)findViewById(R.id.password);
        name=(EditText)findViewById(R.id.name);
        submit=(Button)findViewById(R.id.submit);

        TextWatcher t=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().length() < 6)
                    password.setError("Minimum length:6");
                if(name.getText().toString().length()<2)
                    name.setError("Enter a valid name");
                if(mobile.getText().toString().length()!=10)
                    mobile.setError("Enter a valid mobile no.");

            }
        };
        password.addTextChangedListener(t);
        name.addTextChangedListener(t);
        mobile.addTextChangedListener(t);







    }



    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            //  finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Toast.makeText(getApplicationContext(),"Unable to send verification email",Toast.LENGTH_SHORT).show();;
                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

    public void submit(View view) {
        try {

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(getApplicationContext(), "New account created: Check your mail", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signup_activity.this,LoginActivity.class));


                                submit.setEnabled(false);
                                sendVerificationEmail();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(signup_activity.this, "Wrong username/password syntax.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        } catch (Exception E) {
            Toast.makeText(this, "Complete the form first", Toast.LENGTH_SHORT).show();
        }
    }
}
