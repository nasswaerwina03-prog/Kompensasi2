package com.example.kompensasi;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class PengajuanActivity extends AppCompatActivity {

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    EditText etNama,
            etNim,
            etKelas,
            etMenit,
            etKeterangan,
            etJamMulai,
            etJamSelesai;

    Spinner spinnerMatkul;

    Button btnAjukan;

    LinearLayout btnUpload,
            menuHome,
            menuAjukan,
            navLogout;

    ImageView btnBack;

    TextView tvUpload;

    // ================= DATA =================

    String nimLogin;

    int idMahasiswa;

    Uri videoUri;

    String buktiFile = "-";

    ArrayList<Integer> kompensasiIdList;

    ArrayList<String> kompensasiList;

    String jamMulaiStr = "-";

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan);

        // ================= DATABASE =================

        databaseHelper = new DatabaseHelper(this);

        // ================= INIT =================

        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        etKelas = findViewById(R.id.etKelas);
        etMenit = findViewById(R.id.etMenit);
        etKeterangan = findViewById(R.id.etKeterangan);
        etJamMulai = findViewById(R.id.etJamMulai);
        etJamSelesai = findViewById(R.id.etJamSelesai);
        spinnerMatkul = findViewById(R.id.spinnerMatkul);
        btnAjukan = findViewById(R.id.btnAjukan);
        btnUpload = findViewById(R.id.btnUpload);
        tvUpload = findViewById(R.id.tvUpload);
        menuHome = findViewById(R.id.menuHome);
        menuAjukan = findViewById(R.id.menuAjukan);
        navLogout = findViewById(R.id.navLogout);
        btnBack = findViewById(R.id.btnBack);

        // ================= DISABLE FIELDS & HIDE UPLOAD =================

        etMenit.setEnabled(false);
        etJamMulai.setEnabled(false);
        etJamSelesai.setEnabled(false);
        btnUpload.setVisibility(View.VISIBLE);
        tvUpload.setVisibility(View.VISIBLE);

        // ================= GET LOGIN =================

        nimLogin = getIntent().getStringExtra("nim");

        // ================= LOAD DATA =================

        loadMahasiswa();
        loadSpinnerKompensasi();

        // ================= SPINNER CHANGE =================

        spinnerMatkul.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadDetailKompensasi(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ================= UPLOAD VIDEO =================

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("video/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(intent, 100);
        });

        // ================= AJUKAN =================

        btnAjukan.setOnClickListener(v -> {

            if (kompensasiIdList.isEmpty()) {
                Toast.makeText(this, "Belum ada sesi tugas", Toast.LENGTH_SHORT).show();
                return;
            }

            if (buktiFile.equals("-")) {
                Toast.makeText(this, "Silakan upload video bukti terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            int idSesi = kompensasiIdList.get(spinnerMatkul.getSelectedItemPosition());

            // Ajukan sesi ini
            databaseHelper.ajukanSesiMahasiswa(idSesi, buktiFile);

            Toast.makeText(this, "Tugas berhasil diajukan ✓", Toast.LENGTH_SHORT).show();
            finish();
        });

        // ================= BACK =================

        btnBack.setOnClickListener(v -> finish());

        // ================= HOME =================

        menuHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardMahasiswaActivity.class);
            intent.putExtra("nim", nimLogin);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        menuAjukan.setOnClickListener(v -> {});

        // ================= LOGOUT =================

        navLogout.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Yakin ingin keluar?");
            builder.setPositiveButton("Ya", (dialog, which) -> {
                android.content.SharedPreferences prefsLogout = getSharedPreferences("SESSION", MODE_PRIVATE);
                prefsLogout.edit().clear().apply();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
            builder.setNegativeButton("Batal", null);
            builder.show();
        });
    }

    // ================= LOAD MAHASISWA =================

    private void loadMahasiswa() {
        Cursor cursor = databaseHelper.searchMahasiswa(nimLogin);
        if (cursor.moveToFirst()) {
            idMahasiswa = cursor.getInt(0);
            etNim.setText(cursor.getString(2));
            etNama.setText(cursor.getString(3));
            etKelas.setText(cursor.getString(4));
        }
        cursor.close();
    }

    // ================= LOAD SPINNER =================

    private void loadSpinnerKompensasi() {
        kompensasiList = new ArrayList<>();
        kompensasiIdList = new ArrayList<>();

        Cursor cursor = databaseHelper.getSesiBelumSelesaiByMahasiswa(idMahasiswa);

        if (cursor.moveToFirst()) {
            do {
                int idSesi = cursor.getInt(0);
                String tugas = cursor.getString(2);
                String tanggal = cursor.getString(6);
                String status = cursor.getString(7);

                kompensasiIdList.add(idSesi);
                kompensasiList.add(tugas + " (" + tanggal + ")" + (status.equals("Ditolak") ? " - [Ditolak]" : ""));

            } while (cursor.moveToNext());
        }
        cursor.close();

        // Jika belum ada tugas
        if (kompensasiList.isEmpty()) {
            kompensasiList.add("Tidak ada sesi tugas yang perlu diajukan");
            btnAjukan.setEnabled(false);
            btnAjukan.setAlpha(0.5f);
            spinnerMatkul.setEnabled(false);
            btnUpload.setEnabled(false);
            Toast.makeText(this, "Tidak ada sesi tugas baru", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                kompensasiList
        );
        spinnerMatkul.setAdapter(adapter);
    }

    // ================= LOAD DETAIL =================

    private void loadDetailKompensasi(int position) {
        if (kompensasiIdList.isEmpty()) return;

        int idSesi = kompensasiIdList.get(position);
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT menit, jam_mulai, jam_selesai FROM tb_sesi WHERE id_sesi = ?",
                new String[]{String.valueOf(idSesi)}
        );

        if (cursor.moveToFirst()) {
            etMenit.setText(cursor.getString(0));
            etJamMulai.setText(cursor.getString(1));
            etJamSelesai.setText(cursor.getString(2));
        }
        cursor.close();
    }

    // ================= RESULT VIDEO =================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            videoUri = data.getData();

            try {
                // Generate a unique filename
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String videoFileName = "VIDEO_" + idMahasiswa + "_" + timeStamp + ".mp4";
                
                // Get the app's internal Videos directory
                File storageDir = new File(getFilesDir(), "Videos");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                
                File videoFile = new File(storageDir, videoFileName);
                
                // Copy the file from the URI to the internal file
                InputStream inputStream = getContentResolver().openInputStream(videoUri);
                FileOutputStream outputStream = new FileOutputStream(videoFile);
                
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                
                inputStream.close();
                outputStream.close();
                
                // Save the absolute path to database instead of the content URI
                buktiFile = videoFile.getAbsolutePath();
                
                tvUpload.setText("✓ Video berhasil disimpan");
                Toast.makeText(this, "Video berhasil dipilih", Toast.LENGTH_SHORT).show();
                
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memproses video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}