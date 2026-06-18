package com.example.kompensasi;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class TambahTugasActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    ImageView btnBack;
    EditText etTugas;
    MaterialButton btnSimpan;

    int idKompensasi;

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tugas);

        databaseHelper = new DatabaseHelper(this);

        idKompensasi = getIntent().getIntExtra("id_kompensasi", -1);

        btnBack = findViewById(R.id.btnBack);
        etTugas = findViewById(R.id.etTugas);
        btnSimpan = findViewById(R.id.btnSimpan);

        btnBack.setOnClickListener(v -> finish());

        btnSimpan.setOnClickListener(v -> {
            String tugas = etTugas.getText().toString().trim();

            if (tugas.isEmpty()) {
                Toast.makeText(this, "Tugas wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (idKompensasi == -1) {
                Toast.makeText(this, "ID Kompensasi tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseHelper.updateTugasKompensasi(idKompensasi, tugas);

            Snackbar snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Tugas berhasil disimpan ✓",
                    Snackbar.LENGTH_SHORT
            );
            snackbar.setBackgroundTint(Color.parseColor("#B1456E"));
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();

            finish();
        });
    }
}
