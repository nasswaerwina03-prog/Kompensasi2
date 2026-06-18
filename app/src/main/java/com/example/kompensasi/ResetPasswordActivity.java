package com.example.kompensasi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ResetPasswordActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvDeskripsiEmail;
    TextInputEditText etOTP, etPasswordBaru;
    Button btnReset;
    DatabaseHelper databaseHelper;

    String targetUsername;
    String targetEmail;
    String validOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        databaseHelper = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        tvDeskripsiEmail = findViewById(R.id.tvDeskripsiEmail);
        etOTP = findViewById(R.id.etOTP);
        etPasswordBaru = findViewById(R.id.etPasswordBaru);
        btnReset = findViewById(R.id.btnReset);

        targetUsername = getIntent().getStringExtra("username");
        targetEmail = getIntent().getStringExtra("email");
        validOtp = getIntent().getStringExtra("otp");

        tvDeskripsiEmail.setText("Kode OTP telah dikirimkan ke email:\n" + targetEmail);

        btnBack.setOnClickListener(v -> finish());

        btnReset.setOnClickListener(v -> {
            String inputOtp = etOTP.getText().toString().trim();
            String passwordBaru = etPasswordBaru.getText().toString().trim();

            if (inputOtp.isEmpty() || passwordBaru.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi OTP dan Password Baru", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!inputOtp.equals(validOtp)) {
                etOTP.setError("Kode OTP salah");
                return;
            }

            // Update database
            boolean isMahasiswa = databaseHelper.isMahasiswa(targetUsername);
            if (isMahasiswa) {
                // For mahasiswa, we only have updateMahasiswaProfile which takes nim, password, foto, email.
                // We don't want to change foto, so we should fetch it first.
                android.database.Cursor c = databaseHelper.getReadableDatabase().rawQuery("SELECT foto_profil FROM tb_mahasiswa WHERE nim=?", new String[]{targetUsername});
                String foto = null;
                if (c.moveToFirst()) foto = c.getString(0);
                c.close();

                databaseHelper.updateMahasiswaProfile(targetUsername, passwordBaru, foto, targetEmail);
            } else {
                // For user
                android.database.Cursor c = databaseHelper.getReadableDatabase().rawQuery("SELECT foto_profil FROM tb_user WHERE username=?", new String[]{targetUsername});
                String foto = null;
                if (c.moveToFirst()) foto = c.getString(0);
                c.close();

                databaseHelper.updateUserProfile(targetUsername, passwordBaru, foto, targetEmail);
            }

            Toast.makeText(this, "Sandi berhasil direset. Silakan login.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
