package com.example.kompensasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class LupaPasswordActivity extends AppCompatActivity {

    ImageView btnBack;
    TextInputEditText etUsername;
    Button btnKirimOTP;
    ProgressBar progressBar;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        databaseHelper = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        etUsername = findViewById(R.id.etUsername);
        btnKirimOTP = findViewById(R.id.btnKirimOTP);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());

        btnKirimOTP.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (username.isEmpty()) {
                etUsername.setError("Masukkan NIM/Username");
                return;
            }

            // Check if user is Mahasiswa or User
            String email = databaseHelper.getEmailMahasiswa(username);
            if (email == null) {
                email = databaseHelper.getEmailUser(username);
            }

            if (email == null) {
                Toast.makeText(this, "NIM/Username tidak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Email belum didaftarkan. Hubungi Administrator.", Toast.LENGTH_LONG).show();
                return;
            }

            sendOtpEmail(username, email);
        });
    }

    private void sendOtpEmail(String username, String email) {
        btnKirimOTP.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        // Generate 6 digit OTP
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);

        String subject = "Kode OTP Reset Sandi - Kompensasi Polinema";
        String message = "Halo,\n\n" +
                "Anda meminta untuk mereset sandi akun Anda.\n" +
                "Gunakan kode OTP berikut untuk melanjutkan proses reset sandi:\n\n" +
                "Kode OTP: " + otp + "\n\n" +
                "Jangan bagikan kode ini kepada siapa pun.\n\n" +
                "Terima kasih,\nSistem Kompensasi Polinema";

        new EmailSender(email, subject, message, new EmailSender.EmailCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnKirimOTP.setEnabled(true);
                    Toast.makeText(LupaPasswordActivity.this, "Kode OTP telah dikirim ke email " + email, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(LupaPasswordActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("otp", otp);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnKirimOTP.setEnabled(true);
                    String errorMsg = e != null ? e.getMessage() : "Unknown error";
                    Toast.makeText(LupaPasswordActivity.this, "Gagal: " + errorMsg, Toast.LENGTH_LONG).show();
                });
            }
        }).execute();
    }
}
