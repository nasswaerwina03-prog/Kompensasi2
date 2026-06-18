package com.example.kompensasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class ProfileUserActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    ImageView btnBack, ivProfil;
    MaterialCardView btnEditFoto;
    TextView tvNamaUtama, tvNipUtama;
    TextView tvDetailNama, tvDetailNip, tvDetailJabatan, tvDetailUsername;
    EditText etDetailPassword, etDetailEmail;
    Button btnBatal, btnSimpan;

    String username;
    String currentPassword = "";
    String currentFotoBase64 = null;
    String newFotoBase64 = null;

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        databaseHelper = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        ivProfil = findViewById(R.id.ivProfil);
        btnEditFoto = findViewById(R.id.btnEditFoto);
        
        tvNamaUtama = findViewById(R.id.tvNamaUtama);
        tvNipUtama = findViewById(R.id.tvNipUtama);
        
        tvDetailNama = findViewById(R.id.tvDetailNama);
        tvDetailNip = findViewById(R.id.tvDetailNip);
        tvDetailJabatan = findViewById(R.id.tvDetailJabatan);
        tvDetailUsername = findViewById(R.id.tvDetailUsername);
        etDetailPassword = findViewById(R.id.etDetailPassword);
        etDetailEmail = findViewById(R.id.etDetailEmail);
        btnBatal = findViewById(R.id.btnBatal);
        btnSimpan = findViewById(R.id.btnSimpan);

        SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
        String nama = preferences.getString("nama", "-");
        username = preferences.getString("username", "-");
        String role = preferences.getString("role", "-");

        tvNamaUtama.setText(nama);
        tvNipUtama.setText("NIP: " + username);

        tvDetailNama.setText(nama);
        tvDetailNip.setText(username);
        tvDetailJabatan.setText(role);
        tvDetailUsername.setText(username);

        loadProfileData();

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream is = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            ivProfil.setImageBitmap(bitmap);
                            newFotoBase64 = encodeImage(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnEditFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        btnBatal.setOnClickListener(v -> {
            loadProfileData();
            Toast.makeText(this, "Perubahan dibuang", Toast.LENGTH_SHORT).show();
        });

        btnSimpan.setOnClickListener(v -> {
            String updatedPassword = etDetailPassword.getText().toString();
            String updatedEmail = etDetailEmail.getText().toString();
            if (updatedPassword.isEmpty()) {
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            if (updatedEmail.isEmpty() || updatedEmail.equals("-")) {
                updatedEmail = "";
            }
            databaseHelper.updateUserProfile(username, updatedPassword, newFotoBase64, updatedEmail);
            currentPassword = updatedPassword;
            currentFotoBase64 = newFotoBase64;
            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadProfileData() {
        Cursor c = databaseHelper.getReadableDatabase().rawQuery("SELECT password, foto_profil, email FROM tb_user WHERE username=?", new String[]{username});
        String email = "";
        if (c.moveToFirst()) {
            currentPassword = c.getString(0);
            currentFotoBase64 = c.getString(1);
            email = c.getString(2);
        }
        c.close();

        if (currentPassword == null || currentPassword.isEmpty()) currentPassword = "123";
        etDetailPassword.setText(currentPassword);
        if (email == null || email.isEmpty()) email = "-";
        etDetailEmail.setText(email);
        newFotoBase64 = currentFotoBase64;

        if (currentFotoBase64 != null && !currentFotoBase64.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(currentFotoBase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivProfil.setImageBitmap(decodedByte);
            } catch (Exception e) {
                e.printStackTrace();
                ivProfil.setImageResource(R.drawable.ic_person);
            }
        } else {
            ivProfil.setImageResource(R.drawable.ic_person);
        }
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 300;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
