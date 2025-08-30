package com.example.quickswap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class ForgottenPasswordActivity extends AppCompatActivity {

    private EditText etEmail, etVerificationCode;
    private TextView tvCheckEmail;
    private Button btnLoginForgot;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String generatedCode = "";
    private boolean isVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_forgotten_password);

        etEmail = findViewById(R.id.et_forgot_email);
        etVerificationCode = findViewById(R.id.et_verification_code);
        tvCheckEmail = findViewById(R.id.tv_check_email);
        btnLoginForgot = findViewById(R.id.btn_login_forgot);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLoginForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString().trim();
                String codeEntered = etVerificationCode.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Required");
                    etEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter valid email");
                    etEmail.requestFocus();
                    return;
                }

                if (!isVerified) {
                    // Check if email exists in Firestore users
                    db.collection("users").document(email).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        if (doc.exists()) {
                                            // Generate 6-digit verification code
                                            generatedCode = String.format("%06d", new Random().nextInt(999999));
                                            // Here, in real app, send email using Email API / Firebase function
                                            Toast.makeText(ForgottenPasswordActivity.this,
                                                    "Verification code sent: " + generatedCode, Toast.LENGTH_LONG).show();
                                            tvCheckEmail.setText("Check your Email and Enter Verification code");
                                            isVerified = true;
                                        } else {
                                            Toast.makeText(ForgottenPasswordActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ForgottenPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Verify entered code
                    if (TextUtils.isEmpty(codeEntered)) {
                        etVerificationCode.setError("Required");
                        etVerificationCode.requestFocus();
                        return;
                    }

                    if (codeEntered.equals(generatedCode)) {
                        Toast.makeText(ForgottenPasswordActivity.this, "Verification successful!", Toast.LENGTH_SHORT).show();
                        // After verification, navigate to login
                        Intent intent = new Intent(ForgottenPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgottenPasswordActivity.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
