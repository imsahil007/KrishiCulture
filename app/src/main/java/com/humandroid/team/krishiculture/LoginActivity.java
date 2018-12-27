package com.humandroid.team.krishiculture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    Button signin,sendotp;
    RadioButton viaotp,viapassword;
    EditText username,otp_password;
    RadioGroup radioGroup;
    ProgressBar progressBar;TextWatcher t;


    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //firebase auth object
    private FirebaseAuth mAuth;
    private static final String TAG = "KrishiCulture";
    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            finish();
            callMain();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        signin=(Button)findViewById(R.id.sign_in_button);
        sendotp=(Button)findViewById(R.id.send_otp);
        viaotp=(RadioButton) findViewById(R.id.via_otp);
        viapassword=(RadioButton) findViewById(R.id.via_password);
        username=(EditText)findViewById(R.id.username);
        otp_password=(EditText)findViewById(R.id.otp_password);
        radioGroup=(RadioGroup)findViewById(R.id.logintype);

       // type=pref.getString("type","Student");

        t=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(username.getText().toString().trim().length()==10){
                    sendotp.setClickable(true);
                    sendotp.setBackgroundColor(getResources().getColor(R.color.colorPrimary));  //blue
                    //          otp_password.setFocusable(true);
                    return;
                }
                else{
                    username.setError("Enter a 10 digit mobile no.");
                    username.requestFocus();


                }

            }
        };

        viaotp.performClick();        //default login is through otp



    }


    public void callsignup(View view) {
        startActivity(new Intent(this,signup_activity.class));
    }

    public void setSendotp(View view) {

        sendVerificationCode(username.getText().toString().trim());     //sending mobile no. for receiving otp

    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterotp(v);
            }
        });
    }
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp_password.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                         callMain();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }

                    }


                });
    }

    private void callMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void setviaotp(View view) {


        sendotp.setVisibility(View.VISIBLE);
        sendotp.setClickable(false);
        sendotp.setBackgroundColor(getResources().getColor(R.color.title));  //gray

        username.setHint("Mobile no.");
        username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobilevector, 0, 0, 0);
        username.setInputType(InputType.TYPE_CLASS_PHONE);
        //otp_password.setFocusable(false);
        otp_password.setHint("Your OTP");



        username.addTextChangedListener(t);


    }
    public void setViapassword(View view) {
        sendotp.setVisibility(View.INVISIBLE);
        username.setHint("Email");
        username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.emailvector, 0, 0, 0);
        username.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        otp_password.setHint("Password");


        username.removeTextChangedListener(t);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinviaemail(v);
            }
        });





    }

    public void enterotp(View view) {

        if(otp_password.getText().toString().trim().length()!=6){
            otp_password.requestFocus();
            otp_password.setError("Enter a valid code");
        }
        else       verifyVerificationCode(otp_password.getText().toString().trim());
    }

    private void checkIfEmailVerified(View view)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            Log.d(TAG, "signInWithEmail:success");
            Button a=(Button)view;
            a.setBackgroundColor(Color.GREEN);

                callMain();     //open main activity

            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {Toast.makeText(LoginActivity.this, "Your account is not verified", Toast.LENGTH_SHORT).show();
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            //finish();

            //restart this activity

        }
    }


    public void signinviaemail(final View view) {
        try{
            mAuth.signInWithEmailAndPassword(username.getText().toString(), otp_password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                checkIfEmailVerified(view);


                            } else {
                                // If sign in fails, display a message to the user.
                                //signinbtn.setEnabled(false);
                                Log.w(TAG, "signInWithEmail:failure", task.getException());

                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();


                            }

                            // ...
                        }
                    });}catch (Exception e){Toast.makeText(getApplicationContext(),"Enter email id and password first",Toast.LENGTH_SHORT).show();}

    }

}

