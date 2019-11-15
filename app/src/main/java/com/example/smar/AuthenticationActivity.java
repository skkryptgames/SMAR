package com.example.smar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AuthenticationActivity extends AppCompatActivity {

    private EditText phoneEnter;
    private EditText otpEnter;
    private Button requestOtp;
    private TextView otp, phoneNumber;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId,userNumber;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        phoneEnter = findViewById(R.id.phoneEnter);
        otpEnter = findViewById(R.id.otpEnter);
        requestOtp = findViewById(R.id.requestOtp);
        otp = findViewById(R.id.otp);
        phoneNumber = findViewById(R.id.phoneNumber);
        progressBar=findViewById(R.id.smar_progressbar);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),"Service error,please try again after short time",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                Toast.makeText(AuthenticationActivity.this, "Otp sent successfully", Toast.LENGTH_SHORT).show();
                otpEnter = (EditText) findViewById(R.id.otpEnter);
                otp = (TextView) findViewById(R.id.otp);
                otpEnter.setVisibility(View.VISIBLE);
                otp.setVisibility(View.VISIBLE);

                requestOtp.setText("Go");

            }
        };
        requestOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneEnter = (EditText) findViewById(R.id.phoneEnter);
                userNumber = phoneEnter.getText().toString();
                String number = "+" + "91" + userNumber;
                PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, AuthenticationActivity.this, mCallbacks);

                phoneEnter.setFilters(new InputFilter[] { new InputFilter.LengthFilter(13) });
                phoneEnter.setText("+91" + userNumber);
                phoneEnter.setEnabled(false);


                requestOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        otpEnter = (EditText) findViewById(R.id.otpEnter);
                        String entered_OTP = otpEnter.getText().toString();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, entered_OTP);
                        signInWithPhoneAuthCredential(credential);
                        progressBar.setVisibility(View.VISIBLE);
                        requestOtp.setEnabled(false);
                        requestOtp.setBackgroundResource(R.drawable.inactive_button_background);

                    }
                });

            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(),"User successfully signed in",Toast.LENGTH_SHORT).show();
                            final String uid=mAuth.getCurrentUser().getUid();
                            final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
                            reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        HashMap<String,Object> a=new HashMap<>();
                                        a.put("signInStatus","signedIn");
                                        reference.child(uid).child("info").updateChildren(a);
                                        if(dataSnapshot.child("info").child("userType").getValue(String.class).equals("admin")){
                                            Intent homeIntent = new Intent(AuthenticationActivity.this, AdminPage.class);
                                            startActivity(homeIntent);
                                            finish();
                                        }else {
                                            Intent clientIntent =new Intent(AuthenticationActivity.this,ClientPage.class);
                                            startActivity(clientIntent);
                                            finish();
                                        }
                                    }else {
                                        if (userNumber.equals("6304267978") || userNumber.equals("9900422344") || userNumber.equals("9900977344") || userNumber.equals("7981168985")) {
                                            HashMap<String, Object> a = new HashMap<>();
                                            a.put("signInStatus","signedIn");
                                            a.put("phoneNumber", userNumber);
                                            a.put("userType", "admin");
                                            reference.child(uid).child("info").updateChildren(a);
                                            Intent homeIntent = new Intent(AuthenticationActivity.this, AdminPage.class);
                                            startActivity(homeIntent);
                                            finish();

                                        } else {
                                            HashMap<String, Object> a = new HashMap<>();
                                            a.put("signInStatus","signedIn");
                                            a.put("phoneNumber", userNumber);
                                            a.put("userType", "client");
                                            reference.child(uid).child("info").updateChildren(a);
                                            Intent clientIntent =new Intent(AuthenticationActivity.this,ClientPage.class);
                                            startActivity(clientIntent);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //homeIntent.putExtra("number",userNumber);

                        } else {
                            Toast.makeText(getApplicationContext(), "You have entered incorrect OTP", Toast.LENGTH_SHORT).show();
                            requestOtp.setEnabled(true);
                            requestOtp.setBackgroundResource(R.drawable.round_button);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


}
