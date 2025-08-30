package com.example.quickswap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsernameEmail, etPassword;
    private CheckBox cbSaveData;
    private Button btnLogin;
    private TextView tvForgetPassword, tvSignup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREFS_NAME = "loginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsernameEmail = findViewById(R.id.et_username_email);
        etPassword = findViewById(R.id.et_password);
        cbSaveData = findViewById(R.id.cb_save_data);
        btnLogin = findViewById(R.id.btn_login);
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        tvSignup = findViewById(R.id.tv_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Auto-fill saved data
        loadSavedData();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgottenPasswordActivity.class));
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loadSavedData() {
        String savedUsername = sharedPreferences.getString("username_email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!TextUtils.isEmpty(savedUsername)) {
            etUsernameEmail.setText(savedUsername);
        }

        if (!TextUtils.isEmpty(savedPassword)) {
            etPassword.setText(savedPassword);
        }

        cbSaveData.setChecked(!TextUtils.isEmpty(savedUsername));
    }

    private void loginUser() {
        String usernameEmail = etUsernameEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate fields
        if (TextUtils.isEmpty(usernameEmail)) {
            etUsernameEmail.setError("Required");
            etUsernameEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required");
            etPassword.requestFocus();
            return;
        }

        if (!isPasswordValid(password)) {
            etPassword.setError("Password must be 6+ chars, include uppercase, lowercase, number & special char");
            etPassword.requestFocus();
            return;
        }

        // If usernameEmail is email
        if (Patterns.EMAIL_ADDRESS.matcher(usernameEmail).matches()) {
            mAuth.signInWithEmailAndPassword(usernameEmail, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveDataIfChecked(usernameEmail, password);
                                navigateToHome();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // If username, check Firestore
            db.collection("users")
                    .whereEqualTo("username", usernameEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String email = document.getString("email");
                                    // Sign in using email from Firestore
                                    mAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> authTask) {
                                                    if (authTask.isSuccessful()) {
                                                        saveDataIfChecked(usernameEmail, password);
                                                        navigateToHome();
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "Login Failed: " + authTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    break; // only first match
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean isPasswordValid(String password) {
        // Minimum 6 chars, 1 upper, 1 lower, 1 number, 1 special
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void saveDataIfChecked(String usernameEmail, String password) {
        if (cbSaveData.isChecked()) {
            editor.putString("username_email", usernameEmail);
            editor.putString("password", password);
            editor.apply();
        } else {
            editor.clear();
            editor.apply();
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
