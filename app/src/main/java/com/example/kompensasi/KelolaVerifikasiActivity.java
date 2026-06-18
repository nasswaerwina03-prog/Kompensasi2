package com.example.kompensasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// ================= HALAMAN ACTIVITY =================
// Ini adalah kelas halaman utama untuk antarmuka pengguna.
public class KelolaVerifikasiActivity extends AppCompatActivity {

    // ================= DATABASE =================

    DatabaseHelper databaseHelper;

    // ================= COMPONENT =================

    TextView tvTotalKompensasi,
            tvFilterSemua,
            tvFilterBelum,
            tvFilterSelesai;

    EditText etSearchKompensasi;

    LinearLayout navHome,
            navVerifikasi,
            navLogout;

    RecyclerView recyclerKompensasi;

    // ================= LIST =================

    ArrayList<KompensasiModel> kompensasiList;
    KompensasiDosenAdapter adapter;
    String kelasDosen;
    int currentFilter = 0; // 0: Semua, 1: Belum Selesai, 2: Selesai

    @Override
    // ================= INISIALISASI HALAMAN (ONCREATE) =================
    // Posisi ini dijalankan pertama kali saat halaman dibuka untuk mengatur tampilan.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_kompensasi);

        // ================= DATABASE =================

        databaseHelper = new DatabaseHelper(this);

        // ================= SESSION =================

        SharedPreferences preferences = getSharedPreferences("SESSION", MODE_PRIVATE);
        kelasDosen = preferences.getString("kelas_dosen", "");

        // ================= INIT VIEW =================

        tvTotalKompensasi = findViewById(R.id.tvTotalKompensasi);
        tvFilterSemua = findViewById(R.id.tvFilterSemua);
        tvFilterBelum = findViewById(R.id.tvFilterBelum);
        tvFilterSelesai = findViewById(R.id.tvFilterSelesai);
        etSearchKompensasi = findViewById(R.id.etSearchKompensasi);
        recyclerKompensasi = findViewById(R.id.recyclerKompensasi);
        navHome = findViewById(R.id.navHome);
        navVerifikasi = findViewById(R.id.navVerifikasi);
        navLogout = findViewById(R.id.navLogout);

        // ================= RECYCLER =================

        kompensasiList = new ArrayList<>();
        adapter = new KompensasiDosenAdapter(this, kompensasiList);
        recyclerKompensasi.setLayoutManager(new LinearLayoutManager(this));
        recyclerKompensasi.setAdapter(adapter);

        // ================= LOAD =================

        loadTotalKompensasi();
        loadKompensasiData("");

        // ================= SEARCH =================

        etSearchKompensasi.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadKompensasiData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // ================= FILTER TABS =================

        tvFilterSemua.setOnClickListener(v -> {
            updateTabUI(tvFilterSemua, tvFilterBelum, tvFilterSelesai);
            currentFilter = 0;
            loadKompensasiData(etSearchKompensasi.getText().toString());
        });

        tvFilterBelum.setOnClickListener(v -> {
            updateTabUI(tvFilterBelum, tvFilterSemua, tvFilterSelesai);
            currentFilter = 1;
            loadKompensasiData(etSearchKompensasi.getText().toString());
        });

        tvFilterSelesai.setOnClickListener(v -> {
            updateTabUI(tvFilterSelesai, tvFilterSemua, tvFilterBelum);
            currentFilter = 2;
            loadKompensasiData(etSearchKompensasi.getText().toString());
        });

        // ================= NAVIGATION =================

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardDosenPaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });



        navVerifikasi.setOnClickListener(v -> {
            // Already here
        });

        // ================= LOGOUT =================

        navLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Yakin ingin keluar?");
            builder.setPositiveButton("Logout", (dialog, which) -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("Batal", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#B1456E"));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
        });
    }

    // ================= TAB UI =================

    private void updateTabUI(TextView active, TextView inactive1, TextView inactive2) {
        active.setTextColor(android.graphics.Color.parseColor("#3A2A33"));
        active.setTypeface(null, android.graphics.Typeface.BOLD);
        active.setBackgroundResource(R.drawable.tab_bg);

        inactive1.setTextColor(android.graphics.Color.parseColor("#999999"));
        inactive1.setTypeface(null, android.graphics.Typeface.NORMAL);
        inactive1.setBackgroundResource(0);

        inactive2.setTextColor(android.graphics.Color.parseColor("#999999"));
        inactive2.setTypeface(null, android.graphics.Typeface.NORMAL);
        inactive2.setBackgroundResource(0);
    }

    // ================= TOTAL =================

    private void loadTotalKompensasi() {
        Cursor cursor = databaseHelper.searchKompensasiByKelas("", kelasDosen);
        tvTotalKompensasi.setText(String.valueOf(cursor.getCount()));
        cursor.close();
    }

    // ================= LOAD DATA =================

    private void loadKompensasiData(String keyword) {

        kompensasiList.clear();

        Cursor cursor = databaseHelper.searchKompensasiByKelas(keyword, kelasDosen);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String namaMahasiswa = cursor.getString(1);
                String nim = cursor.getString(2);
                String kelas = cursor.getString(3);
                int jumlahMenit = cursor.getInt(4);
                int sisaMenit = cursor.getInt(5);
                String tugas = cursor.getString(6);
                String status = cursor.getString(7);
                String tanggalMulai = cursor.getString(8);
                String tanggal = cursor.getString(9);
                String tanggalSelesai = cursor.getString(10);
                String jamMulai = cursor.getString(11);
                String jamSelesai = cursor.getString(12);
                String fotoProfil = null;
                try {
                    fotoProfil = cursor.getString(cursor.getColumnIndexOrThrow("foto_profil"));
                } catch (Exception e) {}

                if (currentFilter == 1 && sisaMenit <= 0) continue;
                if (currentFilter == 2 && sisaMenit > 0) continue;

                kompensasiList.add(new KompensasiModel(
                        id,
                        namaMahasiswa,
                        nim,
                        kelas,
                        jumlahMenit,
                        sisaMenit,
                        tugas,
                        status,
                        tanggalMulai,
                        tanggal,
                        tanggalSelesai,
                        jamMulai,
                        jamSelesai,
                        fotoProfil
                ));

            } while (cursor.moveToNext());
        }

        adapter = new KompensasiDosenAdapter(this, kompensasiList);
        recyclerKompensasi.setAdapter(adapter);
        cursor.close();
    }
}